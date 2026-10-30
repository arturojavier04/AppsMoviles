package com.example.explora2.navigation

sealed interface EventoNavegacion {
    data class IrHacia(val rutaDestino: String) : EventoNavegacion
    data object Retroceder : EventoNavegacion
}