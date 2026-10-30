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
fun HomeScreenMedium(onComenzarClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("¡Bienvenido a Explora2!", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Plataforma interactiva para segundo básico.", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = onComenzarClick) { Text("Comenzar Experiencia") }
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Explora2",
                modifier = Modifier.weight(1f).height(180.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}