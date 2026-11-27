
package com.example.explora2.viewmodel

import com.example.explora2.data.model.ConfiguracionExamenDto
import com.example.explora2.data.remote.ExploraApiService
import com.example.explora2.repository.ConfiguracionExamenRepository
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
import retrofit2.Response
import java.net.ConnectException
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ConfiguracionExamenTest : FunSpec({

    lateinit var api: ExploraApiService
    lateinit var vm: ConfiguracionExamenViewModel

    val configuracion = ConfiguracionExamenDto(
        id = 2L,
        intentosPermitidos = 4,
        fechaInicio = "01/11/2026",
        fechaFin = "31/12/2026",
        version = "v1.2"
    )

    fun cargar(datos: ConfiguracionExamenDto? = null) {
        coEvery {
            api.listarConfiguraciones()
        } returns listOfNotNull(datos)

        vm.cargar()
    }

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        api = mockk()

        vm = ConfiguracionExamenViewModel(
            ConfiguracionExamenRepository(api)
        )
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("recuperar configuracion existente") {

        cargar(configuracion)

        assertEquals(
            configuracion,
            vm.estado.value.borrador
        )

        assertTrue(vm.estado.value.cargado)
        assertFalse(vm.estado.value.cargando)
        assertNull(vm.estado.value.error)
    }

    test("tabla vacia permite crear configuracion") {

        cargar()

        assertTrue(vm.estado.value.cargado)
        assertNull(vm.estado.value.borrador.id)
        assertEquals(
            2,
            vm.estado.value.borrador.intentosPermitidos
        )
    }

    test("no permite guardar antes de cargar") {

        vm.guardar()

        coVerify(exactly = 0) {
            api.guardarConfiguracion(any())
        }
    }

    test("rechaza intentos fuera del rango permitido") {

        cargar(configuracion)

        listOf(0, 6, -1).forEach { intentos ->

            vm.cambiarBorrador(
                configuracion.copy(
                    intentosPermitidos = intentos
                )
            )

            vm.guardar()

            assertNotNull(vm.estado.value.error)
        }

        coVerify(exactly = 0) {
            api.guardarConfiguracion(any())
        }
    }

    test("rechaza fechas inexistentes") {

        cargar(configuracion)

        vm.cambiarBorrador(
            configuracion.copy(
                fechaInicio = "31/02/2026"
            )
        )

        vm.guardar()

        assertNotNull(vm.estado.value.error)

        coVerify(exactly = 0) {
            api.guardarConfiguracion(any())
        }
    }

    test("rechaza fechas con formato incorrecto") {

        cargar(configuracion)

        vm.cambiarBorrador(
            configuracion.copy(
                fechaInicio = "2026-11-01"
            )
        )

        vm.guardar()

        assertNotNull(vm.estado.value.error)

        coVerify(exactly = 0) {
            api.guardarConfiguracion(any())
        }
    }

    test("rechaza cierre anterior al inicio") {

        cargar(configuracion)

        vm.cambiarBorrador(
            configuracion.copy(
                fechaFin = "01/10/2026"
            )
        )

        vm.guardar()

        assertNotNull(vm.estado.value.error)

        coVerify(exactly = 0) {
            api.guardarConfiguracion(any())
        }
    }

    test("rechaza una version vacia") {

        cargar(configuracion)

        vm.cambiarBorrador(
            configuracion.copy(version = "")
        )

        vm.guardar()

        assertNotNull(vm.estado.value.error)

        coVerify(exactly = 0) {
            api.guardarConfiguracion(any())
        }
    }

    test("guarda una nueva configuracion correctamente") {

        cargar()

        val nueva = configuracion.copy(id = null)

        coEvery {
            api.guardarConfiguracion(nueva)
        } returns Response.success(configuracion)

        vm.cambiarBorrador(nueva)
        vm.guardar()

        assertEquals(
            configuracion,
            vm.estado.value.borrador
        )

        assertNotNull(vm.estado.value.mensaje)
        assertNull(vm.estado.value.error)
        assertFalse(vm.estado.value.guardando)

        coVerify(exactly = 1) {
            api.guardarConfiguracion(nueva)
        }
    }

    test("actualizar intentos conserva el identificador") {

        cargar(configuracion)

        val actualizada = configuracion.copy(
            intentosPermitidos = 5
        )

        coEvery {
            api.guardarConfiguracion(
                actualizada.copy(id = null)
            )
        } returns Response.success(actualizada)

        vm.cambiarBorrador(actualizada)
        vm.guardar()

        assertEquals(
            2L,
            vm.estado.value.borrador.id
        )

        assertEquals(
            5,
            vm.estado.value.borrador.intentosPermitidos
        )

        assertNull(vm.estado.value.error)
    }

    test("error de conexion conserva el borrador") {

        cargar(configuracion)

        val borrador = configuracion.copy(
            intentosPermitidos = 3
        )

        coEvery {
            api.guardarConfiguracion(any())
        } throws ConnectException()

        vm.cambiarBorrador(borrador)
        vm.guardar()

        assertEquals(
            borrador,
            vm.estado.value.borrador
        )

        assertNotNull(vm.estado.value.error)
        assertNull(vm.estado.value.mensaje)
        assertFalse(vm.estado.value.guardando)
    }

    test("dos pulsaciones solo realizan un PUT") {

        cargar(configuracion)

        val pendiente =
            CompletableDeferred<Response<ConfiguracionExamenDto>>()

        coEvery {
            api.guardarConfiguracion(any())
        } coAnswers {
            pendiente.await()
        }

        vm.guardar()
        vm.guardar()

        assertTrue(vm.estado.value.guardando)

        pendiente.complete(
            Response.success(configuracion)
        )

        coVerify(exactly = 1) {
            api.guardarConfiguracion(any())
        }

        assertFalse(vm.estado.value.guardando)
        assertNotNull(vm.estado.value.mensaje)
    }

    test("rechaza multiples configuraciones globales") {

        coEvery {
            api.listarConfiguraciones()
        } returns listOf(
            configuracion,
            configuracion.copy(id = 3L)
        )

        vm.cargar()

        assertNotNull(vm.estado.value.error)
        assertFalse(vm.estado.value.cargado)
        assertFalse(vm.estado.value.cargando)
    }

    test("no confirma una configuracion sin identificador") {

        cargar(configuracion)

        coEvery {
            api.guardarConfiguracion(any())
        } returns Response.success(
            configuracion.copy(id = null)
        )

        vm.guardar()

        assertNotNull(vm.estado.value.error)
        assertNull(vm.estado.value.mensaje)
        assertFalse(vm.estado.value.guardando)
    }
})
