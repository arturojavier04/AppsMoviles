package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionExamenScreen(navController: NavController) {
    var intentosPermitidos by remember { mutableStateOf(2) }
    var fechaInicio by remember { mutableStateOf("01/11/2026") }
    var fechaFin by remember { mutableStateOf("15/11/2026") }
    var versionSeleccionada by remember { mutableStateOf("Versión 1.2") }
    var mensajeGuardado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Configuración de Prueba") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Parámetros de Evaluación", style = MaterialTheme.typography.titleLarge)

            Text("Número de Intentos Permitidos: $intentosPermitidos")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { if (intentosPermitidos > 1) intentosPermitidos-- }) { Text("-") }
                Button(onClick = { if (intentosPermitidos < 5) intentosPermitidos++ }) { Text("+") }
            }

            OutlinedTextField(
                value = fechaInicio,
                onValueChange = { fechaInicio = it },
                label = { Text("Fecha de Disponibilidad Inicial") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaFin,
                onValueChange = { fechaFin = it },
                label = { Text("Fecha de Cierre") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Control de Versiones de la Prueba", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("v1.0", "v1.2", "v2.0").forEach { version ->
                    FilterChip(
                        selected = versionSeleccionada == version,
                        onClick = { versionSeleccionada = version },
                        label = { Text(version) }
                    )
                }
            }

            Button(
                onClick = { mensajeGuardado = true },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Guardar Parámetros de Examen")
            }

            if (mensajeGuardado) {
                Text(
                    text = "Configuración guardada para $versionSeleccionada con $intentosPermitidos intentos.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}