package com.example.explora2.model

import com.example.explora2.data.model.PlanificacionDto

data class ConsultaUtpUiState(
    val cargando: Boolean = true,
    val solicitudes: List<PlanificacionDto> = emptyList(),
    val detalle: PlanificacionDto? = null,
    val error: String? = null,
    val autorizando: Boolean = false
)
