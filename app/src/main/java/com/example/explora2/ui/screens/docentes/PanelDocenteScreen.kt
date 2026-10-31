package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.model.GestionUiState


// Modelo de datos para las unidades y desafíos del docente
data class UnidadDocente(
    val titulo: String,
    val objetivoAprendizaje: String,
    var intentosPermitidos: Int,
    var disponibilidad: String,
    val rendimientoPromedio: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PanelDocenteScreen(
    navController: NavController,
    gestionUiState: GestionUiState,
    onAsignarClick: () -> Unit
) {

    // Estado reactivo para la gestión docente de unidades y parámetros de evaluación
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
                title = { Text("Panel Docente - Seguimiento 2.° Básico") }
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
            item {
                Text(
                    text = "Gestión de Contenidos y Configuración de Pruebas",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Consulte unidades autorizadas por UTP, configure intentos y supervise el rendimiento por Objetivo de Aprendizaje (OA).",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            item {
                if (gestionUiState.autorizado) {

                    Text(
                        text = "Contenido autorizado por UTP",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Título: ${gestionUiState.titulo}"
                    )

                    Text(
                        text = "Asignatura: ${gestionUiState.asignatura}"
                    )

                    Text(
                        text = "Objetivo: ${gestionUiState.objetivo}"
                    )

                    if (gestionUiState.asignadoAlumno) {
                        Text(
                            text = "Estado: Asignado al alumno de prueba"
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


            item {
                Button(
                    onClick = {
                        navController.navigate(Screen.GestionContenido.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Planificar Contenido Pedagógico")
                }
            }


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

                        Divider(modifier = Modifier.padding(vertical = 4.dp))

                        Text(text = "• Disponibilidad: ${unidad.disponibilidad}")
                        Text(text = "• Intentos máximos para la prueba final: ${unidad.intentosPermitidos}")
                        Text(text = "• Rendimiento grupal (OA): ${unidad.rendimientoPromedio}")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    // Incrementar intentos permitidos como prueba de parametrización docente
                                    unidades = unidades.map {
                                        if (it == unidad) it.copy(intentosPermitidos = it.intentosPermitidos + 1) else it
                                    }
                                },
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Aumentar Intento")
                            }

                            OutlinedButton(
                                onClick = { /* Lógica para ver informe detallado por alumno */ },
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Ver Informe OA")
                            }
                        }
                    }
                }
            }
        }
    }
}