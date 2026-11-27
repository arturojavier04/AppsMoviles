package com.example.explora2.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExploraRetrofitInstance {
    val api: ExploraApiService by lazy {
        val cliente = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .build()
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8089/")
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExploraApiService::class.java)
    }
}
