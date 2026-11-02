package com.example.explora2.ui.screens.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    val asignaturas = listOf(
        AsignaturaUtp("Lenguaje y Comunicación", 3, "Unidades Autorizadas"),
        AsignaturaUtp("Matemática", 2, "En Revisión por UTP"),
        AsignaturaUtp("Ciencias Naturales", 2, "Autorizado / Publicado"),
        AsignaturaUtp("Historia, Geografía y Cs. Sociales", 1, "Borrador")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Jefatura UTP") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.SelectorPerfil.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a Selector de Perfil"
                        )
                    }
                }
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
                text = "Gestione y supervise las unidades didácticas del nivel de 2.° Básico de la Escuela Marcela Paz.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.navigate(Screen.AutorizacionUnidades.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Autorizaciones")
                }
                OutlinedButton(
                    onClick = { navController.navigate(Screen.InformeConsolidado.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Consolidado OA")
                }
            }

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
                        }
                    }
                }
            }
        }
    }
}