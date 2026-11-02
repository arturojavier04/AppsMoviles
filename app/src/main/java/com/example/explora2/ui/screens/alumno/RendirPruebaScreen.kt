package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.repository.ConfiguracionStore
import com.example.explora2.ui.utils.Audio
import kotlinx.coroutines.launch

// Modelo para preguntas pedagógicas de 2.° básico
data class PreguntaEvaluacion(
    val asignatura: String,
    val titulo: String,
    val pregunta: String,
    val opciones: List<String>,
    val respuestaCorrecta: Int,
    val explicacion: String
)

@Composable
fun RendirPruebaScreen(navController: NavController) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    val configuracionStore = remember { ConfiguracionStore(contexto) }
    val intentosRestantes by configuracionStore.intentosRestantesFlow.collectAsState(initial = 2)
    val ultimoPuntaje by configuracionStore.ultimoPuntajeFlow.collectAsState(initial = 0)

    val audioHelper = remember { Audio(contexto) }

    DisposableEffect(Unit) {
        onDispose {
            audioHelper.liberar()
        }
    }

    RendirPruebaContent(
        intentosRestantes = intentosRestantes,
        ultimoPuntaje = ultimoPuntaje,
        onVolverClick = { navController.popBackStack() },
        onReproducirAudio = { texto -> audioHelper.hablar(texto) },
        onGuardarIntento = { nuevosIntentos, puntaje ->
            scope.launch {
                configuracionStore.guardarIntentoYNota(nuevosIntentos, puntaje)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RendirPruebaContent(
    intentosRestantes: Int,
    ultimoPuntaje: Int,
    onVolverClick: () -> Unit,
    onReproducirAudio: (String) -> Unit,
    onGuardarIntento: (Int, Int) -> Unit
) {
    val maximosIntentos = 2

    // Banco de desafíos oficiales para las 4 asignaturas del caso
    val evaluaciones = listOf(
        PreguntaEvaluacion(
            asignatura = "Lenguaje",
            titulo = "Lectura: Fábula y Comprensión",
            pregunta = "¿Qué personaje ayudó al león a escapar de la red?",
            opciones = listOf("Un ratón pequeño", "Un cazador del bosque", "Un pájaro veloz"),
            respuestaCorrecta = 0,
            explicacion = "El ratón royó la cuerda de la red con sus dientes."
        ),
        PreguntaEvaluacion(
            asignatura = "Matemática",
            titulo = "Matemática: Sumas hasta 100",
            pregunta = "¿Cuánto es 35 + 24?",
            opciones = listOf("49", "59", "61"),
            respuestaCorrecta = 1,
            explicacion = "30 + 20 son 50, y 5 + 4 son 9. Da 59."
        ),
        PreguntaEvaluacion(
            asignatura = "Ciencias",
            titulo = "Ciencias: Hábitos y Cuerpo",
            pregunta = "¿Qué órgano bombea sangre a todo el cuerpo humano?",
            opciones = listOf("El estómago", "Los pulmones", "El corazón"),
            respuestaCorrecta = 2,
            explicacion = "El corazón bombea sangre para dar energía a todo el cuerpo."
        ),
        PreguntaEvaluacion(
            asignatura = "Historia",
            titulo = "Historia: Nuestra Comunidad",
            pregunta = "¿Qué institución ayuda a apagar incendios en la comunidad?",
            opciones = listOf("Bomberos", "El hospital", "La biblioteca"),
            respuestaCorrecta = 0,
            explicacion = "Los bomberos asisten a la comunidad ante incendios y rescates."
        )
    )

    var materiaActiva by remember { mutableStateOf("Lenguaje") }
    val evaluacionActual = evaluaciones.first { it.asignatura == materiaActiva }

    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var evaluacionEnviada by remember { mutableStateOf(false) }
    var notaCalculada by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(evaluacionActual.titulo) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al panel"
                        )
                    }
                }
            )
        }
    ) { espacioInterior ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterior)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Selector horizontal para cambiar de materia dentro de la prueba
            Text(text = "Elige la evaluación de tu asignatura:", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Lenguaje", "Matemática", "Ciencias", "Historia").forEach { materia ->
                    FilterChip(
                        selected = materiaActiva == materia,
                        onClick = {
                            materiaActiva = materia
                            respuestaSeleccionada = null
                            evaluacionEnviada = false
                        },
                        label = { Text(materia) }
                    )
                }
            }

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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Intentos disponibles: $intentosRestantes de $maximosIntentos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (intentosRestantes > 0) "Escucha y responde el desafío."
                            else "Has agotado los intentos para esta prueba.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = {
                        onReproducirAudio("Tienes $intentosRestantes intentos para responder.")
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Escuchar intentos"
                        )
                    }
                }
            }

            // Pregunta con soporte auditivo
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = evaluacionActual.pregunta,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onReproducirAudio(evaluacionActual.pregunta) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Escuchar pregunta",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    evaluacionActual.opciones.forEachIndexed { indice, opcion ->
                        OutlinedButton(
                            onClick = {
                                if (intentosRestantes > 0 && !evaluacionEnviada) {
                                    respuestaSeleccionada = indice
                                    onReproducirAudio(opcion)
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

            // Botón de entrega
            Button(
                onClick = {
                    if (respuestaSeleccionada != null && intentosRestantes > 0) {
                        val esCorrecto = (respuestaSeleccionada == evaluacionActual.respuestaCorrecta)
                        notaCalculada = if (esCorrecto) 7 else 2
                        val nuevosIntentos = intentosRestantes - 1

                        onGuardarIntento(nuevosIntentos, notaCalculada)
                        evaluacionEnviada = true

                        if (esCorrecto) {
                            onReproducirAudio("¡Excelente! Respuesta correcta. Obtuviste siete puntos.")
                        } else {
                            onReproducirAudio("Respuesta incorrecta. " + evaluacionActual.explicacion)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = respuestaSeleccionada != null && !evaluacionEnviada && intentosRestantes > 0
            ) {
                Text("Entregar y Guardar Evaluación")
            }

            // Retroalimentación pedagógica inmediata
            if (evaluacionEnviada) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (notaCalculada >= 4) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (notaCalculada >= 4) "¡Aprobado! ($notaCalculada/7 puntos)"
                            else "Requiere Refuerzo ($notaCalculada/7 puntos)",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (notaCalculada >= 4) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                        Text(text = evaluacionActual.explicacion)

                        if (intentosRestantes > 0) {
                            Button(
                                onClick = {
                                    evaluacionEnviada = false
                                    respuestaSeleccionada = null
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar Desafío ($intentosRestantes restante)")
                            }
                        } else {
                            Text(
                                text = "Límite máximo de intentos alcanzado.",
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

@Preview(
    name = "Vista Celular - Rendir Evaluación Dinámica",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun RendirPruebaScreenPreview() {
    MaterialTheme {
        RendirPruebaContent(
            intentosRestantes = 2,
            ultimoPuntaje = 0,
            onVolverClick = {},
            onReproducirAudio = {},
            onGuardarIntento = { _, _ -> }
        )
    }
}