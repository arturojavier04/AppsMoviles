package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.repository.ConfiguracionStore
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.BarraNavegacionInferiorContent
import com.example.explora2.ui.components.IconoLibroInfantil
import com.example.explora2.ui.components.IconoTrofeoInfantil
import com.example.explora2.ui.utils.Audio
import com.example.explora2.utils.Vibracion
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class PreguntaEvaluacion(
    val asignatura: String,
    val unidad: String,
    val titulo: String,
    val pregunta: String,
    val opciones: List<String>,
    val respuestaCorrecta: Int,
    val explicacion: String
)

@Composable
fun RendirPruebaScreen(
    navController: NavController,
    onEntregaDocente: ((titulo: String, asignatura: String, unidad: String, nota: String, respuesta: String) -> Unit)? = null
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    val materiaRecibida = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("materia") ?: "Lenguaje"

    val unidadRecibida = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("unidad") ?: "Unidad 1"

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
        materiaInicial = materiaRecibida,
        unidadInicial = unidadRecibida,
        intentosRestantes = intentosRestantes,
        ultimoPuntaje = ultimoPuntaje,
        onVolverClick = { navController.popBackStack() },
        onReproducirAudio = { texto -> audioHelper.hablar(texto) },
        onVibrarError = { Vibracion.vibrarError(contexto) },
        onGuardarIntento = { nuevosIntentos, puntaje ->
            scope.launch {
                configuracionStore.guardarIntentoYNota(nuevosIntentos, puntaje)
            }
        },
        onEntregaDocente = onEntregaDocente,
        bottomBar = {
            BarraNavegacionInferior(
                navController = navController,
                rutaActual = Screen.Asignaturas.route
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RendirPruebaContent(
    materiaInicial: String = "Lenguaje",
    unidadInicial: String = "Unidad 1",
    intentosRestantes: Int,
    ultimoPuntaje: Int,
    onVolverClick: () -> Unit,
    onReproducirAudio: (String) -> Unit,
    onVibrarError: () -> Unit = {},
    onGuardarIntento: (Int, Int) -> Unit,
    onEntregaDocente: ((String, String, String, String, String) -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {
        BarraNavegacionInferiorContent(rutaActual = Screen.Asignaturas.route)
    }
) {
    val maximosIntentos = 2

    val evaluaciones = listOf(
        // LENGUAJE
        PreguntaEvaluacion(
            asignatura = "Lenguaje",
            unidad = "Unidad 1",
            titulo = "Lenguaje · Unidad 1",
            pregunta = "¿Qué personaje ayudó al león a escapar de la red?",
            opciones = listOf("Un ratón pequeño", "Un cazador del bosque", "Un pájaro veloz"),
            respuestaCorrecta = 0,
            explicacion = "El ratón royó la red con sus dientes para salvar al león."
        ),
        PreguntaEvaluacion(
            asignatura = "Lenguaje",
            unidad = "Unidad 2",
            titulo = "Lenguaje · Unidad 2",
            pregunta = "¿Con qué tipo de letra se debe comenzar siempre una oración?",
            opciones = listOf("Con letra minúscula", "Con letra mayúscula", "Con un número"),
            respuestaCorrecta = 1,
            explicacion = "Toda oración comienza con mayúscula y termina con punto final."
        ),

        // MATEMÁTICA
        PreguntaEvaluacion(
            asignatura = "Matemática",
            unidad = "Unidad 1",
            titulo = "Matemática · Unidad 1",
            pregunta = "¿Cuánto es 35 + 24?",
            opciones = listOf("49", "59", "61"),
            respuestaCorrecta = 1,
            explicacion = "35 más 24 es igual a 59 (30 + 20 = 50, y 5 + 4 = 9)."
        ),
        PreguntaEvaluacion(
            asignatura = "Matemática",
            unidad = "Unidad 2",
            titulo = "Matemática · Unidad 2",
            pregunta = "Si contamos de 10 en 10: 20, 30, 40... ¿qué número viene después?",
            opciones = listOf("45", "50", "60"),
            respuestaCorrecta = 1,
            explicacion = "Siguiendo el patrón de 10 en 10, después del 40 viene el 50."
        ),

        // CIENCIAS
        PreguntaEvaluacion(
            asignatura = "Ciencias",
            unidad = "Unidad 1",
            titulo = "Ciencias · Unidad 1",
            pregunta = "¿Qué órgano bombea sangre a todo nuestro cuerpo?",
            opciones = listOf("El estómago", "Los pulmones", "El corazón"),
            respuestaCorrecta = 2,
            explicacion = "El corazón bombea sangre llevando oxígeno a todo el cuerpo."
        ),
        PreguntaEvaluacion(
            asignatura = "Ciencias",
            unidad = "Unidad 2",
            titulo = "Ciencias · Unidad 2",
            pregunta = "¿Cuál de estos animales es un ave nativa de la cordillera chilena?",
            opciones = listOf("El Cóndor", "El León", "El Canguro"),
            respuestaCorrecta = 0,
            explicacion = "El cóndor es un ave emblemática que habita la cordillera de los Andes."
        ),

        // HISTORIA
        PreguntaEvaluacion(
            asignatura = "Historia",
            unidad = "Unidad 1",
            titulo = "Historia · Unidad 1",
            pregunta = "¿Qué institución voluntaria ayuda a apagar incendios en la comunidad?",
            opciones = listOf("Los Bomberos", "El Hospital", "La Biblioteca"),
            respuestaCorrecta = 0,
            explicacion = "Los bomberos asisten y protegen a la comunidad ante incendios y rescates."
        ),
        PreguntaEvaluacion(
            asignatura = "Historia",
            unidad = "Unidad 2",
            titulo = "Historia · Unidad 2",
            pregunta = "¿Qué pueblo originario navegaba en canoas por los canales del sur de Chile?",
            opciones = listOf("Los Chonos y Yaganes", "Los Mayas", "Los Aztecas"),
            respuestaCorrecta = 0,
            explicacion = "Los pueblos canoeros como los Yaganes recorrían los canales australes de Chile."
        )
    )

    var materiaActiva by remember { mutableStateOf(materiaInicial) }
    var unidadActiva by remember { mutableStateOf(unidadInicial) }

    val evaluacionActual = evaluaciones.firstOrNull {
        it.asignatura == materiaActiva && it.unidad == unidadActiva
    } ?: evaluaciones.firstOrNull { it.asignatura == materiaActiva } ?: evaluaciones.first()

    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var evaluacionEnviada by remember { mutableStateOf(false) }
    var notaCalculada by remember { mutableStateOf(0) }

    val letrasOpciones = listOf("A", "B", "C")

    Scaffold(
        containerColor = Color(0xFFFFFDE7),
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFF7043), // Coral alegre
                                    Color(0xFFFFB300)  // Amarillo sol
                                )
                            )
                        )
                ) {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconoTrofeoInfantil(modifier = Modifier.size(28.dp))
                                }
                                Column {
                                    Text(
                                        text = "Desafío · ${evaluacionActual.asignatura}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 19.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${evaluacionActual.titulo} · 2.° Básico",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = Color(0xFFFFF8E1)
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = onVolverClick,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.92f))
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver al panel",
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        actions = {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.92f),
                                modifier = Modifier.padding(end = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFA000),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "7 pts",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }
        },
        bottomBar = bottomBar
    ) { espacioInterior ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterior)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. SELECTOR DE UNIDAD TIPO PÍLDORA KAWAII
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Unidad 1", "Unidad 2").forEach { unidad ->
                    val seleccionada = unidadActiva == unidad
                    FilterChip(
                        selected = seleccionada,
                        onClick = {
                            unidadActiva = unidad
                            respuestaSeleccionada = null
                            evaluacionEnviada = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFF7043),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFFE65100)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = seleccionada,
                            borderColor = Color(0xFFFF9800),
                            borderWidth = 2.dp
                        ),
                        label = {
                            Text(
                                text = "📘 $unidad",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    )
                }
            }

            // 2. TARJETA DE INTENTOS RESTANTES
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (intentosRestantes > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 3.dp,
                        color = if (intentosRestantes > 0) Color(0xFF66BB6A) else Color(0xFFEF5350),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (intentosRestantes > 0)
                                "⚡ Intentos: $intentosRestantes de $maximosIntentos"
                            else
                                "🔒 Sin intentos disponibles",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = if (intentosRestantes > 0) Color(0xFF1B5E20) else Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (intentosRestantes > 0) "Toca una opción para escucharla y ver su dibujo."
                            else "Has completado tus oportunidades en esta prueba.",
                            fontSize = 14.sp,
                            color = if (intentosRestantes > 0) Color(0xFF2E7D32) else Color(0xFFB71C1C)
                        )
                    }
                    IconButton(
                        onClick = {
                            onReproducirAudio("Tienes $intentosRestantes intentos restantes para responder esta evaluación.")
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, Color(0xFF43A047), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Escuchar intentos",
                            tint = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            // 3. TARJETA KAWAII DE LA PREGUNTA Y ALTERNATIVAS (BORDE NARANJA + ESTRELLITA KAWAII)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(3.dp, Color(0xFFFFA726), RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF3E0))
                                .border(2.5.dp, Color(0xFFFB8C00), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            IconoEstrellaPreguntaKawaii(modifier = Modifier.size(36.dp))
                        }
                        Text(
                            text = evaluacionActual.pregunta,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { onReproducirAudio(evaluacionActual.pregunta) },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF9800))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Escuchar pregunta",
                                tint = Color.White
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFFFE0B2))

                    // Opciones didácticas: Letra (A/B/C) + Texto + Personaje Kawaii de la opción
                    evaluacionActual.opciones.forEachIndexed { indice, opcion ->
                        val estaSeleccionada = respuestaSeleccionada == indice
                        OutlinedButton(
                            onClick = {
                                if (intentosRestantes > 0 && !evaluacionEnviada) {
                                    respuestaSeleccionada = indice
                                    onReproducirAudio(opcion)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 68.dp),
                            shape = RoundedCornerShape(22.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            border = BorderStroke(
                                width = if (estaSeleccionada) 3.dp else 2.dp,
                                color = if (estaSeleccionada) Color(0xFF8E24AA) else Color(0xFFFFB74D)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (estaSeleccionada) Color(0xFFF3E5F5) else Color(0xFFFFFBF2),
                                contentColor = Color(0xFF3E2723)
                            ),
                            enabled = intentosRestantes > 0 && !evaluacionEnviada
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Burbuja con letra A, B o C
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (estaSeleccionada) Color(0xFF8E24AA) else Color(0xFFFF9800)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letrasOpciones.getOrElse(indice) { "•" },
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                }

                                // Texto de la alternativa
                                Text(
                                    text = opcion,
                                    fontSize = 16.sp,
                                    fontWeight = if (estaSeleccionada) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )

                                // Burbuja con ilustración kawaii específica para esa opción
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(
                                            width = 2.dp,
                                            color = if (estaSeleccionada) Color(0xFFAB47BC) else Color(0xFFFFCC80),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IlustracionOpcionKawaii(
                                        opcion = opcion,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. BOTÓN DE ENTREGA KAWAII
            Button(
                onClick = {
                    if (respuestaSeleccionada != null && intentosRestantes > 0) {
                        val esCorrecto = (respuestaSeleccionada == evaluacionActual.respuestaCorrecta)
                        notaCalculada = if (esCorrecto) 7 else 2
                        val nuevosIntentos = intentosRestantes - 1

                        onGuardarIntento(nuevosIntentos, notaCalculada)
                        evaluacionEnviada = true

                        val textoOpcionElegida = evaluacionActual.opciones[respuestaSeleccionada!!]

                        onEntregaDocente?.invoke(
                            evaluacionActual.titulo,
                            evaluacionActual.asignatura,
                            evaluacionActual.unidad,
                            "$notaCalculada/7 puntos",
                            textoOpcionElegida
                        )

                        if (esCorrecto) {
                            onReproducirAudio("¡Excelente! Respuesta correcta. Obtuviste siete puntos.")
                        } else {
                            onVibrarError()
                            onReproducirAudio("Respuesta incorrecta. " + evaluacionActual.explicacion)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF43A047),
                    disabledContainerColor = Color(0xFFC8E6C9)
                ),
                enabled = respuestaSeleccionada != null && !evaluacionEnviada && intentosRestantes > 0
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¡Enviar mi Respuesta! 🚀",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // 5. TARJETA DE RESULTADO Y RETROALIMENTACIÓN KAWAII
            if (evaluacionEnviada) {
                val aprobado = notaCalculada >= 4
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 3.dp,
                            color = if (aprobado) Color(0xFF43A047) else Color(0xFFFF7043),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (aprobado) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (aprobado) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconoTrofeoInfantil(modifier = Modifier.size(38.dp))
                                }
                            }
                            Text(
                                text = if (aprobado)
                                    "🎉 ¡Lo lograste! ($notaCalculada/7 pts)"
                                else
                                    "💪 ¡Casi! Sigamos practicando ($notaCalculada/7 pts)",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (aprobado) Color(0xFF1B5E20) else Color(0xFFE65100),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = evaluacionActual.explicacion,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF3E2723)
                        )

                        if (intentosRestantes > 0) {
                            Button(
                                onClick = {
                                    evaluacionEnviada = false
                                    respuestaSeleccionada = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800)
                                )
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Intentar otra vez ($intentosRestantes restante)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        } else {
                            Text(
                                text = "Has usado todos tus intentos disponibles.",
                                color = Color(0xFFC62828),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// ILUSTRACIONES DIDÁCTICAS KAWAII PARA CADA ALTERNATIVA
// =========================================================================

@Composable
private fun IlustracionOpcionKawaii(
    opcion: String,
    modifier: Modifier = Modifier
) {
    val texto = opcion.lowercase()

    when {
        // LENGUAJE U1: Ratón, Cazador, Pájaro
        "ratón" in texto -> DibujoRatonKawaii(modifier)
        "cazador" in texto -> DibujoSombreroExploradorKawaii(modifier)
        "pájaro" in texto -> DibujoPajaroKawaii(modifier)

        // LENGUAJE U2: Minúscula, Mayúscula, Número
        "minúscula" in texto -> DibujoFichaTextoKawaii(texto = "a", colorFondo = Color(0xFFFFCCBC), colorBorde = Color(0xFFE64A19), modifier = modifier)
        "mayúscula" in texto -> DibujoFichaTextoKawaii(texto = "A", colorFondo = Color(0xFFC8E6C9), colorBorde = Color(0xFF2E7D32), modifier = modifier)
        "número" in texto -> DibujoFichaTextoKawaii(texto = "123", colorFondo = Color(0xFFBBDEFB), colorBorde = Color(0xFF1565C0), modifier = modifier)

        // CIENCIAS U1: Estómago, Pulmones, Corazón
        "corazón" in texto -> DibujoCorazonKawaii(modifier)
        "pulmones" in texto -> DibujoPulmonesKawaii(modifier)
        "estómago" in texto -> DibujoManzanaKawaii(modifier)

        // CIENCIAS U2: Cóndor, León, Canguro
        "cóndor" in texto -> DibujoCondorKawaii(modifier)
        "león" in texto -> DibujoLeonKawaii(modifier)
        "canguro" in texto -> DibujoCanguroKawaii(modifier)

        // HISTORIA U1: Bomberos, Hospital, Biblioteca
        "bomberos" in texto -> DibujoCascoBomberoKawaii(modifier)
        "hospital" in texto -> DibujoHospitalKawaii(modifier)
        "biblioteca" in texto -> IconoLibroInfantil(modifier)

        // HISTORIA U2: Chonos y Yaganes (canoa), Mayas, Aztecas
        "yaganes" in texto || "chonos" in texto -> DibujoCanoaKawaii(modifier)
        "mayas" in texto || "aztecas" in texto -> DibujoPiramideKawaii(modifier)

        // MATEMÁTICA (Números con ficha kawaii colorida)
        else -> DibujoFichaTextoKawaii(
            texto = opcion.take(3),
            colorFondo = Color(0xFFFFF59D),
            colorBorde = Color(0xFFF57F17),
            modifier = modifier
        )
    }
}

@Composable
private fun DibujoRatonKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFF90A4AE), radius = w * 0.19f, center = Offset(w * 0.24f, h * 0.30f))
        drawCircle(color = Color(0xFFF8BBD0), radius = w * 0.11f, center = Offset(w * 0.24f, h * 0.30f))
        drawCircle(color = Color(0xFF90A4AE), radius = w * 0.19f, center = Offset(w * 0.76f, h * 0.30f))
        drawCircle(color = Color(0xFFF8BBD0), radius = w * 0.11f, center = Offset(w * 0.76f, h * 0.30f))
        drawCircle(color = Color(0xFFB0BEC5), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.56f))
        drawCircle(color = Color(0xFF455A64), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.56f), style = Stroke(w * 0.04f))
        drawCircle(color = Color(0xFFEC407A), radius = w * 0.055f, center = Offset(w * 0.50f, h * 0.62f))
        dibujarCaritaMini(w * 0.50f, h * 0.54f, w * 0.21f)
    }
}

@Composable
private fun DibujoPajaroKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawLine(
            color = Color(0xFFEF6C00),
            start = Offset(w * 0.40f, h * 0.78f),
            end = Offset(w * 0.40f, h * 0.88f),
            strokeWidth = w * 0.05f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFFEF6C00),
            start = Offset(w * 0.60f, h * 0.78f),
            end = Offset(w * 0.60f, h * 0.88f),
            strokeWidth = w * 0.05f,
            cap = StrokeCap.Round
        )

        val alaIzq = Path().apply {
            moveTo(w * 0.25f, h * 0.46f)
            quadraticTo(w * 0.04f, h * 0.34f, w * 0.14f, h * 0.62f)
            close()
        }
        drawPath(alaIzq, Color(0xFF0288D1))
        drawPath(alaIzq, Color(0xFF01579B), style = Stroke(w * 0.035f))

        val alaDer = Path().apply {
            moveTo(w * 0.75f, h * 0.46f)
            quadraticTo(w * 0.96f, h * 0.34f, w * 0.86f, h * 0.62f)
            close()
        }
        drawPath(alaDer, Color(0xFF0288D1))
        drawPath(alaDer, Color(0xFF01579B), style = Stroke(w * 0.035f))

        drawOval(
            color = Color(0xFF29B6F6),
            topLeft = Offset(w * 0.42f, h * 0.10f),
            size = Size(w * 0.10f, h * 0.16f)
        )
        drawOval(
            color = Color(0xFF29B6F6),
            topLeft = Offset(w * 0.50f, h * 0.12f),
            size = Size(w * 0.10f, h * 0.15f)
        )

        drawOval(
            color = Color(0xFF4FC3F7),
            topLeft = Offset(w * 0.22f, h * 0.22f),
            size = Size(w * 0.56f, h * 0.58f)
        )
        drawOval(
            color = Color(0xFF01579B),
            topLeft = Offset(w * 0.22f, h * 0.22f),
            size = Size(w * 0.56f, h * 0.58f),
            style = Stroke(w * 0.04f)
        )

        val pico = Path().apply {
            moveTo(w * 0.43f, h * 0.54f)
            lineTo(w * 0.57f, h * 0.54f)
            lineTo(w * 0.50f, h * 0.65f)
            close()
        }
        drawPath(pico, Color(0xFFFF9800))
        drawPath(pico, Color(0xFFE65100), style = Stroke(w * 0.025f))

        dibujarCaritaMini(w * 0.50f, h * 0.44f, w * 0.20f)
    }
}

