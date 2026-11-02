package com.example.explora2.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.almacenPreferencias by preferencesDataStore(name = "explora2_config_prefs")

class ConfiguracionStore(private val context: Context) {

    companion object {
        // Clave original de la Guía 12 (Sesión / Modo)
        private val CLAVE_ESTADO_SESION = booleanPreferencesKey("clave_estado_sesion")

        // Nuevas claves para el Caso MPAZ PRO (Intentos y Notas)
        private val CLAVE_INTENTOS_RESTANTES = intPreferencesKey("intentos_restantes")
        private val CLAVE_ULTIMO_PUNTAJE = intPreferencesKey("ultimo_puntaje")
    }

    // --- MÉTODOS GUÍA 12 (Sesión / Preferencias) ---
    fun obtenerPreferencia(): Flow<Boolean> {
        return context.almacenPreferencias.data.map { registro ->
            registro[CLAVE_ESTADO_SESION] ?: false
        }
    }

    suspend fun guardarPreferencia(activo: Boolean) {
        context.almacenPreferencias.edit { registro ->
            registro[CLAVE_ESTADO_SESION] = activo
        }
    }

    // --- MÉTODOS CASO MPAZ PRO (Punto 3: Persistencia de Evaluación) ---
    val intentosRestantesFlow: Flow<Int> = context.almacenPreferencias.data.map { registro ->
        registro[CLAVE_INTENTOS_RESTANTES] ?: 2 // Inicia con 2 intentos permitidos
    }

    val ultimoPuntajeFlow: Flow<Int> = context.almacenPreferencias.data.map { registro ->
        registro[CLAVE_ULTIMO_PUNTAJE] ?: 0
    }

    suspend fun guardarIntentoYNota(intentosRestantes: Int, puntaje: Int) {
        context.almacenPreferencias.edit { registro ->
            registro[CLAVE_INTENTOS_RESTANTES] = intentosRestantes
            registro[CLAVE_ULTIMO_PUNTAJE] = puntaje
        }
    }

    suspend fun reiniciarIntentosPrueba() {
        context.almacenPreferencias.edit { registro ->
            registro[CLAVE_INTENTOS_RESTANTES] = 2
            registro[CLAVE_ULTIMO_PUNTAJE] = 0
        }
    }
}