package com.example.explora2.ui.screens.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen

data class AsignaturaUtp(
    val nombre: String,
    val unidadesCreadas: Int,
    val estadoAprobacion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelUtpScreen(navController: NavController) {
    // Lista simulada de las 4 asignaturas exigidas por el caso MPAZ PRO
    val asignaturas = listOf(
        AsignaturaUtp("Lenguaje y Comunicación", 3, "Unidades Autorizadas"),
        AsignaturaUtp("Matemática", 2, "En Revisión por UTP"),
        AsignaturaUtp("Ciencias Naturales", 2, "Autorizado / Publicado"),
        AsignaturaUtp("Historia, Geografía y Cs. Sociales", 1, "Borrador")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Jefatura UTP - Administrador Pedagógico") }
            )
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
                text = "Gestione, apruebe o modifique las unidades didácticas del nivel de 2.° Básico de la Escuela Marcela Paz.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Listado de las asignaturas institucionales
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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {

                                Button(
                                    onClick = {
                                        navController.navigate(Screen.AutorizacionUnidades.route)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Ver solicitudes de autorización")
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}