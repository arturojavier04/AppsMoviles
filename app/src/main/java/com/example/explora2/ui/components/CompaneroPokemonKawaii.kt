package com.example.explora2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.explora2.viewmodel.PokemonViewModel

@Composable
fun CompaneroPokemonKawaii(
    pokemonViewModel: PokemonViewModel,
    modifier: Modifier = Modifier
) {
    val pokemon by pokemonViewModel.pokemonActual.collectAsState()
    val mensaje by pokemonViewModel.mensajeSaludo.collectAsState()

    val nombreCapitalizado = pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "Explorador"
    val imagenUrl = pokemon?.sprites?.other?.officialArtwork?.frontDefault
        ?: pokemon?.sprites?.frontDefault

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(3.dp, Color(0xFF1E1E1E), RoundedCornerShape(24.dp))
            .clickable {
                pokemonViewModel.saludarConNuevoPokemon("¡Hola! ¡Sigue así!")
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.5.dp, Color(0xFF1E1E1E), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (imagenUrl != null) {
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = nombreCapitalizado,
                        modifier = Modifier.size(54.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(text = "⚡", fontSize = 26.sp)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "✨ $nombreCapitalizado te saluda:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = mensaje,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E1E1E)
                )
            }
        }
    }
}