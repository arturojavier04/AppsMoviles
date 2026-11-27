package com.example.explora2.model

import com.example.explora2.data.model.PlanificacionDto

data class ConsultaDocenteUiState(
    val cargando: Boolean = true,
    val planificaciones: List<PlanificacionDto> = emptyList(),
    val asignandoId: Long? = null,
    val error: String? = null
)
