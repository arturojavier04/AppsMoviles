package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadLecturaScreen(
    titulo: String,
    instrucciones: String,
    respuestaEstudiante: String,
    onRespuestaChange: (String) -> Unit,
    onEnviarClick: () -> Unit,
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Desafío de Lectura") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver a Asignaturas"
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
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Instrucciones:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = instrucciones,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            OutlinedTextField(
                value = respuestaEstudiante,
                onValueChange = onRespuestaChange,
                label = { Text("Escribe aquí tu respuesta") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = onEnviarClick,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Entregar Desafío")
            }
        }
    }
}

@Preview(
    name = "Vista Celular - Desafío Lectura",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ActividadLecturaScreenPreview() {
    val navController = rememberNavController()
    ActividadLecturaScreen(
        titulo = "Fábula: El León y el Ratón",
        instrucciones = "Lee atentamente el texto y responde cuál es la moraleja que nos enseña la historia.",
        respuestaEstudiante = "",
        onRespuestaChange = {},
        onEnviarClick = {},
        navController = navController
    )
}