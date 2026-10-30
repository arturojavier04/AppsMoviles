package com.example.explora2.ui.screens.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class SolicitudUnidad(val id: Int, val docente: String, val unidad: String, val descripcion: String, val aprobada: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutorizacionUnidadesScreen(navController: NavController) {
    var solicitudes by remember {
        mutableStateOf(
            listOf(
                SolicitudUnidad(1, "Prof. Andrea Campos", "Unidad 2 - Fabulas", "Lectura y audio de apoyo", false),
                SolicitudUnidad(2, "Prof. Roberto Vidal", "Unidad 1 - Vocabulario", "Guías visuales de palabras", true)
            )
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Autorización Curricular (UTP)") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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