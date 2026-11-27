package com.example.explora2.repository

import com.example.explora2.data.model.PlanificacionDto
import com.example.explora2.data.remote.ExploraApiService
import com.example.explora2.data.remote.ExploraRetrofitInstance
import java.io.IOException
import retrofit2.HttpException

class PlanificacionRepository(private val api: ExploraApiService = ExploraRetrofitInstance.api) {
    suspend fun crear(datos: PlanificacionDto): Long {
        val respuesta = api.crear(datos)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
        val id = respuesta.body()?.id
        if (respuesta.code() != 201 || id == null || id <= 0) {
            throw IOException("El servidor no confirmó el identificador creado")
        }
        return id
    }

    suspend fun listarEnviadas(): List<PlanificacionDto> =
        api.listar().filter { it.enviadoUtp && it.id != null }

    suspend fun obtenerPorId(id: Long): PlanificacionDto = api.obtenerPorId(id)
}