@Composable
private fun DibujoSombreroExploradorKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFF66BB6A), topLeft = Offset(w * 0.28f, h * 0.24f), size = Size(w * 0.44f, h * 0.36f), cornerRadius = CornerRadius(w * 0.16f))
        drawRoundRect(color = Color(0xFF388E3C), topLeft = Offset(w * 0.12f, h * 0.54f), size = Size(w * 0.76f, h * 0.14f), cornerRadius = CornerRadius(w * 0.07f))
        dibujarCaritaMini(w * 0.50f, h * 0.43f, w * 0.16f)
    }
}

@Composable
private fun DibujoCorazonKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.50f, h * 0.82f)
            cubicTo(w * 0.10f, h * 0.55f, w * 0.14f, h * 0.20f, w * 0.50f, h * 0.36f)
            cubicTo(w * 0.86f, h * 0.20f, w * 0.90f, h * 0.55f, w * 0.50f, h * 0.82f)
            close()
        }
        drawPath(path, Color(0xFFEF5350))
        drawPath(path, Color(0xFFB71C1C), style = Stroke(w * 0.045f))
        dibujarCaritaMini(w * 0.50f, h * 0.52f, w * 0.18f)
    }
}

@Composable
private fun DibujoPulmonesKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFFF48FB1), topLeft = Offset(w * 0.16f, h * 0.26f), size = Size(w * 0.30f, h * 0.52f), cornerRadius = CornerRadius(w * 0.14f))
        drawRoundRect(color = Color(0xFFF48FB1), topLeft = Offset(w * 0.54f, h * 0.26f), size = Size(w * 0.30f, h * 0.52f), cornerRadius = CornerRadius(w * 0.14f))
        dibujarCaritaMini(w * 0.50f, h * 0.54f, w * 0.18f)
    }
}

