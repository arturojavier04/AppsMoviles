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
})
