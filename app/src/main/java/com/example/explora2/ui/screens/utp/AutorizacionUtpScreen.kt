package com.example.explora2.ui.screens.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explora2.model.GestionUiState

data class SolicitudUnidad(
    val id: Int,
    val docente: String,
    val unidad: String,
    val descripcion: String,
    val aprobada: Boolean
)

// Repositorio en memoria persistente durante la sesión de la app
private val estadoSolicitudesGlobales = mutableStateListOf(
    SolicitudUnidad(1, "Prof. Andrea Campos", "Unidad 2 - Fábulas", "Lectura y audio de apoyo", false),
    SolicitudUnidad(2, "Prof. Roberto Vidal", "Unidad 1 - Vocabulario", "Guías visuales de palabras", true)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutorizacionUtpScreen(
    navController: NavController? = null,
    gestionUiState: GestionUiState = GestionUiState(),
    onAprobarClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Autorización Curricular (UTP)") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver al Panel UTP"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // TARJETA DE LO QUE CREÓ Y ENVIÓ EL DOCENTE EN TIEMPO REAL
            if (gestionUiState.enviadoUtp) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (gestionUiState.autorizado) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "📌 Solicitud Reciente del Docente",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(text = "Título: ${gestionUiState.titulo}", style = MaterialTheme.typography.titleSmall)
                            Text(text = "Asignatura: ${gestionUiState.asignatura}")
                            Text(text = "Unidad: ${gestionUiState.unidad}")
                            Text(text = "Objetivo: ${gestionUiState.objetivo}")
                            Text(text = "Recurso: ${gestionUiState.tipoRecurso}")
                            Text(text = "Instrucción: ${gestionUiState.descripcion}")

                            Spacer(modifier = Modifier.height(4.dp))

                            if (gestionUiState.autorizado) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "✓ Contenido Autorizado para el Curso",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            } else {
                                Button(
                                    onClick = onAprobarClick,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Autorizar contenido para Docente y Alumno")
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Otras solicitudes curriculares",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // LISTADO DE SOLICITUDES CON ESTADO PERSISTENTE EN LA SESIÓN
            items(estadoSolicitudesGlobales) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = item.unidad, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Docente: ${item.docente}", style = MaterialTheme.typography.bodySmall)
                        Text(text = item.descripcion, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(6.dp))

                        if (!item.aprobada) {
                            Button(
                                onClick = {
                                    val index = estadoSolicitudesGlobales.indexOfFirst { it.id == item.id }
                                    if (index != -1) {
                                        estadoSolicitudesGlobales[index] = item.copy(aprobada = true)
                                    }
                                }
                            ) {
                                Text("Aprobar para Estudiantes")
                            }
                        } else {
                            AssistChip(
                                onClick = { },
                                label = { Text("Unidad Autorizada ✓") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Vista Celular - Autorización UTP", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun AutorizacionUtpScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        AutorizacionUtpScreen(
            navController = navController,
            gestionUiState = GestionUiState(
                enviadoUtp = true,
                autorizado = false,
                asignatura = "Lenguaje y Comunicación",
                unidad = "Unidad 2",
                objetivo = "OA 03: Lectura Fluida",
                tipoRecurso = "Texto con Audio",
                titulo = "Fábula del León y el Ratón",
                descripcion = "Lectura interactiva con apoyo auditivo para segundo básico."
            ),
            onAprobarClick = {}
        )
    }
}