package com.example.explora2.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explora2.ui.screen.HomeScreen
import com.example.explora2.ui.screen.RegistroScreen
import com.example.explora2.ui.screen.ResumenScreen
import com.example.explora2.viewmodel.UsuarioViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Registro : Screen("registro_screen")
    data object Resumen : Screen("resumen_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Registro.route) {
            RegistroScreen(navController = navController, viewModel = usuarioViewModel)
        }
        composable(Screen.Resumen.route) {
            ResumenScreen(viewModel = usuarioViewModel)
        }
    }
}