@Composable
private fun DibujoManzanaKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFFFF7043), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.56f))
        drawOval(color = Color(0xFF66BB6A), topLeft = Offset(w * 0.48f, h * 0.16f), size = Size(w * 0.22f, h * 0.14f))
        dibujarCaritaMini(w * 0.50f, h * 0.56f, w * 0.20f)
    }
}

@Composable
private fun DibujoCondorKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFFECEFF1), topLeft = Offset(w * 0.24f, h * 0.62f), size = Size(w * 0.52f, h * 0.18f), cornerRadius = CornerRadius(w * 0.09f))
        drawCircle(color = Color(0xFF37474F), radius = w * 0.30f, center = Offset(w * 0.50f, h * 0.44f))
        dibujarCaritaMini(w * 0.50f, h * 0.44f, w * 0.19f)
    }
}

@Composable
private fun DibujoLeonKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFFFF8F00), radius = w * 0.40f, center = Offset(w * 0.50f, h * 0.50f))
        drawCircle(color = Color(0xFFFFCA28), radius = w * 0.28f, center = Offset(w * 0.50f, h * 0.50f))
        dibujarCaritaMini(w * 0.50f, h * 0.50f, w * 0.19f)
    }
}

@Composable
private fun DibujoCanguroKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawOval(color = Color(0xFFA1887F), topLeft = Offset(w * 0.26f, h * 0.12f), size = Size(w * 0.14f, h * 0.32f))
        drawOval(color = Color(0xFFA1887F), topLeft = Offset(w * 0.60f, h * 0.12f), size = Size(w * 0.14f, h * 0.32f))
        drawCircle(color = Color(0xFFBCAAA4), radius = w * 0.31f, center = Offset(w * 0.50f, h * 0.56f))
        dibujarCaritaMini(w * 0.50f, h * 0.56f, w * 0.19f)
    }
}

