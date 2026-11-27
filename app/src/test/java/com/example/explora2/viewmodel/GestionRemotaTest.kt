package com.example.explora2.viewmodel

import com.example.explora2.data.model.PlanificacionDto
import com.example.explora2.data.remote.ExploraApiService
import com.example.explora2.repository.PlanificacionRepository
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GestionRemotaTest : FunSpec({
    lateinit var api: ExploraApiService
    lateinit var vm: GestionViewModel
    val plan = PlanificacionDto(id = 47, titulo = "Lectura", enviadoUtp = true)

    fun completar() {
        vm.onAsignaturaChange("Lenguaje")
        vm.onUnidadChange("Unidad 1")
        vm.onObjetivoChange("Comprender")
        vm.onTipoRecursoChange("Texto")
        vm.onTituloChange("Lectura")
        vm.onDescripcionChange("Leer una fábula")
    }

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        api = mockk()
        vm = GestionViewModel(PlanificacionRepository(api))
    }
    afterTest { Dispatchers.resetMain() }

    test("el formulario incompleto no hace POST") {
        vm.enviarAUtp()
        coVerify(exactly = 0) { api.crear(any()) }
        assertNotNull(vm.estado.value.errores.titulo)
    }
    test("201 conserva el id y bloquea un nuevo envío del mismo borrador") {
        completar()
        coEvery { api.crear(any()) } returns Response.success(201, plan)
        vm.enviarAUtp()
        vm.enviarAUtp()
        assertEquals(47L, vm.estado.value.idRemoto)
        assertTrue(vm.estado.value.enviadoUtp)
        coVerify(exactly = 1) { api.crear(match { it.id == null && it.titulo == "Lectura" && it.enviadoUtp && it.validado }) }
        vm.nuevaPlanificacion()
        assertNull(vm.estado.value.idRemoto)
    }
    test("una conexión rechazada conserva el borrador sin confirmar") {
        completar()
        coEvery { api.crear(any()) } throws ConnectException()
        vm.enviarAUtp()
        assertFalse(vm.estado.value.enviadoUtp)
        assertFalse(vm.estado.value.enviando)
        assertEquals("Leer una fábula", vm.estado.value.descripcion)
        assertNotNull(vm.estado.value.errorEnvio)
    }
    test("dos pulsaciones mientras está pendiente solo hacen un POST") {
        completar()
        val pendiente = CompletableDeferred<Response<PlanificacionDto>>()
        coEvery { api.crear(any()) } coAnswers { pendiente.await() }
        vm.enviarAUtp()
        vm.enviarAUtp()
        assertTrue(vm.estado.value.enviando)
        pendiente.complete(Response.success(201, plan))
        coVerify(exactly = 1) { api.crear(any()) }
        assertEquals(47L, vm.estado.value.idRemoto)
    }
    test("si se pierde la respuesta no se reenvía automáticamente") {
        completar()
        coEvery { api.crear(any()) } throws SocketTimeoutException()
        vm.enviarAUtp()
        assertFalse(vm.estado.value.enviadoUtp)
        assertTrue(vm.estado.value.errorEnvio.orEmpty().contains("Revisa"))
        coVerify(exactly = 1) { api.crear(any()) }
    }
    test("una creación sin id no confirma el envío") {
        completar()
        coEvery { api.crear(any()) } returns Response.success(201, plan.copy(id = null))
        vm.enviarAUtp()
        assertNull(vm.estado.value.idRemoto)
        assertFalse(vm.estado.value.enviadoUtp)
    }
    test("UTP recupera solo las solicitudes enviadas") {
        coEvery { api.listar() } returns listOf(plan, plan.copy(id = 48, enviadoUtp = false))
        vm.cargarSolicitudes()
        assertEquals(listOf(plan), vm.consulta.value.solicitudes)
    }
    test("UTP distingue una colección vacía") {
        coEvery { api.listar() } returns emptyList()
        vm.cargarSolicitudes()
        assertTrue(vm.consulta.value.solicitudes.isEmpty())
        assertNull(vm.consulta.value.error)
        assertFalse(vm.consulta.value.cargando)
    }
    test("UTP informa un error de lectura") {
        coEvery { api.listar() } throws ConnectException()
        vm.cargarSolicitudes()
        assertNotNull(vm.consulta.value.error)
        assertFalse(vm.consulta.value.cargando)
    }
    test("UTP consulta el detalle por id sin alterar el formulario") {
        coEvery { api.obtenerPorId(47) } returns plan
        vm.consultarDetalle(47)
        assertEquals(plan, vm.consulta.value.detalle)
        assertNull(vm.estado.value.idRemoto)
    }
    test("una solicitud eliminada produce un mensaje de detalle") {
        coEvery { api.obtenerPorId(47) } throws retrofit2.HttpException(
            Response.error<PlanificacionDto>(404, "".toResponseBody())
        )
        vm.consultarDetalle(47)
        assertTrue(vm.consulta.value.error.orEmpty().contains("ya no está disponible"))
    }

    test("UTP autoriza una planificación sin alterar sus atributos") {
        val pendiente = plan.copy(
            validado = true,
            autorizado = false,
            asignadoAlumno = false,
            descripcion = "Leer una fábula"
        )

        val autorizada = pendiente.copy(autorizado = true)

        coEvery { api.obtenerPorId(47) } returns pendiente
        coEvery { api.actualizar(47, autorizada) } returns
                Response.success(autorizada)

        vm.consultarDetalle(47)
        vm.autorizarPlanificacion(47)

        assertTrue(vm.consulta.value.detalle!!.autorizado)
        assertFalse(vm.consulta.value.detalle!!.asignadoAlumno)
        assertFalse(vm.consulta.value.autorizando)
        assertEquals(
            autorizada,
            vm.consulta.value.detalle
        )

        coVerify(exactly = 1) {
            api.actualizar(47, autorizada)
        }
    }

    test("UTP no autoriza una planificación sin validar") {
        val invalida = plan.copy(
            validado = false,
            autorizado = false
        )

        coEvery { api.obtenerPorId(47) } returns invalida

        vm.consultarDetalle(47)
        vm.autorizarPlanificacion(47)

        assertFalse(vm.consulta.value.detalle!!.autorizado)
        assertNotNull(vm.consulta.value.error)
        assertFalse(vm.consulta.value.autorizando)

        coVerify(exactly = 0) {
            api.actualizar(any(), any())
        }
    }

    test("un error de conexión no confirma la autorización") {
        val pendiente = plan.copy(
            validado = true,
            autorizado = false
        )

        coEvery { api.obtenerPorId(47) } returns pendiente

        coEvery {
            api.actualizar(any(), any())
        } throws ConnectException()

        vm.consultarDetalle(47)
        vm.autorizarPlanificacion(47)

        assertFalse(vm.consulta.value.detalle!!.autorizado)
        assertFalse(vm.consulta.value.autorizando)
        assertNotNull(vm.consulta.value.error)
    }

    test("dos pulsaciones simultáneas solo realizan un PUT") {
        val pendiente = plan.copy(
            validado = true,
            autorizado = false
        )

        val autorizada = pendiente.copy(autorizado = true)

        val respuestaPendiente =
            CompletableDeferred<Response<PlanificacionDto>>()

        coEvery { api.obtenerPorId(47) } returns pendiente

        coEvery {
            api.actualizar(47, autorizada)
        } coAnswers {
            respuestaPendiente.await()
        }

        vm.consultarDetalle(47)
        vm.autorizarPlanificacion(47)
        vm.autorizarPlanificacion(47)

        assertTrue(vm.consulta.value.autorizando)

        respuestaPendiente.complete(
            Response.success(autorizada)
        )

        coVerify(exactly = 1) {
            api.actualizar(47, autorizada)
        }

        assertTrue(vm.consulta.value.detalle!!.autorizado)
        assertFalse(vm.consulta.value.autorizando)
    }

    test("una planificación ya autorizada no necesita otro PUT") {
        val autorizada = plan.copy(
            validado = true,
            autorizado = true
        )

        coEvery { api.obtenerPorId(47) } returns autorizada

        vm.consultarDetalle(47)
        vm.autorizarPlanificacion(47)

        assertTrue(vm.consulta.value.detalle!!.autorizado)
        assertNull(vm.consulta.value.error)

        coVerify(exactly = 0) {
            api.actualizar(any(), any())
        }
    }

    test("docente recupera solo planificaciones autorizadas") {

        val autorizada = plan.copy(
            validado = true,
            autorizado = true
        )

        val pendiente = autorizada.copy(
            id = 48,
            autorizado = false
        )

        coEvery { api.listar() } returns
                listOf(autorizada, pendiente)

        vm.cargarPlanificacionesDocente()

        assertEquals(
            listOf(autorizada),
            vm.consultaDocente.value.planificaciones
        )

        assertFalse(vm.consultaDocente.value.cargando)
        assertNull(vm.consultaDocente.value.error)
    }

    test("docente asigna una planificación autorizada") {

        val autorizada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false
        )

        val asignada = autorizada.copy(
            asignadoAlumno = true
        )

        coEvery { api.listar() } returns listOf(autorizada)

        coEvery { api.obtenerPorId(47) } returns autorizada

        coEvery { api.actualizar(47, asignada) } returns
                Response.success(asignada)

        vm.cargarPlanificacionesDocente()
        vm.asignarPlanificacion(47)

        assertEquals(
            asignada,
            vm.consultaDocente.value.planificaciones.first()
        )

        assertNull(vm.consultaDocente.value.asignandoId)
        assertNull(vm.consultaDocente.value.error)

        coVerify(exactly = 1) {
            api.actualizar(47, asignada)
        }
    }

    test("docente no puede asignar una planificación sin autorización") {

        val pendiente = plan.copy(
            validado = true,
            autorizado = false
        )

        coEvery { api.listar() } returns listOf(pendiente)

        coEvery { api.obtenerPorId(47) } returns pendiente

        vm.cargarPlanificacionesDocente()
        vm.asignarPlanificacion(47)

        assertNotNull(vm.consultaDocente.value.error)
        assertNull(vm.consultaDocente.value.asignandoId)

        coVerify(exactly = 0) {
            api.actualizar(any(), any())
        }
    }

    test("un error de conexión no confirma la asignación") {

        val autorizada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false
        )

        coEvery { api.listar() } returns listOf(autorizada)

        coEvery { api.obtenerPorId(47) } returns autorizada

        coEvery {
            api.actualizar(any(), any())
        } throws ConnectException()

        vm.cargarPlanificacionesDocente()
        vm.asignarPlanificacion(47)

        assertFalse(
            vm.consultaDocente.value
                .planificaciones.first().asignadoAlumno
        )

        assertNotNull(vm.consultaDocente.value.error)
        assertNull(vm.consultaDocente.value.asignandoId)
    }

    test("dos pulsaciones simultáneas solo realizan una asignación") {

        val autorizada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false
        )

        val asignada = autorizada.copy(
            asignadoAlumno = true
        )

        val respuestaPendiente =
            CompletableDeferred<Response<PlanificacionDto>>()

        coEvery { api.listar() } returns listOf(autorizada)

        coEvery { api.obtenerPorId(47) } returns autorizada

        coEvery {
            api.actualizar(47, asignada)
        } coAnswers {
            respuestaPendiente.await()
        }

        vm.cargarPlanificacionesDocente()

        vm.asignarPlanificacion(47)
        vm.asignarPlanificacion(47)

        assertEquals(
            47L,
            vm.consultaDocente.value.asignandoId
        )

        respuestaPendiente.complete(
            Response.success(asignada)
        )

        coVerify(exactly = 1) {
            api.actualizar(47, asignada)
        }

        assertTrue(
            vm.consultaDocente.value
                .planificaciones.first().asignadoAlumno
        )

        assertNull(vm.consultaDocente.value.asignandoId)
    }

    test("asignar una planificación no modifica las demás") {

        val primera = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false
        )

        val segunda = primera.copy(
            id = 48,
            titulo = "Actividad de Matemática"
        )

        val asignada = primera.copy(
            asignadoAlumno = true
        )

        coEvery { api.listar() } returns
                listOf(primera, segunda)

        coEvery { api.obtenerPorId(47) } returns primera

        coEvery { api.actualizar(47, asignada) } returns
                Response.success(asignada)

        vm.cargarPlanificacionesDocente()
        vm.asignarPlanificacion(47)

        assertEquals(
            listOf(asignada, segunda),
            vm.consultaDocente.value.planificaciones
        )

        coVerify(exactly = 0) {
            api.actualizar(48, any())
        }
    }

    test("alumno recupera solo actividades asignadas") {

        val asignada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true
        )

        val noAsignada = asignada.copy(
            id = 48,
            asignadoAlumno = false
        )

        val noAutorizada = asignada.copy(
            id = 49,
            autorizado = false
        )

        coEvery { api.listar() } returns
                listOf(asignada, noAsignada, noAutorizada)

        vm.cargarActividadesAlumno()

        assertEquals(
            listOf(asignada),
            vm.consultaAlumno.value.actividades
        )

        assertFalse(vm.consultaAlumno.value.cargando)
        assertNull(vm.consultaAlumno.value.error)
    }

    test("alumno no puede abrir una actividad sin asignar") {

        val actividad = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false
        )

        coEvery { api.obtenerPorId(47) } returns actividad

        vm.abrirActividadAlumno(47)

        assertNull(vm.consultaAlumno.value.detalle)
        assertNotNull(vm.consultaAlumno.value.errorDetalle)
        assertFalse(vm.consultaAlumno.value.cargandoDetalle)
    }


    test("alumno no puede entregar una respuesta vacía") {

        val actividad = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true
        )

        coEvery { api.obtenerPorId(47) } returns actividad

        vm.abrirActividadAlumno(47)
        vm.cambiarRespuestaRemota("   ")
        vm.entregarRespuestaRemota()

        assertNotNull(vm.consultaAlumno.value.errorRespuesta)

        assertFalse(
            vm.consultaAlumno.value.detalle!!.respuestaEntregada
        )

        coVerify(exactly = 0) {
            api.actualizar(any(), any())
        }
    }


    test("alumno entrega una respuesta correctamente") {

        val actividad = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaEntregada = false
        )

        val entregada = actividad.copy(
            respuestaAlumno = "El león y el ratón",
            respuestaEntregada = true
        )

        coEvery { api.listar() } returns listOf(actividad)

        coEvery { api.obtenerPorId(47) } returns actividad

        coEvery { api.actualizar(47, entregada) } returns
                Response.success(entregada)

        vm.cargarActividadesAlumno()
        vm.abrirActividadAlumno(47)
        vm.cambiarRespuestaRemota("El león y el ratón")
        vm.entregarRespuestaRemota()

        assertEquals(
            entregada,
            vm.consultaAlumno.value.detalle
        )

        assertEquals(
            entregada,
            vm.consultaAlumno.value.actividades.first()
        )

        assertNull(vm.consultaAlumno.value.errorRespuesta)
        assertFalse(vm.consultaAlumno.value.enviando)

        coVerify(exactly = 1) {
            api.actualizar(47, entregada)
        }
    }

    test("un error de conexión conserva la respuesta escrita") {

        val actividad = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true
        )

        coEvery { api.obtenerPorId(47) } returns actividad

        coEvery {
            api.actualizar(any(), any())
        } throws ConnectException()

        vm.abrirActividadAlumno(47)
        vm.cambiarRespuestaRemota("Mi respuesta")
        vm.entregarRespuestaRemota()

        assertEquals(
            "Mi respuesta",
            vm.consultaAlumno.value.respuesta
        )

        assertFalse(
            vm.consultaAlumno.value.detalle!!.respuestaEntregada
        )

        assertNotNull(vm.consultaAlumno.value.errorRespuesta)
        assertFalse(vm.consultaAlumno.value.enviando)
    }


    test("dos pulsaciones simultáneas solo realizan una entrega") {

        val actividad = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true
        )

        val entregada = actividad.copy(
            respuestaAlumno = "Mi respuesta",
            respuestaEntregada = true
        )

        val pendiente =
            CompletableDeferred<Response<PlanificacionDto>>()

        coEvery { api.obtenerPorId(47) } returns actividad

        coEvery {
            api.actualizar(47, entregada)
        } coAnswers {
            pendiente.await()
        }

        vm.abrirActividadAlumno(47)
        vm.cambiarRespuestaRemota("Mi respuesta")

        vm.entregarRespuestaRemota()
        vm.entregarRespuestaRemota()

        assertTrue(vm.consultaAlumno.value.enviando)

        pendiente.complete(
            Response.success(entregada)
        )

        coVerify(exactly = 1) {
            api.actualizar(47, entregada)
        }

        assertTrue(
            vm.consultaAlumno.value.detalle!!.respuestaEntregada
        )

        assertFalse(vm.consultaAlumno.value.enviando)
    }


    test("alumno recupera una respuesta previamente entregada") {

        val entregada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaAlumno = "Respuesta original",
            respuestaEntregada = true
        )

        coEvery { api.obtenerPorId(47) } returns entregada

        vm.abrirActividadAlumno(47)
        vm.cambiarRespuestaRemota("Otra respuesta")
        vm.entregarRespuestaRemota()

        assertEquals(
            "Respuesta original",
            vm.consultaAlumno.value.respuesta
        )

        assertTrue(
            vm.consultaAlumno.value.detalle!!.respuestaEntregada
        )

        coVerify(exactly = 0) {
            api.actualizar(any(), any())
        }
    }


    test("una entrega existente en el servidor no se sobrescribe") {

        val actividad = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true
        )

        val entregada = actividad.copy(
            respuestaAlumno = "Respuesta guardada",
            respuestaEntregada = true
        )

        coEvery {
            api.obtenerPorId(47)
        } returnsMany listOf(actividad, entregada)

        vm.abrirActividadAlumno(47)
        vm.cambiarRespuestaRemota("Nueva respuesta")
        vm.entregarRespuestaRemota()

        assertEquals(
            "Respuesta guardada",
            vm.consultaAlumno.value.respuesta
        )

        assertTrue(
            vm.consultaAlumno.value.detalle!!.respuestaEntregada
        )

        coVerify(exactly = 0) {
            api.actualizar(any(), any())
        }
    }


    test("docente recupera solo actividades entregadas") {

        val entregada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaEntregada = true,
            respuestaAlumno = "Mi respuesta"
        )

        val pendiente = entregada.copy(
            id = 48,
            respuestaEntregada = false,
            respuestaAlumno = ""
        )

        coEvery { api.listar() } returns
                listOf(entregada, pendiente)

        vm.cargarEntregasDocente()

        assertEquals(
            listOf(entregada),
            vm.revisionEntregas.value.entregas
        )

        assertFalse(vm.revisionEntregas.value.cargando)
        assertNull(vm.revisionEntregas.value.error)
    }

    test("docente recupera varias entregas con sus respuestas") {

        val primera = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaEntregada = true,
            respuestaAlumno = "El leon y el raton"
        )

        val segunda = primera.copy(
            id = 48,
            titulo = "Actividad de Matemática",
            respuestaAlumno = "El resultado es 25"
        )

        coEvery { api.listar() } returns
                listOf(primera, segunda)

        vm.cargarEntregasDocente()

        assertEquals(
            listOf(primera, segunda),
            vm.revisionEntregas.value.entregas
        )

        assertEquals(
            "El leon y el raton",
            vm.revisionEntregas.value.entregas[0].respuestaAlumno
        )

        assertEquals(
            "El resultado es 25",
            vm.revisionEntregas.value.entregas[1].respuestaAlumno
        )
    }

    test("docente puede consultar una lista sin entregas") {

        coEvery { api.listar() } returns emptyList()

        vm.cargarEntregasDocente()

        assertTrue(
            vm.revisionEntregas.value.entregas.isEmpty()
        )

        assertFalse(vm.revisionEntregas.value.cargando)
        assertNull(vm.revisionEntregas.value.error)
    }

    test("error de conexion al recuperar entregas") {

        coEvery {
            api.listar()
        } throws ConnectException()

        vm.cargarEntregasDocente()

        assertTrue(
            vm.revisionEntregas.value.entregas.isEmpty()
        )

        assertNotNull(vm.revisionEntregas.value.error)
        assertFalse(vm.revisionEntregas.value.cargando)
    }

    test("docente recupera entregas despues de reintentar") {

        val entregada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaEntregada = true,
            respuestaAlumno = "Respuesta guardada"
        )

        coEvery {
            api.listar()
        } throws ConnectException()

        vm.cargarEntregasDocente()

        assertNotNull(vm.revisionEntregas.value.error)

        coEvery {
            api.listar()
        } returns listOf(entregada)

        vm.cargarEntregasDocente()

        assertNull(vm.revisionEntregas.value.error)

        assertEquals(
            listOf(entregada),
            vm.revisionEntregas.value.entregas
        )

        assertFalse(vm.revisionEntregas.value.cargando)
    }

    test("docente elimina una planificacion no asignada") {

        val eliminable = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false,
            respuestaEntregada = false
        )

        coEvery { api.listar() } returnsMany
                listOf(listOf(eliminable), emptyList())

        coEvery { api.obtenerPorId(47) } returns eliminable

        coEvery { api.eliminar(47) } returns
                Response.success<Unit>(204, null)

        vm.cargarPlanificacionesDocente()
        vm.eliminarPlanificacion(47)

        assertTrue(
            vm.consultaDocente.value.planificaciones.isEmpty()
        )

        assertNotNull(vm.consultaDocente.value.mensaje)
        assertNull(vm.consultaDocente.value.error)
        assertNull(vm.consultaDocente.value.eliminandoId)

        coVerify(exactly = 1) {
            api.eliminar(47)
        }
    }

    test("docente no puede eliminar una actividad asignada") {

        val asignada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaEntregada = false
        )

        coEvery { api.listar() } returns listOf(asignada)

        coEvery { api.obtenerPorId(47) } returns asignada

        vm.cargarPlanificacionesDocente()
        vm.eliminarPlanificacion(47)

        assertNotNull(vm.consultaDocente.value.error)
        assertNull(vm.consultaDocente.value.eliminandoId)

        assertEquals(
            listOf(asignada),
            vm.consultaDocente.value.planificaciones
        )

        coVerify(exactly = 0) {
            api.eliminar(any())
        }
    }

    test("docente no puede eliminar una actividad entregada") {

        val entregada = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = true,
            respuestaEntregada = true,
            respuestaAlumno = "Respuesta del alumno"
        )

        coEvery { api.listar() } returns listOf(entregada)

        coEvery { api.obtenerPorId(47) } returns entregada

        vm.cargarPlanificacionesDocente()
        vm.eliminarPlanificacion(47)

        assertNotNull(vm.consultaDocente.value.error)

        assertEquals(
            entregada,
            vm.consultaDocente.value.planificaciones.first()
        )

        coVerify(exactly = 0) {
            api.eliminar(any())
        }
    }

    test("error de conexion conserva la planificacion") {

        val eliminable = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false,
            respuestaEntregada = false
        )

        coEvery { api.listar() } returns listOf(eliminable)

        coEvery { api.obtenerPorId(47) } returns eliminable

        coEvery {
            api.eliminar(47)
        } throws ConnectException()

        vm.cargarPlanificacionesDocente()
        vm.eliminarPlanificacion(47)

        assertEquals(
            listOf(eliminable),
            vm.consultaDocente.value.planificaciones
        )

        assertNotNull(vm.consultaDocente.value.error)
        assertNull(vm.consultaDocente.value.mensaje)
        assertNull(vm.consultaDocente.value.eliminandoId)
    }

    test("eliminar una planificacion conserva las restantes") {

        val eliminable = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false,
            respuestaEntregada = false
        )

        val protegida = eliminable.copy(
            id = 48,
            asignadoAlumno = true,
            respuestaEntregada = true,
            respuestaAlumno = "Respuesta original"
        )

        coEvery { api.listar() } returnsMany
                listOf(
                    listOf(eliminable, protegida),
                    listOf(protegida)
                )

        coEvery { api.obtenerPorId(47) } returns eliminable

        coEvery { api.eliminar(47) } returns
                Response.success<Unit>(204, null)

        vm.cargarPlanificacionesDocente()
        vm.eliminarPlanificacion(47)

        assertEquals(
            listOf(protegida),
            vm.consultaDocente.value.planificaciones
        )

        coVerify(exactly = 1) {
            api.eliminar(47)
        }

        coVerify(exactly = 0) {
            api.eliminar(48)
        }
    }

    test("dos pulsaciones solo realizan una eliminacion") {

        val eliminable = plan.copy(
            validado = true,
            autorizado = true,
            asignadoAlumno = false,
            respuestaEntregada = false
        )

        val pendiente =
            CompletableDeferred<Response<Unit>>()

        coEvery { api.listar() } returnsMany
                listOf(listOf(eliminable), emptyList())

        coEvery { api.obtenerPorId(47) } returns eliminable

        coEvery {
            api.eliminar(47)
        } coAnswers {
            pendiente.await()
        }

        vm.cargarPlanificacionesDocente()

        vm.eliminarPlanificacion(47)
        vm.eliminarPlanificacion(47)

        assertEquals(
            47L,
            vm.consultaDocente.value.eliminandoId
        )

        pendiente.complete(
            Response.success<Unit>(204, null)
        )

        coVerify(exactly = 1) {
            api.eliminar(47)
        }

        assertNull(vm.consultaDocente.value.eliminandoId)

        assertTrue(
            vm.consultaDocente.value.planificaciones.isEmpty()
        )
    }










})
