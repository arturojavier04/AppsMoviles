package com.example.explora2.data.remote

import com.example.explora2.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonResponse
}