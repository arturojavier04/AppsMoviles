package com.example.explora2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelMaestroScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Docente - Seguimiento 2.° Básico") }
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
            Text(text = "Gestión de Asignaturas y OA", style = MaterialTheme.typography.titleLarge)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Unidad 1: Matemática y Lectura", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• Estado de pruebas finales: Habilitadas")
                    Text(text = "• Intentos permitidos configurados: 2")
                    Text(text = "• Alumnos evaluados ficticios: 25 / 27")
                }
            }

            Button(
                onClick = { /* Lógica para ver informe consolidado */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar Rendimiento por Objetivo de Aprendizaje")
            }
        }
    }
}