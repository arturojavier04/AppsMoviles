
package com.example.explora2.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.almacenPreferencias by preferencesDataStore(
    name = "explora2_config_prefs"
)

data class ProgresoEvaluacion(
    val intentosUsados: Int = 0,
    val ultimoPuntaje: Int = 0
)

class ConfiguracionStore(
    private val almacen: DataStore<Preferences>
) {

    constructor(context: Context) :
            this(context.almacenPreferencias)

    companion object {

        private val CLAVE_ESTADO_SESION =
            booleanPreferencesKey("clave_estado_sesion")

        private val CLAVE_INTENTOS_RESTANTES =
            intPreferencesKey("intentos_restantes")

        private val CLAVE_ULTIMO_PUNTAJE =
            intPreferencesKey("ultimo_puntaje")

        private val CLAVE_INTENTOS_USADOS =
            intPreferencesKey("intentos_usados")
    }

    fun obtenerPreferencia(): Flow<Boolean> {
        return almacen.data.map { registro ->
            registro[CLAVE_ESTADO_SESION] ?: false
        }
    }

    suspend fun guardarPreferencia(activo: Boolean) {
        almacen.edit { registro ->
            registro[CLAVE_ESTADO_SESION] = activo
        }
    }

    val intentosRestantesFlow: Flow<Int> =
        almacen.data.map { registro ->
            registro[CLAVE_INTENTOS_RESTANTES] ?: 2
        }

    val ultimoPuntajeFlow: Flow<Int> =
        almacen.data.map { registro ->
            registro[CLAVE_ULTIMO_PUNTAJE] ?: 0
        }

    suspend fun guardarIntentoYNota(
        intentosRestantes: Int,
        puntaje: Int
    ) {
        almacen.edit { registro ->
            registro[CLAVE_INTENTOS_RESTANTES] =
                intentosRestantes

            registro[CLAVE_ULTIMO_PUNTAJE] =
                puntaje
        }
    }

    val progresoEvaluacionFlow: Flow<ProgresoEvaluacion> =
        almacen.data.map { registro ->

            val restantesAnteriores =
                registro[CLAVE_INTENTOS_RESTANTES] ?: 2

            val usados =
                registro[CLAVE_INTENTOS_USADOS]
                    ?: (2 - restantesAnteriores).coerceIn(0, 2)

            ProgresoEvaluacion(
                intentosUsados = usados,
                ultimoPuntaje =
                    registro[CLAVE_ULTIMO_PUNTAJE] ?: 0
            )
        }

    suspend fun registrarIntento(
        maximoPermitido: Int,
        puntaje: Int
    ): Boolean {

        require(maximoPermitido in 1..5)

        var registrado = false

        almacen.edit { registro ->

            val restantesAnteriores =
                registro[CLAVE_INTENTOS_RESTANTES] ?: 2

            val usados =
                registro[CLAVE_INTENTOS_USADOS]
                    ?: (2 - restantesAnteriores).coerceIn(0, 2)

            if (usados < maximoPermitido) {

                val nuevosUsados = usados + 1

                registro[CLAVE_INTENTOS_USADOS] =
                    nuevosUsados

                registro[CLAVE_INTENTOS_RESTANTES] =
                    maximoPermitido - nuevosUsados

                registro[CLAVE_ULTIMO_PUNTAJE] =
                    puntaje

                registrado = true
            }
        }

        return registrado
    }

    suspend fun reiniciarIntentosPrueba() {
        almacen.edit { registro ->

            registro[CLAVE_INTENTOS_RESTANTES] = 2

            registro[CLAVE_ULTIMO_PUNTAJE] = 0

            registro[CLAVE_INTENTOS_USADOS] = 0
        }
    }
}
