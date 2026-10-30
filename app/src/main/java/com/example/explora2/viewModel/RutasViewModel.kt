package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.navigation.DestinoRuta
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RutasViewModel : ViewModel() {

    private val _flujoNavegacion = MutableSharedFlow<DestinoRuta>()
    val flujoNavegacion: SharedFlow<DestinoRuta> = _flujoNavegacion.asSharedFlow()

    fun irADestino(ruta: String) {
        viewModelScope.launch {
            _flujoNavegacion.emit(DestinoRuta.NavigateTo(ruta))
        }
    }

    fun volverAtras() {
        viewModelScope.launch {
            _flujoNavegacion.emit(DestinoRuta.NavigateBack)
        }
    }
}