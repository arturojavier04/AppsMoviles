package com.example.explora2.ui.screens.alumno

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.repository.ConfiguracionStore
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RendirPruebaScreen(navController: NavController) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    // --- PERSISTENCIA LOCAL (Punto 3: DataStore / Caso MPAZ PRO) ---
    val configuracionStore = remember { ConfiguracionStore(contexto) }
    val intentosRestantes by configuracionStore.intentosRestantesFlow.collectAsState(initial = 2)
    val ultimoPuntaje by configuracionStore.ultimoPuntajeFlow.collectAsState(initial = 0)

    val maximosIntentos = 2
    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var evaluacionEnviada by remember { mutableStateOf(false) }

    // Sincronizar estado visual si el alumno ya rindió la prueba anteriormente
    LaunchedEffect(intentosRestantes) {
        if (intentosRestantes < maximosIntentos) {
            evaluacionEnviada = true
        }
    }

    // --- SOPORTE MULTIMEDIA: TextToSpeech Nativo (Punto 1) ---
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    DisposableEffect(contexto) {
        val instanciaTts = TextToSpeech(contexto) { estado ->
            if (estado == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "CL")
            }
        }
        tts = instanciaTts
        onDispose {
            instanciaTts.stop()
            instanciaTts.shutdown()
        }
    }

    fun reproducirAudio(mensaje: String) {
        tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    val preguntaTexto = "¿Qué personaje ayudó al león a escapar de la red?"
    val opciones = listOf("Un ratón pequeño", "Un cazador del bosque", "Un pájaro veloz")
    val indiceCorrecto = 0 // El ratón pequeño

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prueba: Fábula y Comprensión (2.° Básico)") },
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
            // Tarjeta de estado de intentos persistidos
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (intentosRestantes > 0) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Intentos restantes: $intentosRestantes de $maximosIntentos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (intentosRestantes > 0) "Registro guardado en dispositivo local."
                            else "Intentos agotados en este dispositivo.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = {
                        reproducirAudio("Tienes $intentosRestantes intentos restantes para realizar esta evaluación.")
                    }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar intentos")
                    }
                }
            }

            // Pregunta con apoyo de audio didáctico
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = preguntaTexto,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { reproducirAudio(preguntaTexto) }) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Escuchar pregunta",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Opciones de respuesta para lectores iniciales
                    opciones.forEachIndexed { indice, opcion ->
                        OutlinedButton(
                            onClick = {
                                if (intentosRestantes > 0 && !evaluacionEnviada) {
                                    respuestaSeleccionada = indice
                                    reproducirAudio(opcion)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (respuestaSeleccionada == indice)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else Color.Transparent
                            ),
                            enabled = intentosRestantes > 0 && !evaluacionEnviada
                        ) {
                            Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            // Botón para enviar prueba y persistir los datos
            Button(
                onClick = {
                    if (respuestaSeleccionada != null && intentosRestantes > 0) {
                        val puntaje = if (respuestaSeleccionada == indiceCorrecto) 7 else 2
                        val nuevosIntentos = intentosRestantes - 1

                        scope.launch {
                            configuracionStore.guardarIntentoYNota(nuevosIntentos, puntaje)
                        }
                        evaluacionEnviada = true

                        if (puntaje == 7) {
                            reproducirAudio("¡Excelente trabajo! Respuesta correcta guardada.")
                        } else {
                            reproducirAudio("Respuesta incorrecta registrada. Recuerda leer con atención.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = respuestaSeleccionada != null && !evaluacionEnviada && intentosRestantes > 0
            ) {
                Text("Entregar y Guardar Evaluación")
            }

            // Retroalimentación inmediata (Aciertos / Errores persistidos)
            if (evaluacionEnviada) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (ultimoPuntaje >= 4) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (ultimoPuntaje >= 4) "Resultado: ¡Aprobado! ($ultimoPuntaje/7)"
                            else "Resultado: Requiere Refuerzo ($ultimoPuntaje/7)",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (ultimoPuntaje >= 4) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                        Text(
                            text = if (ultimoPuntaje >= 4) "¡Felicitaciones! Has completado el objetivo de lectura."
                            else "Has seleccionado una opción incorrecta. Revisa el texto e inténtalo otra vez."
                        )

                        if (intentosRestantes > 0) {
                            Button(
                                onClick = {
                                    evaluacionEnviada = false
                                    respuestaSeleccionada = null
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar Prueba ($intentosRestantes restante)")
                            }
                        } else {
                            Text(
                                text = "Has alcanzado el límite máximo de intentos configurados por el docente.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}