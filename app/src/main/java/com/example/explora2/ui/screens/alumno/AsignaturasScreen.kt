package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AsignaturasScreen(
    onLenguajeClick: () -> Unit,
    onMatematicaClick: () -> Unit,
    onCienciasClick: () -> Unit,
    onHistoriaClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Asignaturas (2.° Básico)",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Button(onClick = onLenguajeClick, modifier = Modifier.fillMaxWidth()) {
            Text("Lenguaje y Comunicación")
        }
        Button(onClick = onMatematicaClick, modifier = Modifier.fillMaxWidth()) {
            Text("Matemática")
        }
        Button(onClick = onCienciasClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ciencias Naturales")
        }
        Button(onClick = onHistoriaClick, modifier = Modifier.fillMaxWidth()) {
            Text("Historia, Geografía y Ciencias Sociales")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AsignaturasScreenPreview() {
    AsignaturasScreen({}, {}, {}, {})
}