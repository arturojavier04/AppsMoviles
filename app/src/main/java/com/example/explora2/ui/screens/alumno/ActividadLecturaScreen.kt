package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActividadLecturaScreen(
    titulo: String,
    instrucciones: String,
    respuestaEstudiante: String,
    onRespuestaChange: (String) -> Unit,
    onEnviarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = titulo, style = MaterialTheme.typography.headlineMedium)
        Text(text = instrucciones, style = MaterialTheme.typography.bodyLarge)

        OutlinedTextField(
            value = respuestaEstudiante,
            onValueChange = onRespuestaChange,
            label = { Text("Escribe aquí tu respuesta") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onEnviarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entregar Desafío")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActividadLecturaScreenPreview() {
    ActividadLecturaScreen(
        titulo = "Fábula del León",
        instrucciones = "Lee atentamente y responde la moraleja.",
        respuestaEstudiante = "",
        onRespuestaChange = {},
        onEnviarClick = {}
    )
}