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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutorizacionUtpScreen(
    navController: NavController? = null,
    gestionUiState: GestionUiState = GestionUiState(),
    onAprobarClick: () -> Unit = {}
) {
    var solicitudes by remember {
        mutableStateOf(
            listOf(
                SolicitudUnidad(1, "Prof. Andrea Campos", "Unidad 2 - Fabulas", "Lectura y audio de apoyo", false),
                SolicitudUnidad(2, "Prof. Roberto Vidal", "Unidad 1 - Vocabulario", "Guías visuales de palabras", true)
            )
        )
    }

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (gestionUiState.enviadoUtp) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Solicitud enviada por docente",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Asignatura: ${gestionUiState.asignatura}")
                            Text("Unidad: ${gestionUiState.unidad}")
                            Text("Objetivo: ${gestionUiState.objetivo}")
                            Text("Tipo de recurso: ${gestionUiState.tipoRecurso}")
                            Text("Título: ${gestionUiState.titulo}")
                            Text("Descripción: ${gestionUiState.descripcion}")

                            if (gestionUiState.autorizado) {
                                Text("Estado: Autorizado por UTP")
                            } else {
                                Button(onClick = onAprobarClick) {
                                    Text("Autorizar contenido")
                                }
                            }
                        }
                    }
                }
            }

            items(solicitudes) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = item.unidad, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Docente: ${item.docente}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (!item.aprobada) {
                                Button(onClick = {
                                    solicitudes = solicitudes.map { if (it.id == item.id) it.copy(aprobada = true) else it }
                                }) {
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
}

@Preview(
    name = "Vista Previa - Autorización UTP",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
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