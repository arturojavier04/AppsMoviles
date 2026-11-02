package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Sobrecarga para AppNavigation
@Composable
fun ProgresoEstudianteScreen(navController: NavController? = null) {
    ProgresoEstudianteContent(
        onVolverClick = { navController?.popBackStack() }
    )
}

// Vista desacoplada de navController para Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgresoEstudianteContent(onVolverClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Avance Escolar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Panel"
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Progreso del Estudiante",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B)
            )

            Text(
                text = "Unidades completadas del plan lector y matemática",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF455A64)
            )

            LinearProgressIndicator(
                progress = { 0.65f },
                color = Color(0xFF00796B),
                trackColor = Color(0xFFE0F2F1),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

            Text(
                text = "65% completado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedButton(
                onClick = onVolverClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Volver al Menú Principal")
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun ProgresoEstudianteScreenPreview() {
    ProgresoEstudianteContent(onVolverClick = {})
}