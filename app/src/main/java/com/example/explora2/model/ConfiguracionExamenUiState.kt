package com.example.explora2.model

import com.example.explora2.data.model.ConfiguracionExamenDto

data class ConfiguracionExamenUiState(
    val borrador: ConfiguracionExamenDto =
        ConfiguracionExamenDto(),
    val cargando: Boolean = true,
    val cargado: Boolean = false,
    val guardando: Boolean = false,
    val error: String? = null,
    val mensaje: String? = null
)
