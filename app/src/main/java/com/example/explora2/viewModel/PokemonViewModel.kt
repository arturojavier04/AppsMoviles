package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.data.model.PokemonResponse
import com.example.explora2.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class PokemonViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val pokemonesAmigables = listOf(1, 4, 7, 25, 35, 39, 52, 54, 133, 143, 151, 175)

    private val _pokemonActual = MutableStateFlow<PokemonResponse?>(null)
    open val pokemonActual: StateFlow<PokemonResponse?> = _pokemonActual

    private val _mensajeSaludo = MutableStateFlow("¡Hola! ¡Vamos a aprender juntos!")
    val mensajeSaludo: StateFlow<String> = _mensajeSaludo

    init {
        // Por defecto arranca con Pikachu (ID 25) o el saludo inicial
        cargarPokemonEspecifico(25, "¡Pika-hola! ¡Soy tu compañero de estudio!")
    }

    // Carga un Pokémon específico por su ID (Ej: Pikachu = 25, Charmander = 4, etc.)
    fun cargarPokemonEspecifico(idPokemon: Int, mensaje: String = "¡Hola! ¡Listo para aprender!") {
        _mensajeSaludo.value = mensaje
        viewModelScope.launch {
            try {
                val resultado = repository.obtenerPokemon(idPokemon)
                _pokemonActual.value = resultado
            } catch (e: Exception) {
                println("Error al obtener Pokémon específico: ${e.localizedMessage}")
            }
        }
    }

    // Carga un Pokémon aleatorio de la lista de amigables
    fun saludarConNuevoPokemon(mensaje: String = "¡Qué bien lo estás haciendo!") {
        _mensajeSaludo.value = mensaje
        viewModelScope.launch {
            try {
                val idAleatorio = pokemonesAmigables.random()
                val resultado = repository.obtenerPokemon(idAleatorio)
                _pokemonActual.value = resultado
            } catch (e: Exception) {
                println("Error al obtener Pokémon aleatorio: ${e.localizedMessage}")
            }
        }
    }
}