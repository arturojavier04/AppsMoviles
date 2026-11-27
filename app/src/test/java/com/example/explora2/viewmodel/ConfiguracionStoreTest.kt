package com.example.explora2.viewmodel

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.explora2.repository.ConfiguracionStore
import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.nio.file.Files
import org.junit.Assert.*

class ConfiguracionStoreTest : FunSpec({

    lateinit var store: ConfiguracionStore
    lateinit var scope: CoroutineScope
    lateinit var carpeta: java.io.File

    beforeTest {
        carpeta = Files.createTempDirectory(
            "explora2-test"
        ).toFile()

        scope = CoroutineScope(
            Dispatchers.IO + SupervisorJob()
        )

        val datastore = PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = {
                carpeta.resolve("test.preferences_pb")
            }
        )

        store = ConfiguracionStore(datastore)
    }

    afterTest {
        scope.coroutineContext[Job]?.cancelAndJoin()
        carpeta.deleteRecursively()
    }

    test("preferencia inicial desactivada") {
        assertFalse(
            store.obtenerPreferencia().first()
        )
    }

    test("guardar y recuperar preferencia") {
        store.guardarPreferencia(true)

        assertTrue(
            store.obtenerPreferencia().first()
        )

        store.guardarPreferencia(false)

        assertFalse(
            store.obtenerPreferencia().first()
        )
    }

    test("progreso inicial sin intentos utilizados") {
        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(0, progreso.intentosUsados)
        assertEquals(0, progreso.ultimoPuntaje)

        assertEquals(
            2,
            store.intentosRestantesFlow.first()
        )

        assertEquals(
            0,
            store.ultimoPuntajeFlow.first()
        )
    }

    test("registrar intento guarda nota y progreso") {
        assertTrue(
            store.registrarIntento(4, 7)
        )

        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(1, progreso.intentosUsados)
        assertEquals(7, progreso.ultimoPuntaje)

        assertEquals(
            3,
            store.intentosRestantesFlow.first()
        )
    }

    test("impide superar el limite de intentos") {
        assertTrue(
            store.registrarIntento(1, 7)
        )

        assertFalse(
            store.registrarIntento(1, 2)
        )

        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(1, progreso.intentosUsados)
        assertEquals(7, progreso.ultimoPuntaje)
    }

    test("aumentar limite permite nuevos intentos") {
        store.registrarIntento(1, 7)

        assertTrue(
            store.registrarIntento(4, 2)
        )

        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(2, progreso.intentosUsados)
        assertEquals(2, progreso.ultimoPuntaje)

        assertEquals(
            2,
            store.intentosRestantesFlow.first()
        )
    }

    test("reducir limite conserva nota y progreso") {
        store.registrarIntento(4, 2)
        store.registrarIntento(4, 7)

        assertFalse(
            store.registrarIntento(1, 2)
        )

        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(2, progreso.intentosUsados)
        assertEquals(7, progreso.ultimoPuntaje)
    }

    test("rechaza limites invalidos") {
        for (limite in listOf(0, -1, 6)) {
            val resultado = runCatching {
                store.registrarIntento(limite, 7)
            }

            assertTrue(
                resultado.exceptionOrNull()
                        is IllegalArgumentException
            )
        }
    }

    test("recupera progreso del sistema anterior") {
        store.guardarIntentoYNota(1, 7)

        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(1, progreso.intentosUsados)
        assertEquals(7, progreso.ultimoPuntaje)
    }

    test("reiniciar restablece el progreso") {
        store.registrarIntento(4, 7)

        store.reiniciarIntentosPrueba()

        val progreso =
            store.progresoEvaluacionFlow.first()

        assertEquals(0, progreso.intentosUsados)
        assertEquals(0, progreso.ultimoPuntaje)

        assertEquals(
            2,
            store.intentosRestantesFlow.first()
        )
    }
})
