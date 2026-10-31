package com.example.explora2.model

data class GestionUiState(
    val asignatura: String = "",
    val unidad: String = "",
    val objetivo: String = "",
    val tipoRecurso: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val errores: GestionErrores = GestionErrores(),
    val validado: Boolean = false,
    val enviadoUtp: Boolean = false,
    val autorizado: Boolean = false,
    val asignadoAlumno: Boolean = false

)
