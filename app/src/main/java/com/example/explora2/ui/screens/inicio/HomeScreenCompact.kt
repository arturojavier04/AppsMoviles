package com.example.explora2.ui.screens.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explora2.R

@Composable
fun HomeScreenCompact(onComenzarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Bienvenido a Explora2!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Text("Apoyo Didáctico - Escuela Marcela Paz", style = MaterialTheme.typography.bodyMedium)

        Button(onClick = onComenzarClick) {
            Text("Ingresar al Portal")
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Explora2",
            modifier = Modifier.fillMaxWidth().height(120.dp),
            contentScale = ContentScale.Fit
        )
    }
}