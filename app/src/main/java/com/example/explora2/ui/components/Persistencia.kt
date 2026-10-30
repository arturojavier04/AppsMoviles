package com.example.explora2.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Persistencia(
    valorEstado: Boolean?,
    avisoVisible: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (valorEstado == null) {
            CircularProgressIndicator()
            Text(
                text = "Recuperando preferencias de Explora2...",
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            AnimatedVisibility(
                visible = avisoVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(text = "Configuración sincronizada en DataStore")
            }
        }
    }
}