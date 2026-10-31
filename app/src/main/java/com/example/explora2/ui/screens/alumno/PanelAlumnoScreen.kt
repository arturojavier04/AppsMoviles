package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.model.GestionUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelAlumnoScreen(
    navController: NavController,
    gestionUiState: GestionUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Espacio Escolar (2.° Básico)") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.SelectorPerfil.route) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver a Perfiles")
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

            if (gestionUiState.autorizado &&
                gestionUiState.asignadoAlumno) {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¡Tienes una nueva actividad!",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Asignatura: ${gestionUiState.asignatura}"
                        )

                        Text(
                            text = "Unidad: ${gestionUiState.unidad}"
                        )

                        Text(
                            text = "Actividad: ${gestionUiState.titulo}"
                        )

                        Text(
                            text = "Objetivo: ${gestionUiState.objetivo}"
                        )

                        Text(
                            text = gestionUiState.descripcion
                        )
                    }
                }

            } else {
                Text(
                    text = "Todavía no tienes nuevas actividades asignadas."
                )
            }


            // Tarjeta 1: Acceso a las materias del nivel
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

            // Tarjeta 2: Conexión al examen con intentos y audio (Punto 2)
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