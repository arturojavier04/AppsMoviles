package com.example.explora2.data.remote

import com.example.explora2.data.model.PlanificacionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExploraApiService {
    @GET("api/planificaciones")
    suspend fun listar(): List<PlanificacionDto>

    @GET("api/planificaciones/{id}")
    suspend fun obtenerPorId(@Path("id") id: Long): PlanificacionDto

    @POST("api/planificaciones")
    suspend fun crear(@Body datos: PlanificacionDto): Response<PlanificacionDto>
}