@Composable
private fun DibujoCascoBomberoKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFFE53935), radius = w * 0.32f, center = Offset(w * 0.50f, h * 0.50f))
        drawRoundRect(color = Color(0xFFB71C1C), topLeft = Offset(w * 0.14f, h * 0.58f), size = Size(w * 0.72f, h * 0.14f), cornerRadius = CornerRadius(w * 0.07f))
        drawCircle(color = Color(0xFFFFD54F), radius = w * 0.09f, center = Offset(w * 0.50f, h * 0.32f))
        dibujarCaritaMini(w * 0.50f, h * 0.50f, w * 0.17f)
    }
}

@Composable
private fun DibujoHospitalKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFFE3F2FD), topLeft = Offset(w * 0.20f, h * 0.20f), size = Size(w * 0.60f, h * 0.60f), cornerRadius = CornerRadius(w * 0.12f))
        drawRect(color = Color(0xFFE53935), topLeft = Offset(w * 0.45f, h * 0.26f), size = Size(w * 0.10f, h * 0.24f))
        drawRect(color = Color(0xFFE53935), topLeft = Offset(w * 0.38f, h * 0.33f), size = Size(w * 0.24f, h * 0.10f))
        dibujarCaritaMini(w * 0.50f, h * 0.64f, w * 0.16f)
    }
}

