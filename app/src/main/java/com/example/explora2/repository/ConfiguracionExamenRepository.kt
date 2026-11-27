package com.example.explora2.repository

import com.example.explora2.data.model.ConfiguracionExamenDto
import com.example.explora2.data.remote.ExploraApiService
import com.example.explora2.data.remote.ExploraRetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class ConfiguracionExamenRepository(
    private val api: ExploraApiService =
        ExploraRetrofitInstance.api
) {

    suspend fun obtener(): ConfiguracionExamenDto? {

        val configuraciones = api.listarConfiguraciones()

        if (configuraciones.size > 1) {
            throw IOException(
                "Existe más de una configuración global"
            )
        }

        return configuraciones.firstOrNull()
    }

    suspend fun guardar(
        datos: ConfiguracionExamenDto
    ): ConfiguracionExamenDto {

        val respuesta = api.guardarConfiguracion(
            datos.copy(id = null)
        )

        if (!respuesta.isSuccessful) {
            throw HttpException(respuesta)
        }

        val guardada = respuesta.body()

        if (respuesta.code() != 200 ||
            guardada == null ||
            guardada.id == null ||
            guardada.id <= 0 ||
            guardada.copy(id = null) != datos.copy(id = null)
        ) {
            throw IOException(
                "El servidor no confirmó la configuración"
            )
        }

        return guardada
    }
}
