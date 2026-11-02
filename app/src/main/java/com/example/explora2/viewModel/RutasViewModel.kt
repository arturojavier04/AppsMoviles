package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.navigation.EventoNavegacion
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RutasViewModel : ViewModel() {

    private val _flujoNavegacion = MutableSharedFlow<EventoNavegacion>()
    val flujoNavegacion: SharedFlow<EventoNavegacion> = _flujoNavegacion.asSharedFlow()

    fun irADestino(ruta: String) {
        viewModelScope.launch {
            _flujoNavegacion.emit(EventoNavegacion.IrHacia(ruta))
        }
    }

    fun volverAtras() {
        viewModelScope.launch {
            _flujoNavegacion.emit(EventoNavegacion.Retroceder)
        }
    }
}