package com.example.explora2.ui.screens.alumno

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RendirPruebaScreen(navController: NavController) {
    val contexto = LocalContext.current

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

    // --- CONTROL DE INTENTOS Y EVALUACIÓN (Punto 2) ---
    val maximosIntentos = 2
    var intentosRestantes by remember { mutableStateOf(maximosIntentos) }
    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var evaluacionEnviada by remember { mutableStateOf(false) }
    var puntajeObtenido by remember { mutableStateOf(0) }

    val preguntaTexto = "¿Qué personaje ayudó al león a escapar de la red?"
    val opciones = listOf("Un ratón pequeño", "Un cazador del bosque", "Un pájaro veloz")
    val indiceCorrecto = 0 // El ratón pequeño

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Prueba: Fábula y Comprensión (2.° Básico)") })
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
            // Tarjeta de estado de intentos
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
                    Text(
                        text = "Intentos restantes: $intentosRestantes de $maximosIntentos",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = {
                        reproducirAudio("Tienes $intentosRestantes intentos restantes para realizar esta evaluación.")
                    }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar intentos")
                    }
                }
            }

            // Pregunta con botón de audio de apoyo didáctico
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

            // Botón para enviar prueba
            Button(
                onClick = {
                    if (respuestaSeleccionada != null && intentosRestantes > 0) {
                        evaluacionEnviada = true
                        intentosRestantes -= 1

                        if (respuestaSeleccionada == indiceCorrecto) {
                            puntajeObtenido = 7
                            reproducirAudio("¡Excelente trabajo! Respuesta correcta. Obtuviste siete puntos.")
                        } else {
                            puntajeObtenido = 2
                            reproducirAudio("Respuesta incorrecta. Recuerda leer con atención el texto.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = respuestaSeleccionada != null && !evaluacionEnviada && intentosRestantes > 0
            ) {
                Text("Entregar Evaluación")
            }

            // Retroalimentación inmediata (Aciertos / Errores)
            if (evaluacionEnviada) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (puntajeObtenido >= 4) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (puntajeObtenido >= 4) "Resultado: ¡Aprobado! (7/7)" else "Resultado: Requiere Refuerzo (2/7)",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (puntajeObtenido >= 4) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                        Text(
                            text = if (puntajeObtenido >= 4) "¡Felicitaciones! Has completado el objetivo de lectura."
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
                                Text("Reintentar Prueba ($intentosRestantes intento restante)")
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