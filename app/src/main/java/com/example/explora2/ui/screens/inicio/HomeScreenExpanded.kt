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
fun HomeScreenExpanded(onComenzarClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text("MPAZ PRO - Explora2", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            Text("Sistema integral de aprendizaje y evaluación adaptativa para 2.° Básico.", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onComenzarClick) { Text("Acceder al Sistema") }
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Explora2",
            modifier = Modifier.weight(1f).height(260.dp),
            contentScale = ContentScale.Fit
        )
    }
}