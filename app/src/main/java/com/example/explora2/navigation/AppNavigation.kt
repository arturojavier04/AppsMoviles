
package com.example.explora2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Inicio
import com.example.explora2.ui.screens.inicio.HomeScreen

// Perfiles
import com.example.explora2.ui.screens.perfiles.PerfilesScreen
import com.example.explora2.ui.screens.perfiles.RegistroAccesoScreen
import com.example.explora2.ui.screens.perfiles.FichaRegistroScreen
import com.example.explora2.ui.screens.perfiles.AvatarPersonalizacionScreen

// Docentes
import com.example.explora2.ui.screens.docentes.PanelDocenteScreen
import com.example.explora2.ui.screens.docentes.FormularioGestionScreen
import com.example.explora2.ui.screens.docentes.ConfiguracionExamenScreen
import com.example.explora2.ui.screens.docentes.RevisionEntregasScreen

// Alumno
import com.example.explora2.ui.screens.alumno.PanelAlumnoScreen
import com.example.explora2.ui.screens.alumno.AsignaturasScreen
import com.example.explora2.ui.screens.alumno.UnidadesObjetivoScreen
import com.example.explora2.ui.screens.alumno.ProgresoEstudianteScreen
import com.example.explora2.ui.screens.alumno.RendirPruebaScreen
import com.example.explora2.ui.screens.alumno.ActividadLecturaScreen

// UTP
import com.example.explora2.ui.screens.utp.PanelUtpScreen
import com.example.explora2.ui.screens.utp.AutorizacionUtpScreen
import com.example.explora2.ui.screens.utp.InformeUtpScreen

// ViewModels
import com.example.explora2.viewmodel.UsuarioViewModel
import com.example.explora2.viewmodel.PersonalizacionViewModel
import com.example.explora2.viewmodel.GestionViewModel

sealed class Screen(val route: String) {

    // Inicio y perfiles
    data object Home : Screen("home_screen")
    data object SelectorPerfil : Screen("selector_perfil")

    // Registro y personalizacion
    data object RegistroAcceso : Screen("registro_acceso")
    data object ResumenRegistro : Screen("resumen_registro")
    data object MiAvatar : Screen("mi_avatar")

    // Alumno
    data object PanelEstudiante : Screen("panel_estudiante")
    data object Asignaturas : Screen("asignaturas_alumno")
    data object UnidadesObjetivos : Screen("unidades_alumno")
    data object ProgresoAlumno : Screen("progreso_alumno")
    data object RendirPrueba : Screen("rendir_prueba")
    data object ActividadAsignada : Screen("actividad_asignada")

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

    // ViewModels compartidos
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val personalizacionViewModel: PersonalizacionViewModel = viewModel()
    val gestionViewModel: GestionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SelectorPerfil.route
    ) {

        // SELECCION Y REGISTRO

        composable(Screen.SelectorPerfil.route) {
            PerfilesScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.RegistroAcceso.route) {
            RegistroAccesoScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        composable(Screen.ResumenRegistro.route) {
            FichaRegistroScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        composable(Screen.MiAvatar.route) {
            AvatarPersonalizacionScreen(
                viewModel = personalizacionViewModel
            )
        }

        // ALUMNO

        composable(Screen.PanelEstudiante.route) {

            val estado by gestionViewModel.estado.collectAsState()

            PanelAlumnoScreen(
                navController = navController,
                gestionUiState = estado
            )
        }

        composable(Screen.Asignaturas.route) {
            AsignaturasScreen(navController = navController)
        }

        composable(Screen.UnidadesObjetivos.route) {
            UnidadesObjetivoScreen(navController = navController)
        }

        composable(Screen.ProgresoAlumno.route) {
            ProgresoEstudianteScreen(
                navController = navController
            )
        }

        composable(Screen.RendirPrueba.route) {
            RendirPruebaScreen(navController = navController)
        }

        // Actividad asignada por el docente
        composable(Screen.ActividadAsignada.route) {

            val estado by gestionViewModel.estado.collectAsState()

            if (estado.autorizado && estado.asignadoAlumno) {

                ActividadLecturaScreen(
                    titulo = estado.titulo,
                    instrucciones = estado.descripcion,
                    respuestaEstudiante = estado.respuestaAlumno,
                    errorRespuesta = estado.errorRespuesta,
                    respuestaEntregada = estado.respuestaEntregada,
                    onRespuestaChange =
                        gestionViewModel::onRespuestaAlumnoChange,
                    onEnviarClick =
                        gestionViewModel::entregarRespuesta,
                    onVolverClick = {
                        navController.popBackStack()
                    }
                )

            } else {

                Text("No tienes una actividad asignada.")
            }
        }

        // DOCENTE

        composable(Screen.PanelDocente.route) {

            val estado by gestionViewModel.estado.collectAsState()

            PanelDocenteScreen(
                navController = navController,
                gestionUiState = estado,
                onAsignarClick = gestionViewModel::asignarAlAlumno
            )
        }

        composable(Screen.ConfiguracionExamen.route) {
            ConfiguracionExamenScreen(
                navController = navController
            )
        }

        composable(Screen.RevisionEntregas.route) {

            val estado by gestionViewModel.estado.collectAsState()

            RevisionEntregasScreen(
                navController = navController,
                gestionUiState = estado
            )
        }

        composable(Screen.GestionContenido.route) {

            val estado by gestionViewModel.estado.collectAsState()

            FormularioGestionScreen(
                uiState = estado,
                errores = estado.errores,
                onAsignaturaChange =
                    gestionViewModel::onAsignaturaChange,
                onUnidadChange =
                    gestionViewModel::onUnidadChange,
                onObjetivoChange =
                    gestionViewModel::onObjetivoChange,
                onTipoRecursoChange =
                    gestionViewModel::onTipoRecursoChange,
                onTituloChange =
                    gestionViewModel::onTituloChange,
                onDescripcionChange =
                    gestionViewModel::onDescripcionChange,
                onEnviarClick =
                    gestionViewModel::enviarAUtp,
                onGuardarClick = {
                    gestionViewModel.validarFormulario()
                }
            )
        }

        // UTP

        composable(Screen.PanelUtp.route) {
            PanelUtpScreen(navController = navController)
        }

        composable(Screen.AutorizacionUnidades.route) {

            val estado by gestionViewModel.estado.collectAsState()

            AutorizacionUtpScreen(
                navController = navController,
                gestionUiState = estado,
                onAprobarClick = gestionViewModel::aprobarContenido
            )
        }

        composable(Screen.InformeConsolidado.route) {
            InformeUtpScreen(navController = navController)
        }
    }
}
