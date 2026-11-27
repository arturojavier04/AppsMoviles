package com.example.explora2.model

import com.example.explora2.data.model.PlanificacionDto

data class RevisionEntregasUiState(
    val cargando: Boolean = true,
    val entregas: List<PlanificacionDto> = emptyList(),
    val error: String? = null
)
