package com.example.explora2.ui.screens.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeUtpScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Informe Consolidado (UTP)") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Métricas de Rendimiento - 2.° Básico", style = MaterialTheme.typography.headlineSmall)
            Text("Escuela Escritora Marcela Paz", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OA1: Lectura Fluida", style = MaterialTheme.typography.titleMedium)
                    LinearProgressIndicator(progress = { 0.78f }, modifier = Modifier.fillMaxWidth().height(8.dp))
                    Text("78% de logro a nivel de curso", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OA2: Comprensión Lectora", style = MaterialTheme.typography.titleMedium)
                    LinearProgressIndicator(progress = { 0.64f }, modifier = Modifier.fillMaxWidth().height(8.dp))
                    Text("64% de logro (Requiere refuerzo)", style = MaterialTheme.typography.bodySmall)
                }
            }

            Button(
                onClick = { /* Generar reporte PDF o exportar datos */ },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Exportar Informe Pedagógico")
            }
        }
    }
}