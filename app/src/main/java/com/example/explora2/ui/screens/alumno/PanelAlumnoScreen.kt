
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
import com.example.explora2.model.GestionUiState
import com.example.explora2.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelAlumnoScreen(
    navController: NavController,
    gestionUiState: GestionUiState
) {

    val tieneActividad =
        gestionUiState.autorizado &&
                gestionUiState.asignadoAlumno

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mi Espacio Escolar (2.° Básico)")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                Screen.SelectorPerfil.route
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver a perfiles"
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "¡Hola, Explorador!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Selecciona una actividad para comenzar.",
                style = MaterialTheme.typography.bodyMedium
            )

            // ACTIVIDAD ASIGNADA POR EL DOCENTE

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Actividad asignada",
                        style = MaterialTheme.typography.titleLarge,
                        color =
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    if (tieneActividad) {

                        Text(
                            text = "¡Tienes una nueva actividad!",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Título: ${gestionUiState.titulo}"
                        )

                        Text(
                            text =
                                "Asignatura: ${gestionUiState.asignatura}"
                        )

                        Text(
                            text = "Unidad: ${gestionUiState.unidad}"
                        )

                        Text(
                            text = "Objetivo: ${gestionUiState.objetivo}"
                        )

                        Text(
                            text = "Instrucciones:"
                        )

                        Text(
                            text = gestionUiState.descripcion
                        )

                        HorizontalDivider()

                        if (gestionUiState.respuestaEntregada) {

                            Text(
                                text = "Estado: Actividad entregada",
                                color = MaterialTheme.colorScheme.primary
                            )

                        } else {

                            Text(
                                text = "Estado: Pendiente de entrega"
                            )
                        }

                    } else {

                        Text(
                            text = "Todavía no tienes actividades asignadas."
                        )

                        Text(
                            text = "Tu docente debe asignarte un contenido autorizado por UTP."
                        )
                    }

                    // BOTON PARA ABRIR LA ACTIVIDAD

                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.ActividadAsignada.route
                            )
                        },
                        enabled = tieneActividad,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Abrir actividad")
                    }
                }
            }

            // MIS ASIGNATURAS

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Mis Asignaturas",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text =
                            "Lenguaje, Matemática, Ciencias Naturales e Historia."
                    )

                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.Asignaturas.route
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Ver Asignaturas")
                    }
                }
            }

            // EVALUACION FINAL

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.secondaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Evaluación Final de Unidad",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text =
                            "Rinde tu prueba de lectura y consulta tus intentos disponibles."
                    )

                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.RendirPrueba.route
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.AssignmentTurnedIn,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Rendir Evaluación de Lectura")
                    }
                }
            }

            // CAMBIAR DE PERFIL

            OutlinedButton(
                onClick = {
                    navController.navigate(
                        Screen.SelectorPerfil.route
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar de Perfil")
            }
        }
    }
}
