package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class EntregaEstudiante(val nombre: String, val evaluacion: String, val puntaje: String, val revisado: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionEntregasScreen(navController: NavController) {
    val entregas = listOf(
        EntregaEstudiante("Mateo Silva", "Comprensión Lectura 1", "6/7 puntos", true),
        EntregaEstudiante("Sofía Morales", "Comprensión Lectura 1", "7/7 puntos", true),
        EntregaEstudiante("Lucas González", "Comprensión Lectura 1", "Pendiente", false)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Revisión de Entregas") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(entregas) { entrega ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = entrega.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = entrega.evaluacion, style = MaterialTheme.typography.bodySmall)
                            Text(text = "Estado: ${entrega.puntaje}", style = MaterialTheme.typography.labelMedium)
                        }
                        Button(onClick = { /* Navegar a detalle de calificación */ }) {
                            Text(if (entrega.revisado) "Ver" else "Calificar")
                        }
                    }
                }
            }
        }
    }
}