package com.example.explora2.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Imports de Inicio
import com.example.explora2.ui.screens.inicio.HomeScreen

// Imports de Perfiles y Registro
import com.example.explora2.ui.screens.perfiles.PerfilesScreen
import com.example.explora2.ui.screens.perfiles.RegistroAccesoScreen
import com.example.explora2.ui.screens.perfiles.FichaRegistroScreen
import com.example.explora2.ui.screens.perfiles.AvatarPersonalizacionScreen

// Imports de Docentes
import com.example.explora2.ui.screens.docentes.PanelDocenteScreen
import com.example.explora2.ui.screens.docentes.FormularioGestionScreen
import com.example.explora2.ui.screens.docentes.ConfiguracionExamenScreen
import com.example.explora2.ui.screens.docentes.RevisionEntregasScreen

// Imports de Alumno (2.° Básico)
import com.example.explora2.ui.screens.alumno.PanelAlumnoScreen
import com.example.explora2.ui.screens.alumno.AsignaturasScreen
import com.example.explora2.ui.screens.alumno.UnidadesObjetivoScreen
import com.example.explora2.ui.screens.alumno.ProgresoEstudianteScreen
import com.example.explora2.ui.screens.alumno.RendirPruebaScreen


// Imports de UTP actualizados
import com.example.explora2.ui.screens.utp.PanelUtpScreen
import com.example.explora2.ui.screens.utp.AutorizacionUtpScreen
import com.example.explora2.ui.screens.utp.InformeUtpScreen

// ViewModels del proyecto
import com.example.explora2.viewmodel.UsuarioViewModel
import com.example.explora2.viewmodel.PersonalizacionViewModel

sealed class Screen(val route: String) {
    // Inicio y Roles
    data object Home : Screen("home_screen")
    data object SelectorPerfil : Screen("selector_perfil")

    // Perfiles y Acceso
    data object RegistroAcceso : Screen("registro_acceso")
    data object ResumenRegistro : Screen("resumen_registro")
    data object MiAvatar : Screen("mi_avatar")

    // Alumno
    data object PanelEstudiante : Screen("panel_estudiante")
    data object Asignaturas : Screen("asignaturas_alumno")
    data object UnidadesObjetivos : Screen("unidades_alumno")
    data object ProgresoAlumno : Screen("progreso_alumno")
    data object RendirPrueba : Screen("rendir_prueba")

    // Docente
    data object PanelDocente : Screen("panel_docente")
    data object GestionContenido : Screen("gestion_contenido")
    data object ConfiguracionExamen : Screen("config_examen")
    data object RevisionEntregas : Screen("revision_entregas")

    // UTP
    data object PanelUtp : Screen("panel_utp")
    data object AutorizacionUnidades : Screen("autorizacion_unidades")
    data object InformeConsolidado : Screen("informe_consolidado")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val personalizacionViewModel: PersonalizacionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SelectorPerfil.route
    ) {
        // --- SELECCIÓN Y REGISTRO ---
        composable(Screen.SelectorPerfil.route) {
            PerfilesScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.RegistroAcceso.route) {
            RegistroAccesoScreen(navController = navController, viewModel = usuarioViewModel)
        }
        composable(Screen.ResumenRegistro.route) {
            FichaRegistroScreen(navController = navController, viewModel = usuarioViewModel)
        }
        composable(Screen.MiAvatar.route) {
            AvatarPersonalizacionScreen(viewModel = personalizacionViewModel)
        }

        // --- ESTUDIANTE (2.° BÁSICO) ---
        composable(Screen.PanelEstudiante.route) {
            PanelAlumnoScreen(navController = navController)
        }
        composable(Screen.Asignaturas.route) {
            AsignaturasScreen(navController = navController)
        }
        composable(Screen.UnidadesObjetivos.route) {
            UnidadesObjetivoScreen(navController = navController)
        }
        composable(Screen.ProgresoAlumno.route) {
            ProgresoEstudianteScreen()
        }
        composable(Screen.RendirPrueba.route) {
            RendirPruebaScreen(navController = navController)
        }

        // --- DOCENTE ---
        composable(Screen.PanelDocente.route) {
            PanelDocenteScreen(navController = navController)
        }
        composable(Screen.ConfiguracionExamen.route) {
            ConfiguracionExamenScreen(navController = navController)
        }
        composable(Screen.RevisionEntregas.route) {
            RevisionEntregasScreen(navController = navController)
        }

        // --- UTP ---
        composable(Screen.PanelUtp.route) {
            PanelUtpScreen(navController = navController)
        }
        composable(Screen.AutorizacionUnidades.route) {
            AutorizacionUtpScreen(navController = navController)
        }
        composable(Screen.InformeConsolidado.route) {
            InformeUtpScreen(navController = navController)
        }
    }
}