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

})
