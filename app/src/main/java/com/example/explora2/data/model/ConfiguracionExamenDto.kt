package com.example.explora2.data.model

data class ConfiguracionExamenDto(
    val id: Long? = null,
    val intentosPermitidos: Int = 2,
    val fechaInicio: String? = "",
    val fechaFin: String? = "",
    val version: String? = "v1.2"
)