@Composable
private fun DibujoCanoaKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFF4FC3F7), topLeft = Offset(w * 0.12f, h * 0.66f), size = Size(w * 0.76f, h * 0.16f), cornerRadius = CornerRadius(w * 0.08f))
        val canoa = Path().apply {
            moveTo(w * 0.14f, h * 0.44f)
            lineTo(w * 0.86f, h * 0.44f)
            lineTo(w * 0.72f, h * 0.68f)
            lineTo(w * 0.28f, h * 0.68f)
            close()
        }
        drawPath(canoa, Color(0xFF8D6E63))
        dibujarCaritaMini(w * 0.50f, h * 0.54f, w * 0.15f)
    }
}

@Composable
private fun DibujoPiramideKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val piramide = Path().apply {
            moveTo(w * 0.50f, h * 0.20f)
            lineTo(w * 0.14f, h * 0.78f)
            lineTo(w * 0.86f, h * 0.78f)
            close()
        }
        drawPath(piramide, Color(0xFFFFB74D))
        drawPath(piramide, Color(0xFFE65100), style = Stroke(w * 0.04f))
        dibujarCaritaMini(w * 0.50f, h * 0.58f, w * 0.16f)
    }
}

@Composable
private fun DibujoFichaTextoKawaii(
    texto: String,
    colorFondo: Color,
    colorBorde: Color,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(colorFondo)
            .border(2.dp, colorBorde, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontWeight = FontWeight.ExtraBold,
            fontSize = if (texto.length > 2) 12.sp else 17.sp,
            color = colorBorde
        )
    }
}

