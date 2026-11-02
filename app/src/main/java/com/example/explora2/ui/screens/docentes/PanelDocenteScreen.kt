
package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.model.GestionUiState
import com.example.explora2.navigation.Screen

// Modelo para las unidades del docente
data class UnidadDocente(
    val titulo: String,
    val objetivoAprendizaje: String,
    val intentosPermitidos: Int,
    val disponibilidad: String,
    val rendimientoPromedio: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelDocenteScreen(
    navController: NavController? = null,
    gestionUiState: GestionUiState = GestionUiState(),
    onAsignarClick: () -> Unit = {}
) {

    // Unidades de ejemplo
    var unidades by remember {
        mutableStateOf(
            listOf(
                UnidadDocente(
                    titulo = "Unidad 1: Lectura y Comprensión de Textos Breves",
                    objetivoAprendizaje = "OA 03: Leer y comprender textos breves.",
                    intentosPermitidos = 2,
                    disponibilidad = "01/11/2026 - 15/11/2026",
                    rendimientoPromedio = "85% Logrado"
                ),
                UnidadDocente(
                    titulo = "Unidad 2: Sumas y Restas hasta el 100",
                    objetivoAprendizaje = "OA 08: Resolver adiciones y sustracciones.",
                    intentosPermitidos = 3,
                    disponibilidad = "10/11/2026 - 25/11/2026",
                    rendimientoPromedio = "72% En Proceso"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Panel Docente - 2.° Básico")
                },
                navigationIcon = {
                    if (navController != null) {
                        TextButton(
                            onClick = {
                                navController.navigate(
                                    Screen.SelectorPerfil.route
                                )
                            }
                        ) {
                            Text("Volver")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Encabezado del panel
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Gestión de Contenidos y Configuración de Pruebas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Consulte unidades, configure intentos y supervise el rendimiento por OA.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Accesos a las funcionalidades docentes
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = {
                            navController?.navigate(
                                Screen.GestionContenido.route
                            )
                        },
                        enabled = navController != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Planificar Contenido Pedagógico")
                    }

                    OutlinedButton(
                        onClick = {
                            navController?.navigate(
                                Screen.RevisionEntregas.route
                            )
                        },
                        enabled = navController != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Revisar entregas de estudiantes")
                    }
                }
            }

            // Contenido autorizado por UTP
            item {
                if (gestionUiState.autorizado) {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = "Contenido autorizado por UTP",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Título: ${gestionUiState.titulo}"
                            )

                            Text(
                                text = "Asignatura: ${gestionUiState.asignatura}"
                            )

                            Text(
                                text = "Unidad: ${gestionUiState.unidad}"
                            )

                            Text(
                                text = "Objetivo: ${gestionUiState.objetivo}"
                            )

                            if (gestionUiState.asignadoAlumno) {

                                Text(
                                    text = "Estado: Asignado al alumno de prueba",
                                    color = MaterialTheme.colorScheme.primary
                                )

                            } else {

                                Button(
                                    onClick = onAsignarClick,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Asignar al alumno")
                                }
                            }
                        }
                    }
                }
            }

            // Título de las unidades
            item {
                Text(
                    text = "Unidades del curso",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Las siguientes unidades contienen datos de ejemplo."
                )
            }

            // Listado de unidades
            items(unidades) { unidad ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = unidad.titulo,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = unidad.objetivoAprendizaje,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = "Disponibilidad: ${unidad.disponibilidad}"
                        )

                        Text(
                            text = "Intentos máximos: ${unidad.intentosPermitidos}"
                        )

                        Text(
                            text = "Rendimiento de ejemplo: ${unidad.rendimientoPromedio}"
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Button(
                                onClick = {
                                    unidades = unidades.map {
                                        if (it == unidad) {
                                            it.copy(
                                                intentosPermitidos =
                                                    it.intentosPermitidos + 1
                                            )
                                        } else {
                                            it
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Aumentar Intento")
                            }

                            OutlinedButton(
                                onClick = {
                                    navController?.navigate(
                                        Screen.ConfiguracionExamen.route
                                    )
                                },
                                enabled = navController != null,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Configurar Prueba")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(
    name = "Vista Celular - Panel Docente",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PanelDocenteScreenPreview() {
    MaterialTheme {
        PanelDocenteScreen()
    }
}
