package com.example.explora2.model

data class GestionUiState(
    val asignatura: String = "",
    val unidad: String = "",
    val objetivo: String = "",
    val tipoRecurso: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val errores: GestionErrores = GestionErrores()
)