package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgresoEstudianteScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Avance Escolar",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(text = "Unidades completadas de 2.° Básico")

        LinearProgressIndicator(
            progress = { 0.65f },
            modifier = Modifier.fillMaxWidth().height(10.dp)
        )

        Text(
            text = "65% completado",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}