package com.example.explora2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator


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

    data object ActividadRemota :
        Screen("actividad_remota/{id}") {

        fun crearRuta(id: Long): String =
            "actividad_remota/$id"
    }

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

            val estado by
            gestionViewModel.estado.collectAsState()

            val consultaAlumno by
            gestionViewModel.consultaAlumno.collectAsState()

            androidx.compose.runtime.LaunchedEffect(Unit) {
                gestionViewModel.cargarActividadesAlumno()
            }

            PanelAlumnoScreen(
                navController = navController,
                gestionUiState = estado,
                consultaAlumno = consultaAlumno,
                onActualizarActividades =
                    gestionViewModel::cargarActividadesAlumno,
                onAbrirActividad = { id ->
                    navController.navigate(
                        Screen.ActividadRemota.crearRuta(id)
                    )
                }
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
            RendirPruebaScreen(
                navController = navController,
                onEntregaDocente = { titulo, asignatura, unidad, nota, respuesta ->
                    gestionViewModel.onTituloChange(titulo)
                    gestionViewModel.onAsignaturaChange(asignatura)
                    gestionViewModel.onUnidadChange(unidad)
                    gestionViewModel.onObjetivoChange("Evaluación Formativa Oficial")
                    gestionViewModel.onRespuestaAlumnoChange("Alternativa seleccionada: \"$respuesta\" — Calificación obtenida: $nota")
                    gestionViewModel.entregarRespuesta()
                }
            )
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



        composable(
            route = Screen.ActividadRemota.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { entrada ->

            val id = entrada.arguments?.getLong("id") ?: -1L

            val consulta by
            gestionViewModel.consultaAlumno.collectAsState()

            androidx.compose.runtime.LaunchedEffect(id) {
                gestionViewModel.abrirActividadAlumno(id)
            }

            val plan = consulta.detalle

            if (consulta.cargandoDetalle) {

                Column {
                    CircularProgressIndicator()
                    Text("Cargando actividad...")
                }

            } else if (plan != null && plan.id == id) {

                ActividadLecturaScreen(
                    titulo = plan.titulo.orEmpty(),
                    instrucciones = plan.descripcion.orEmpty(),
                    respuestaEstudiante = consulta.respuesta,
                    errorRespuesta = consulta.errorRespuesta,
                    respuestaEntregada = plan.respuestaEntregada,
                    onRespuestaChange =
                        gestionViewModel::cambiarRespuestaRemota,
                    onEnviarClick =
                        gestionViewModel::entregarRespuestaRemota,
                    onVolverClick = {
                        navController.popBackStack()
                    },
                    enviando = consulta.enviando
                )

            } else {

                Column {
                    Text(
                        consulta.errorDetalle
                            ?: "Actividad no disponible."
                    )

                    Button(
                        onClick = {
                            gestionViewModel.abrirActividadAlumno(id)
                        }
                    ) {
                        Text("Reintentar")
                    }

                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Volver")
                    }
                }
            }
        }



        composable(Screen.PanelDocente.route) {

            val estado by gestionViewModel.estado.collectAsState()

            val consultaDocente by
            gestionViewModel.consultaDocente.collectAsState()

            androidx.compose.runtime.LaunchedEffect(Unit) {
                gestionViewModel.cargarPlanificacionesDocente()
            }

            PanelDocenteScreen(
                navController = navController,
                gestionUiState = estado,
                onAsignarClick = gestionViewModel::asignarAlAlumno,
                consultaDocente = consultaDocente,
                onActualizarPlanificaciones =
                    gestionViewModel::cargarPlanificacionesDocente,
                onAsignarRemota =
                    gestionViewModel::asignarPlanificacion
            )
        }


        composable(Screen.ConfiguracionExamen.route) {
            ConfiguracionExamenScreen(
                navController = navController
            )
        }


        composable(Screen.RevisionEntregas.route) {

            val estado by
            gestionViewModel.estado.collectAsState()

            val revisionEntregas by
            gestionViewModel.revisionEntregas.collectAsState()

            androidx.compose.runtime.LaunchedEffect(Unit) {
                gestionViewModel.cargarEntregasDocente()
            }

            RevisionEntregasScreen(
                navController = navController,
                gestionUiState = estado,
                revisionEntregas = revisionEntregas,
                onActualizarEntregas =
                    gestionViewModel::cargarEntregasDocente
            )
        }


        composable(Screen.GestionContenido.route) {
            val estado by gestionViewModel.estado.collectAsState()

            FormularioGestionScreen(
                navController = navController,
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
                },
                onNuevaPlanificacionClick = gestionViewModel::nuevaPlanificacion
            )
        }

        // UTP

        composable(Screen.PanelUtp.route) {
            PanelUtpScreen(navController = navController)
        }


        composable(Screen.AutorizacionUnidades.route) {
            val estado by gestionViewModel.estado.collectAsState()

            val consulta by gestionViewModel.consulta.collectAsState()

            androidx.compose.runtime.LaunchedEffect(Unit) {
                gestionViewModel.cargarSolicitudes()
            }

            AutorizacionUtpScreen(
                consulta = consulta,
                onActualizar = gestionViewModel::cargarSolicitudes,
                onDetalle = gestionViewModel::consultarDetalle,
                navController = navController,
                gestionUiState = estado,
                onAprobarClick = gestionViewModel::aprobarContenido,
                onAutorizarRemota = gestionViewModel::autorizarPlanificacion
            )
        }


        composable(Screen.InformeConsolidado.route) {
            InformeUtpScreen(navController = navController)
        }
    }
}