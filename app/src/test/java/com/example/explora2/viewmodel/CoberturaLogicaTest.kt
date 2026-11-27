package com.example.explora2.viewmodel

import com.example.explora2.data.model.*
import com.example.explora2.data.remote.ExploraApiService
import com.example.explora2.data.remote.PokeApiService
import com.example.explora2.repository.*
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.HttpException
import java.io.IOException
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CoberturaLogicaTest : FunSpec({

    lateinit var api: ExploraApiService
    lateinit var repositorio: PlanificacionRepository

    val plan = PlanificacionDto(
        id = 10L,
        titulo = "Lectura",
        validado = true,
        enviadoUtp = true,
        autorizado = true,
        asignadoAlumno = true,
        respuestaEntregada = true
    )

    val configuracion = ConfiguracionExamenDto(
        id = 2L,
        intentosPermitidos = 4,
        fechaInicio = "01/11/2026",
        fechaFin = "31/12/2026",
        version = "v1.2"
    )

    val pokemon = PokemonResponse(
        id = 25,
        name = "pikachu",
        sprites = PokemonSprites(
            frontDefault = "imagen.png",
            other = null
        )
    )

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        api = mockk()
        repositorio = PlanificacionRepository(api)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("crear devuelve identificador valido") {
        coEvery {
            api.crear(any())
        } returns Response.success(201, plan)

        assertEquals(
            10L,
            repositorio.crear(plan.copy(id = null))
        )
    }

    test("crear rechaza error HTTP") {
        coEvery {
            api.crear(any())
        } returns Response.error(
            400,
            "Error".toResponseBody()
        )

        val resultado = runCatching {
            repositorio.crear(plan)
        }

        assertTrue(
            resultado.exceptionOrNull() is HttpException
        )
    }

    test("crear rechaza identificador negativo") {
        coEvery {
            api.crear(any())
        } returns Response.success(
            201,
            plan.copy(id = -1L)
        )

        val resultado = runCatching {
            repositorio.crear(plan)
        }

        assertTrue(
            resultado.exceptionOrNull() is IOException
        )
    }

    test("crear rechaza codigo distinto de 201") {
        coEvery {
            api.crear(any())
        } returns Response.success(200, plan)

        assertTrue(
            runCatching {
                repositorio.crear(plan)
            }.exceptionOrNull() is IOException
        )
    }

    test("listar autorizadas filtra estados invalidos") {
        coEvery {
            api.listar()
        } returns listOf(
            plan,
            plan.copy(id = null),
            plan.copy(validado = false),
            plan.copy(enviadoUtp = false),
            plan.copy(autorizado = false)
        )

        assertEquals(
            listOf(plan),
            repositorio.listarAutorizadas()
        )
    }

    test("listar asignadas filtra actividades sin asignar") {
        coEvery {
            api.listar()
        } returns listOf(
            plan,
            plan.copy(asignadoAlumno = false)
        )

        assertEquals(
            listOf(plan),
            repositorio.listarAsignadas()
        )
    }

    test("listar entregadas filtra respuestas pendientes") {
        coEvery {
            api.listar()
        } returns listOf(
            plan,
            plan.copy(respuestaEntregada = false)
        )

        assertEquals(
            listOf(plan),
            repositorio.listarEntregadas()
        )
    }

    test("obtener recupera planificacion por id") {
        coEvery {
            api.obtenerPorId(10L)
        } returns plan

        assertEquals(
            plan,
            repositorio.obtenerPorId(10L)
        )
    }

    test("actualizar rechaza respuesta con otro id") {
        coEvery {
            api.actualizar(any(), any())
        } returns Response.success(
            plan.copy(id = 99L)
        )

        assertTrue(
            runCatching {
                repositorio.actualizar(10L, plan)
            }.exceptionOrNull() is IOException
        )
    }

    test("actualizar rechaza error HTTP") {
        coEvery {
            api.actualizar(any(), any())
        } returns Response.error(
            409,
            "Conflicto".toResponseBody()
        )

        assertTrue(
            runCatching {
                repositorio.actualizar(10L, plan)
            }.exceptionOrNull() is HttpException
        )
    }

    test("eliminar rechaza codigo distinto de 204") {
        coEvery {
            api.eliminar(10L)
        } returns Response.success(Unit)

        assertTrue(
            runCatching {
                repositorio.eliminar(10L)
            }.exceptionOrNull() is IOException
        )
    }

    test("configuracion rechaza error HTTP") {
        val repo = ConfiguracionExamenRepository(api)

        coEvery {
            api.guardarConfiguracion(any())
        } returns Response.error(
            400,
            "Error".toResponseBody()
        )

        assertTrue(
            runCatching {
                repo.guardar(configuracion)
            }.exceptionOrNull() is HttpException
        )
    }

    test("configuracion rechaza id negativo") {
        val repo = ConfiguracionExamenRepository(api)

        coEvery {
            api.guardarConfiguracion(any())
        } returns Response.success(
            configuracion.copy(id = -1L)
        )

        assertTrue(
            runCatching {
                repo.guardar(configuracion)
            }.exceptionOrNull() is IOException
        )
    }

    test("configuracion rechaza datos diferentes") {
        val repo = ConfiguracionExamenRepository(api)

        coEvery {
            api.guardarConfiguracion(any())
        } returns Response.success(
            configuracion.copy(intentosPermitidos = 2)
        )

        assertTrue(
            runCatching {
                repo.guardar(configuracion)
            }.exceptionOrNull() is IOException
        )
    }

    test("usuario rechaza formulario vacio") {
        val vm = UsuarioViewModel()

        assertFalse(vm.validarFormulario())

        assertNotNull(vm.estado.value.errores.nombre)
        assertNotNull(vm.estado.value.errores.correo)
        assertNotNull(vm.estado.value.errores.clave)
        assertNotNull(vm.estado.value.errores.direccion)
    }

    test("usuario acepta formulario valido") {
        val vm = UsuarioViewModel()

        vm.onNombreChange("Usuario Prueba")
        vm.onCorreoChange("prueba@example.com")
        vm.onClaveChange("123456")
        vm.onDireccionChange("Direccion ficticia")
        vm.onAceptarTerminosChange(true)

        assertTrue(vm.validarFormulario())
        assertTrue(vm.estado.value.aceptaTerminos)
    }

    test("usuario limpia errores al corregir campos") {
        val vm = UsuarioViewModel()

        vm.validarFormulario()

        vm.onNombreChange("Usuario Prueba")
        vm.onCorreoChange("prueba@example.com")
        vm.onClaveChange("123456")
        vm.onDireccionChange("Direccion ficticia")

        assertNull(vm.estado.value.errores.nombre)
        assertNull(vm.estado.value.errores.correo)
        assertNull(vm.estado.value.errores.clave)
        assertNull(vm.estado.value.errores.direccion)
    }

    test("pokemon repository consume API por id") {
        val pokeApi = mockk<PokeApiService>()

        coEvery {
            pokeApi.getPokemon(25)
        } returns pokemon

        val repo = PokemonRepository(pokeApi)

        assertEquals(
            pokemon,
            repo.obtenerPokemon(25)
        )

        coVerify(exactly = 1) {
            pokeApi.getPokemon(25)
        }
    }

    test("pokemon viewmodel carga pokemon inicial") {
        val repo = mockk<PokemonRepository>()

        coEvery {
            repo.obtenerPokemon(25)
        } returns pokemon

        val vm = PokemonViewModel(repo)

        assertEquals(
            pokemon,
            vm.pokemonActual.value
        )
    }

    test("pokemon viewmodel carga pokemon especifico") {
        val repo = mockk<PokemonRepository>()

        coEvery {
            repo.obtenerPokemon(any())
        } returns pokemon

        val vm = PokemonViewModel(repo)

        val jirachi = pokemon.copy(
            id = 385,
            name = "jirachi"
        )

        coEvery {
            repo.obtenerPokemon(385)
        } returns jirachi

        vm.cargarPokemonEspecifico(
            385,
            "Buena suerte"
        )

        assertEquals(
            jirachi,
            vm.pokemonActual.value
        )

        assertEquals(
            "Buena suerte",
            vm.mensajeSaludo.value
        )
    }

    test("pokemon conserva resultado ante error de conexion") {
        val repo = mockk<PokemonRepository>()

        coEvery {
            repo.obtenerPokemon(25)
        } returns pokemon

        val vm = PokemonViewModel(repo)

        coEvery {
            repo.obtenerPokemon(385)
        } throws IOException("Sin conexion")

        vm.cargarPokemonEspecifico(385)

        assertEquals(
            pokemon,
            vm.pokemonActual.value
        )
    }

    test("pokemon aleatorio actualiza saludo y resultado") {
        val repo = mockk<PokemonRepository>()

        coEvery {
            repo.obtenerPokemon(any())
        } returns pokemon

        val vm = PokemonViewModel(repo)

        vm.saludarConNuevoPokemon("Sigue aprendiendo")

        assertEquals(
            "Sigue aprendiendo",
            vm.mensajeSaludo.value
        )

        assertEquals(
            pokemon,
            vm.pokemonActual.value
        )

        coVerify(exactly = 2) {
            repo.obtenerPokemon(any())
        }
    }

    test("pokemon aleatorio maneja error de conexion") {
        val repo = mockk<PokemonRepository>()

        coEvery {
            repo.obtenerPokemon(any())
        } throws IOException("Sin conexion")

        val vm = PokemonViewModel(repo)

        vm.saludarConNuevoPokemon("Bien hecho")

        assertNull(vm.pokemonActual.value)

        assertEquals(
            "Bien hecho",
            vm.mensajeSaludo.value
        )
    }
})
