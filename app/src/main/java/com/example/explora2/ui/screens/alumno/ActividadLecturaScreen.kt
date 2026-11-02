
package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    errorRespuesta: String?,
    respuestaEntregada: Boolean,
    onRespuestaChange: (String) -> Unit,
    onEnviarClick: () -> Unit,
    onVolverClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Mi actividad",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Instrucciones:",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = instrucciones,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        OutlinedTextField(
            value = respuestaEstudiante,
            onValueChange = onRespuestaChange,
            label = {
                Text("Escribe aqui tu respuesta")
            },
            isError = errorRespuesta != null,
            supportingText = {
                if (errorRespuesta != null) {
                    Text(errorRespuesta)
                }
            },
            enabled = !respuestaEntregada,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onEnviarClick,
            enabled = !respuestaEntregada,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entregar Respuesta")
        }

        if (respuestaEntregada) {
            Text(
                text = "¡Muy bien! Tu respuesta fue entregada.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }

        OutlinedButton(
            onClick = onVolverClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Panel del Alumno")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActividadLecturaScreenPreview() {

    MaterialTheme {
        ActividadLecturaScreen(
            titulo = "El perro y su pelota",
            instrucciones = "Explica que hizo el perro.",
            respuestaEstudiante = "",
            errorRespuesta = null,
            respuestaEntregada = false,
            onRespuestaChange = {},
            onEnviarClick = {},
            onVolverClick = {}
        )
    }
}
