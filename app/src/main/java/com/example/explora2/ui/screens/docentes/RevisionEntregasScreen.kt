
package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.model.GestionUiState

data class EntregaEstudiante(
    val nombre: String,
    val evaluacion: String,
    val puntaje: String,
    val revisado: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionEntregasScreen(
    navController: NavController,
    gestionUiState: GestionUiState
) {

    val entregas = listOf(
        EntregaEstudiante(
            "Mateo Silva",
            "Comprensión Lectura 1",
            "6/7 puntos",
            true
        ),
        EntregaEstudiante(
            "Sofía Morales",
            "Comprensión Lectura 1",
            "7/7 puntos",
            true
        ),
        EntregaEstudiante(
            "Lucas González",
            "Comprensión Lectura 1",
            "Pendiente",
            false
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Revisión de Entregas")
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

            item {
                Text(
                    text = "Entrega del Alumno",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        if (gestionUiState.respuestaEntregada) {

                            Text(
                                text = "Actividad: ${gestionUiState.titulo}"
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

                            HorizontalDivider()

                            Text(
                                text = "Respuesta del estudiante:",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = gestionUiState.respuestaAlumno
                            )

                            Text(
                                text = "Estado: Entregado",
                                color = MaterialTheme.colorScheme.primary
                            )

                        } else {

                            Text(
                                text = "Todavía no se ha entregado una respuesta."
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Entregas de ejemplo",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Estos registros son ficticios y se utilizan para demostrar la interfaz."
                )
            }

            items(entregas) { entrega ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = entrega.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = entrega.evaluacion
                        )

                        Text(
                            text = "Resultado: ${entrega.puntaje}"
                        )

                        if (entrega.revisado) {
                            Text(
                                text = "Estado: Revisado"
                            )
                        } else {
                            Text(
                                text = "Estado: Pendiente de revisión"
                            )
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Panel Docente")
                }
            }
        }
    }
}
