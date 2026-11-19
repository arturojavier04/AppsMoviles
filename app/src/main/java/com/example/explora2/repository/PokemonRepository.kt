package com.example.explora2.repository

import com.example.explora2.data.model.PokemonResponse
import com.example.explora2.data.remote.PokeApiService
import com.example.explora2.data.remote.PokeRetrofitInstance

open class PokemonRepository(
    private val apiService: PokeApiService = PokeRetrofitInstance.api
) {
    open suspend fun obtenerPokemon(id: Int): PokemonResponse {
        return apiService.getPokemon(id)
    }
}