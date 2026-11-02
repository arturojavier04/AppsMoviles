package com.example.explora2.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen

// Sobrecarga conectada al NavController real
@Composable
fun BarraNavegacionInferior(navController: NavController) {
    BarraNavegacionInferiorContent(
        onInicioClick = { navController.navigate(Screen.SelectorPerfil.route) },
        onMateriasClick = { navController.navigate(Screen.Asignaturas.route) },
        onAvanceClick = { navController.navigate(Screen.ProgresoAlumno.route) }
    )
}

// Vista desacoplada para Preview
@Composable
fun BarraNavegacionInferiorContent(
    onInicioClick: () -> Unit = {},
    onMateriasClick: () -> Unit = {},
    onAvanceClick: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = onInicioClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Portal") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onMateriasClick,
            icon = { Icon(Icons.Default.AutoStories, contentDescription = "Asignaturas") },
            label = { Text("Ramos") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onAvanceClick,
            icon = { Icon(Icons.Default.QueryStats, contentDescription = "Progreso") },
            label = { Text("Mi Avance") }
        )
    }
}

@Preview(name = "Barra Navegación Inferior", showBackground = true)
@Composable
fun BarraNavegacionInferiorPreview() {
    MaterialTheme {
        BarraNavegacionInferiorContent()
    }
}