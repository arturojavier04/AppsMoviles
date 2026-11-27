package com.example.explora2.data.model

data class PlanificacionDto(
    val id: Long? = null,
    val asignatura: String? = null,
    val unidad: String? = null,
    val objetivo: String? = null,
    val tipoRecurso: String? = null,
    val titulo: String? = null,
    val descripcion: String? = null,
    val validado: Boolean = false,
    val enviadoUtp: Boolean = false,
    val autorizado: Boolean = false,
    val asignadoAlumno: Boolean = false,
    val respuestaEntregada: Boolean = false,
    val respuestaAlumno: String? = null
)
