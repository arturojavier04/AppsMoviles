package com.example.explora2.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen

@Composable
fun BarraNavegacionInferior(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.SelectorPerfil.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Portal") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Asignaturas.route) },
            icon = { Icon(Icons.Default.AutoStories, contentDescription = "Asignaturas") },
            label = { Text("Materias") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.ProgresoAlumno.route) },
            icon = { Icon(Icons.Default.QueryStats, contentDescription = "Progreso") },
            label = { Text("Mi Avance") }
        )
    }
}