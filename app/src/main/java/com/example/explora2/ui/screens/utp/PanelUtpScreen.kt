package com.example.explora2.ui.screens.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explora2.navigation.Screen

data class AsignaturaUtp(
    val nombre: String,
    var unidadesCreadas: Int,
    var estadoAprobacion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelUtpScreen(navController: NavController? = null) {
    // Lista reactiva que permite agregar nuevas unidades creadas por UTP
    var asignaturas by remember {
        mutableStateOf(
            listOf(
                AsignaturaUtp("Lenguaje y Comunicación", 3, "Unidades Autorizadas"),
                AsignaturaUtp("Matemática", 2, "En Revisión por UTP"),
                AsignaturaUtp("Ciencias Naturales", 2, "Autorizado / Publicado"),
                AsignaturaUtp("Historia, Geografía y Cs. Sociales", 1, "Borrador")
            )
        )
    }

    // Estados para el diálogo de creación
    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var nombreUnidad by remember { mutableStateOf("") }
    var codigoOA by remember { mutableStateOf("") }
    var materiaSeleccionada by remember { mutableStateOf("Lenguaje y Comunicación") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Jefatura UTP") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.navigate(Screen.SelectorPerfil.route) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver a Selector de Perfil"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCrear = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Unidad / OA")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Administración de Asignaturas y Objetivos de Aprendizaje (OA)",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Cree nuevas unidades curriculares o autorice las planificaciones enviadas por docentes.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController?.navigate(Screen.AutorizacionUnidades.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Autorizaciones")
                }
                OutlinedButton(
                    onClick = { navController?.navigate(Screen.InformeConsolidado.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Consolidado OA")
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(asignaturas) { asignatura ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = asignatura.nombre,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(text = "• Unidades activas: ${asignatura.unidadesCreadas}")
                            Text(text = "• Estado: ${asignatura.estadoAprobacion}")
                        }
                    }
                }
            }
        }
    }

    // Diálogo emergente para crear Unidad y OA
    if (mostrarDialogoCrear) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCrear = false },
            title = { Text("Crear Unidad y Objetivo (OA)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = materiaSeleccionada,
                        onValueChange = { materiaSeleccionada = it },
                        label = { Text("Asignatura") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nombreUnidad,
                        onValueChange = { nombreUnidad = it },
                        label = { Text("Título de la Unidad") },
                        placeholder = { Text("Ej: Unidad 3 - Fábula y Poesía") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = codigoOA,
                        onValueChange = { codigoOA = it },
                        label = { Text("Objetivo de Aprendizaje (OA)") },
                        placeholder = { Text("Ej: OA 04 - Lectura expresiva") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombreUnidad.isNotBlank()) {
                            asignaturas = asignaturas.map {
                                if (it.nombre.contains(materiaSeleccionada, ignoreCase = true)) {
                                    it.copy(
                                        unidadesCreadas = it.unidadesCreadas + 1,
                                        estadoAprobacion = "Unidad Creada por UTP ✓"
                                    )
                                } else it
                            }
                            mostrarDialogoCrear = false
                            nombreUnidad = ""
                            codigoOA = ""
                        }
                    }
                ) {
                    Text("Crear y Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCrear = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(
    name = "Vista Previa - Panel UTP",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PanelUtpScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        PanelUtpScreen(navController = navController)
    }
}