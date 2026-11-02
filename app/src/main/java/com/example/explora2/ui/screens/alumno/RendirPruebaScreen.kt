package com.example.explora2.ui.screens.alumno

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
    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var evaluacionEnviada by remember { mutableStateOf(false) }

    LaunchedEffect(intentosRestantes) {
        if (intentosRestantes < maximosIntentos) {
            evaluacionEnviada = true
        }
    }

    val preguntaTexto = "¿Qué personaje ayudó al león a escapar de la red?"
    val opciones = listOf("Un ratón pequeño", "Un cazador del bosque", "Un pájaro veloz")
    val indiceCorrecto = 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prueba: Fábula (2.° Básico)") },
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
                        onReproducirAudio("Tienes $intentosRestantes intentos restantes para realizar esta evaluación.")
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
                            text = preguntaTexto,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onReproducirAudio(preguntaTexto) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Escuchar pregunta",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    opciones.forEachIndexed { indice, opcion ->
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
                        val puntaje = if (respuestaSeleccionada == indiceCorrecto) 7 else 2
                        val nuevosIntentos = intentosRestantes - 1

                        onGuardarIntento(nuevosIntentos, puntaje)
                        evaluacionEnviada = true

                        if (puntaje == 7) {
                            onReproducirAudio("¡Excelente trabajo! Respuesta correcta guardada.")
                        } else {
                            onReproducirAudio("Respuesta incorrecta registrada. Recuerda leer con atención.")
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

@Preview(
    name = "Vista Celular - Rendir Evaluación",
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