@Composable
private fun IconoEstrellaPreguntaKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radioExt = w * 0.44f
        val radioInt = w * 0.21f

        val estrella = Path()
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) radioExt else radioInt
            val angulo = (i * 36.0 - 90.0) * (PI / 180.0)
            val x = cx + (r * cos(angulo)).toFloat()
            val y = cy + (r * sin(angulo)).toFloat()
            if (i == 0) estrella.moveTo(x, y) else estrella.lineTo(x, y)
        }
        estrella.close()

        drawPath(path = estrella, color = Color(0xFFFFCA28))
        drawPath(
            path = estrella,
            color = Color(0xFFE65100),
            style = Stroke(width = w * 0.045f)
        )

        dibujarCaritaMini(
            centroX = cx,
            centroY = cy + h * 0.03f,
            escala = w * 0.18f
        )
    }
}

private fun DrawScope.dibujarCaritaMini(
    centroX: Float,
    centroY: Float,
    escala: Float
) {
    val ojoOffsetX = escala * 0.68f
    val ojoY = centroY - escala * 0.14f
    val radioOjo = escala * 0.22f

    drawCircle(color = Color(0xFF212121), radius = radioOjo, center = Offset(centroX - ojoOffsetX, ojoY))
    drawCircle(color = Color.White, radius = radioOjo * 0.42f, center = Offset(centroX - ojoOffsetX - radioOjo * 0.25f, ojoY - radioOjo * 0.25f))

    drawCircle(color = Color(0xFF212121), radius = radioOjo, center = Offset(centroX + ojoOffsetX, ojoY))
    drawCircle(color = Color.White, radius = radioOjo * 0.42f, center = Offset(centroX + ojoOffsetX - radioOjo * 0.25f, ojoY - radioOjo * 0.25f))

    drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.85f), radius = radioOjo * 0.85f, center = Offset(centroX - ojoOffsetX * 1.25f, centroY + escala * 0.18f))
    drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.85f), radius = radioOjo * 0.85f, center = Offset(centroX + ojoOffsetX * 1.25f, centroY + escala * 0.18f))

    drawArc(
        color = Color(0xFF212121),
        startAngle = 15f,
        sweepAngle = 150f,
        useCenter = false,
        topLeft = Offset(centroX - escala * 0.36f, centroY + escala * 0.04f),
        size = Size(escala * 0.72f, escala * 0.52f),
        style = Stroke(width = escala * 0.16f, cap = StrokeCap.Round)
    )
}

@Preview(name = "Vista Celular - Rendir Evaluación Kawaii", showBackground = true, widthDp = 360, heightDp = 820)
@Composable
fun RendirPruebaScreenPreview() {
    MaterialTheme {
        RendirPruebaContent(
            materiaInicial = "Lenguaje",
            unidadInicial = "Unidad 1",
            intentosRestantes = 2,
            ultimoPuntaje = 0,
            onVolverClick = {},
            onReproducirAudio = {},
            onGuardarIntento = { _, _ -> }
        )
    }
}