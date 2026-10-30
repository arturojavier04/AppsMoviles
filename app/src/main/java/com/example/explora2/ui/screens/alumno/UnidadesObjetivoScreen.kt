package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ObjetivoItem(val id: String, val unidad: String, val titulo: String, val descripcion: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnidadesObjetivoScreen(navController: NavController) {
    val listaObjetivos = listOf(
        ObjetivoItem("OA1", "Unidad 1", "Lectura Fluida", "Leer en voz alta palabras de uso frecuente."),
        ObjetivoItem("OA2", "Unidad 1", "Comprensión Lectora", "Comprender textos breves aplicando estrategias."),
        ObjetivoItem("OA3", "Unidad 2", "Escritura Guiada", "Escribir oraciones completas con mayúscula y punto.")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Unidades y Objetivos (2.° Básico)") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaObjetivos) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "${item.unidad} - ${item.id}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.titulo, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.descripcion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}