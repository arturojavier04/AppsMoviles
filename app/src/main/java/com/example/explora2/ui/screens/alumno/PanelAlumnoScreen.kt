package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explora2.model.GestionUiState
import com.example.explora2.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelAlumnoScreen(
    navController: NavController,
    gestionUiState: GestionUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Espacio Escolar") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.SelectorPerfil.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a Perfiles"
                        )
                    }
                }
            )
        }
    ) { espacioInterior ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterior)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¡Hola, Explorador! Selecciona tu actividad:",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            if (gestionUiState.autorizado && gestionUiState.asignadoAlumno) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¡Tienes una nueva actividad!",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Asignatura: ${gestionUiState.asignatura}")
                        Text(text = "Unidad: ${gestionUiState.unidad}")
                        Text(text = "Actividad: ${gestionUiState.titulo}")
                        Text(text = "Objetivo: ${gestionUiState.objetivo}")
                        Text(text = gestionUiState.descripcion)
                    }
                }
            } else {
                Text(text = "Todavía no tienes nuevas actividades asignadas.")
            }

            // Tarjeta 1: Materias
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📚 Mis Asignaturas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Lenguaje, Matemática, Ciencias Naturales e Historia.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = { navController.navigate(Screen.Asignaturas.route) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.AutoStories, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Asignaturas")
                    }
                }
            }

            // Tarjeta 2: Prueba
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⭐ Evaluación Final de Unidad",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Rinde tu prueba de lectura con apoyo de audio y consulta tus intentos disponibles.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = { navController.navigate(Screen.RendirPrueba.route) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rendir Evaluación de Lectura")
                    }
                }
            }

            // Botón de salida
            OutlinedButton(
                onClick = { navController.navigate(Screen.SelectorPerfil.route) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Cambiar de Perfil")
            }
        }
    }
}

@Preview(
    name = "Vista Celular - Panel Estudiante",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PanelAlumnoScreenPreview() {
    val navController = rememberNavController()
    PanelAlumnoScreen(
        navController = navController,
        gestionUiState = GestionUiState(
            autorizado = true,
            asignadoAlumno = true,
            asignatura = "Lenguaje y Comunicación",
            unidad = "Unidad 1",
            titulo = "Lectura de Fábulas",
            objetivo = "OA 03: Leer y comprender textos breves.",
            descripcion = "Lee atentamente la fábula del león y el ratón."
        )
    )
}