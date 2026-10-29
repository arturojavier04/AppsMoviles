package com.example.explora2.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explora2.ui.screens.PerfilesScreen
import com.example.explora2.ui.screens.PanelDocenteScreen
import com.example.explora2.ui.screens.PanelUtpScreen

// (Aquí importaremos las demás pantallas de estudiante y UTP cuando las creemos)
import com.example.explora2.viewmodel.UsuarioViewModel

sealed class Screen(val route: String) {
    data object SelectorPerfil : Screen("selector_perfil")
    data object PanelDocente : Screen("panel_docente")
    data object PanelEstudiante : Screen("panel_estudiante")
    data object PanelUtp : Screen("panel_utp")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SelectorPerfil.route // La app ahora inicia eligiendo el perfil
    ) {
        composable(Screen.SelectorPerfil.route) {
            PerfilesScreen(navController = navController)
        }
        composable(Screen.PanelDocente.route) {
            PanelDocenteScreen(navController = navController)
        }
        composable(Screen.PanelEstudiante.route) {
            // Aquí pondremos la pantalla del estudiante de 2.° básico más adelante
        }
        composable(Screen.PanelUtp.route) {
            // Aquí pondremos la pantalla de Jefatura UTP más adelante
        }

        composable(Screen.PanelUtp.route) {
            PanelUtpScreen(navController = navController)
        }
    }
}