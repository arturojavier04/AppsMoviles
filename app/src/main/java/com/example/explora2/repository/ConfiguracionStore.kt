package com.example.explora2.repositorystore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.almacenPreferencias by preferencesDataStore(name = "explora2_config_prefs")

class ConfiguracionStore(private val context: Context) {

    companion object {
        private val CLAVE_ESTADO_SESION = booleanPreferencesKey("clave_estado_sesion")
    }

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
}