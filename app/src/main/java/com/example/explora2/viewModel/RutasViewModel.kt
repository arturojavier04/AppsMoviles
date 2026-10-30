package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RutasViewModel : ViewModel() {

    private val _flujoNavegacion = MutableSharedFlow<NavigationEvent>()
    val flujoNavegacion: SharedFlow<NavigationEvent> = _flujoNavegacion.asSharedFlow()

    fun irADestino(ruta: String) {
        viewModelScope.launch {
            _flujoNavegacion.emit(NavigationEvent.NavigateTo(ruta))
        }
    }

    fun volverAtras() {
        viewModelScope.launch {
            _flujoNavegacion.emit(NavigationEvent.NavigateBack)
        }
    }
}