package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explora2.repository.ConfiguracionStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionExamenScreen(navController: NavController? = null) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuracionStore = remember { ConfiguracionStore(contexto) }

    // Lee los intentos reales guardados en el almacenamiento local del teléfono
    val intentosGuardados by configuracionStore.intentosRestantesFlow.collectAsState(initial = 2)

    var intentosPermitidos by remember { mutableStateOf(intentosGuardados) }
    var fechaInicio by remember { mutableStateOf("01/11/2026") }
    var fechaFin by remember { mutableStateOf("15/11/2026") }
    var versionSeleccionada by remember { mutableStateOf("v1.2") }
    var mensajeGuardado by remember { mutableStateOf(false) }

    // Sincroniza el valor inicial cuando DataStore entrega los datos guardados
    LaunchedEffect(intentosGuardados) {
        intentosPermitidos = intentosGuardados
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Prueba") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver al panel"
                            )
                        }
                    }
                }
            )
        }
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

            Text(
                text = "Número de Intentos Permitidos: $intentosPermitidos",
                style = MaterialTheme.typography.titleMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    if (intentosPermitidos > 1) {
                        intentosPermitidos--
                        mensajeGuardado = false
                    }
                }) {
                    Text("-", style = MaterialTheme.typography.titleMedium)
                }

                Button(onClick = {
                    if (intentosPermitidos < 5) {
                        intentosPermitidos++
                        mensajeGuardado = false
                    }
                }) {
                    Text("+", style = MaterialTheme.typography.titleMedium)
                }
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

            // BOTÓN QUE AHORA SÍ GUARDA DE VERDAD EN EL ALMACENAMIENTO DEL DISPOSITIVO
            Button(
                onClick = {
                    scope.launch {
                        // Persiste los nuevos intentos en ConfiguracionStore
                        configuracionStore.guardarIntentoYNota(intentosPermitidos, 0)
                        mensajeGuardado = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Guardar Parámetros de Examen")
            }

            if (mensajeGuardado) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✓ Parámetros guardados: $intentosPermitidos intentos registrados para $versionSeleccionada.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "Vista Celular - Configuración Examen", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ConfiguracionExamenScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        ConfiguracionExamenScreen(navController = navController)
    }
}