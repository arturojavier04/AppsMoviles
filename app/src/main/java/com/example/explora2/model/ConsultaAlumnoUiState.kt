package com.example.explora2.model

import com.example.explora2.data.model.PlanificacionDto

data class ConsultaAlumnoUiState(
    val cargando: Boolean = true,
    val actividades: List<PlanificacionDto> = emptyList(),
    val detalle: PlanificacionDto? = null,
    val cargandoDetalle: Boolean = false,
    val respuesta: String = "",
    val enviando: Boolean = false,
    val error: String? = null,
    val errorDetalle: String? = null,
    val errorRespuesta: String? = null
)
