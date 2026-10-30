package com.example.explora2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PreferenciaAppViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStore = EstadoDataStore(application)

    private val _sesionActiva = MutableStateFlow<Boolean?>(null)
    val sesionActiva: StateFlow<Boolean?> = _sesionActiva.asStateFlow()

    private val _alertaVisible = MutableStateFlow(false)
    val alertaVisible: StateFlow<Boolean> = _alertaVisible.asStateFlow()

    init {
        recuperarPreferencia()
    }

    private fun recuperarPreferencia() {
        viewModelScope.launch {
            delay(1200)
            _sesionActiva.value = dataStore.obtenerEstado().first() ?: false
        }
    }

    fun cambiarPreferencia() {
        viewModelScope.launch {
            val valorActual = _sesionActiva.value ?: false
            val valorNuevo = !valorActual

            dataStore.guardarEstado(valorNuevo)
            _sesionActiva.value = valorNuevo
            _alertaVisible.value = true

            delay(1800)
            _alertaVisible.value = false
        }
    }
}