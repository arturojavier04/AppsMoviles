package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignaturasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Asignaturas (2.° Básico)") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.PanelEstudiante.route) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver al panel")
                    }
                }
            )
        }
    ) { espacioInterior ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterior)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Elige una materia para ver sus unidades:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 1. Lenguaje y Comunicación (Navega a las Unidades y Objetivos de Aprendizaje)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Lenguaje y Comunicación",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Lectura fluida, fábulas y comprensión de textos.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = { navController.navigate(Screen.UnidadesObjetivos.route) },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Icon(Icons.Default.AutoStories, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Explorar Unidades (OA)")
                    }
                }
            }

            // 2. Matemática
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Matemática",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Números, adiciones y sustracciones hasta el 100.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { navController.navigate(Screen.UnidadesObjetivos.route) },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Contenidos")
                    }
                }
            }

            // 3. Ciencias Naturales
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ciencias Naturales",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "El cuerpo humano, seres vivos y su hábitat.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { navController.navigate(Screen.UnidadesObjetivos.route) },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Icon(Icons.Default.Science, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Contenidos")
                    }
                }
            }

            // 4. Historia, Geografía y Ciencias Sociales
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Historia, Geografía y Cs. Sociales",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Nuestra comunidad, pueblos originarios y paisajes de Chile.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { navController.navigate(Screen.UnidadesObjetivos.route) },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Icon(Icons.Default.Public, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Contenidos")
                    }
                }
            }
        }
    }
}