
package com.example.explora2.data.remote

import com.example.explora2.data.model.PlanificacionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import com.example.explora2.data.model.ConfiguracionExamenDto
interface ExploraApiService {

    @GET("api/planificaciones")
    suspend fun listar(): List<PlanificacionDto>

    @GET("api/planificaciones/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Long
    ): PlanificacionDto

    @POST("api/planificaciones")
    suspend fun crear(
        @Body datos: PlanificacionDto
    ): Response<PlanificacionDto>

    @PUT("api/planificaciones/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body datos: PlanificacionDto
    ): Response<PlanificacionDto>

    @DELETE("api/planificaciones/{id}")
    suspend fun eliminar(
        @Path("id") id: Long
    ): Response<Unit>


    @GET("api/configuracion-examen")
    suspend fun listarConfiguraciones(): List<ConfiguracionExamenDto>

    @PUT("api/configuracion-examen")
    suspend fun guardarConfiguracion(
        @Body datos: ConfiguracionExamenDto
    ): Response<ConfiguracionExamenDto>

}
