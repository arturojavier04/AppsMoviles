package com.example.explora2.ui.screens.alumno

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.explora2.R
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferiorContent
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private fun reproducirCryCharmander(
    urlAudio: String,
    onPlayerCreated: (MediaPlayer) -> Unit
) {
    try {
        val player = MediaPlayer().apply {
            setDataSource(urlAudio)
            setOnPreparedListener {
                onPlayerCreated(it)
                it.start()
            }
            setOnCompletionListener {
                it.release()
            }
            setOnErrorListener { mp, _, _ ->
                mp.release()
                true
            }
            prepareAsync()
        }
        onPlayerCreated(player)
    } catch (_: Exception) {
        // Evita fallos si no hay conexión o el dispositivo no puede reproducir el stream
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadLecturaScreen(
    titulo: String,
    instrucciones: String,
    respuestaEstudiante: String,
    errorRespuesta: String?,
    respuestaEntregada: Boolean,
    onRespuestaChange: (String) -> Unit,
    onEnviarClick: () -> Unit,
    onVolverClick: () -> Unit
) {
    val colorBordeNegro = Color(0xFF1E1E1E)
    var mostrarBienvenidaCharmander by remember { mutableStateOf(true) }
    var yaSonoAlPasarDedo by remember { mutableStateOf(false) }
    var reproductorActivo by remember { mutableStateOf<MediaPlayer?>(null) }

    val urlCryCharmander = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/4.ogg"

    DisposableEffect(Unit) {
        onDispose {
            reproductorActivo?.runCatching {
                if (isPlaying) stop()
                release()
            }
            reproductorActivo = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                                        Color(0xFFFF7043),
                                        Color(0xFFFFB300)
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
                                    Image(
                                        painter = painterResource(id = R.drawable.poke),
                                        contentDescription = "Pokéball",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.White, CircleShape)
                                    )
                                    Column {
                                        Text(
                                            text = "Mi Actividad Escolar",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 19.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "2.° Básico · Tarea del Profe",
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
                                        contentDescription = "Volver al Panel",
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            actions = {
                                Surface(
                                    shape = RoundedCornerShape(18.dp),
                                    color = Color.White.copy(alpha = 0.92f),
                                    modifier = Modifier.padding(end = 12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.poke),
                                            contentDescription = "Pokéball",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.ultra),
                                            contentDescription = "Ultra Ball",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.master),
                                            contentDescription = "Master Ball",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
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
            bottomBar = {
                BarraNavegacionInferiorContent(
                    rutaActual = Screen.PanelEstudiante.route,
                    onInicioClick = onVolverClick
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. TARJETA CON EL TÍTULO E INSTRUCCIONES
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(3.dp, Color(0xFFFF9800), RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.5.dp, Color(0xFFFF9800), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                ObjetoCuadernoKawaii(modifier = Modifier.size(40.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFFF7043)
                                ) {
                                    Text(
                                        text = "📖 Lectura y Comprensión",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = titulo,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFFFFCC80))

                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, Color(0xFFFFB74D), RoundedCornerShape(18.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "✨ Instrucciones:",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFEF6C00)
                                )
                                Text(
                                    text = instrucciones,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF3E2723)
                                )
                            }
                        }
                    }
                }

                // 2. TARJETA DE ESCRITURA
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(3.dp, Color(0xFF29B6F6), RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE1F5FE))
                                    .border(2.dp, Color(0xFF0288D1), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                ObjetoLapizActividadKawaii(modifier = Modifier.size(36.dp))
                            }
                            Text(
                                text = "Tu Respuesta",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF01579B)
                            )
                        }

                        OutlinedTextField(
                            value = respuestaEstudiante,
                            onValueChange = onRespuestaChange,
                            label = {
                                Text(
                                    text = "✏️ Escribe aquí tu respuesta...",
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            isError = errorRespuesta != null,
                            supportingText = {
                                if (errorRespuesta != null) {
                                    Text(
                                        text = "⚠️ $errorRespuesta",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            enabled = !respuestaEntregada,
                            shape = RoundedCornerShape(18.dp),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0288D1),
                                unfocusedBorderColor = Color(0xFF81D4FA),
                                focusedContainerColor = Color(0xFFF8FDFF),
                                unfocusedContainerColor = Color(0xFFF8FDFF)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = onEnviarClick,
                            enabled = !respuestaEntregada,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF43A047),
                                disabledContainerColor = Color(0xFFC8E6C9)
                            )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Entregar Respuesta 🚀",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // 3. TARJETA DE CONFIRMACIÓN KAWAII CON MASTER BALL
                if (respuestaEntregada) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(3.dp, Color(0xFF43A047), RoundedCornerShape(26.dp)),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, Color(0xFF43A047), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.master),
                                    contentDescription = "Master Ball",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Text(
                                text = "🎉 ¡Muy bien! Tu respuesta fue entregada al profe.",
                                color = Color(0xFF1B5E20),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // PANTALLA COMPLETA DE BIENVENIDA CON CHARMANDER (ID 4) EXPANDIDO + CRY AL PASAR EL DEDO
        if (mostrarBienvenidaCharmander) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFFDE7),
                                Color(0xFFFFF3E0)
                            )
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "¡A leer y responder!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100),
                        textAlign = TextAlign.Center
                    )

                    AsyncImage(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
                        placeholder = painterResource(id = R.drawable.poke),
                        error = painterResource(id = R.drawable.poke),
                        contentDescription = "Charmander acompañando la lectura",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(290.dp)
                            .pointerInput(urlCryCharmander) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        when (event.type) {
                                            PointerEventType.Press, PointerEventType.Move -> {
                                                if (!yaSonoAlPasarDedo) {
                                                    yaSonoAlPasarDedo = true
                                                    reproducirCryCharmander(urlCryCharmander) { mp ->
                                                        reproductorActivo = mp
                                                    }
                                                }
                                            }
                                            PointerEventType.Release, PointerEventType.Exit -> {
                                                yaSonoAlPasarDedo = false
                                            }
                                        }
                                    }
                                }
                            },
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "¡Pasa el dedo sobre Charmander para oírlo! 🔥",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { mostrarBienvenidaCharmander = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(3.dp, colorBordeNegro),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB300),
                            contentColor = colorBordeNegro
                        )
                    ) {
                        Text(
                            text = "¡Comenzar actividad! 📖",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// OBJETOS ESCOLARES KAWAII
// =========================================================================

@Composable
private fun ObjetoCuadernoKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color(0xFFFF7043),
            topLeft = Offset(w * 0.22f, h * 0.14f),
            size = Size(w * 0.60f, h * 0.72f),
            cornerRadius = CornerRadius(w * 0.12f)
        )
        drawRoundRect(
            color = Color(0xFFBF360C),
            topLeft = Offset(w * 0.22f, h * 0.14f),
            size = Size(w * 0.60f, h * 0.72f),
            cornerRadius = CornerRadius(w * 0.12f),
            style = Stroke(width = w * 0.045f)
        )

        for (i in 0 until 4) {
            val y = h * (0.26f + i * 0.16f)
            drawRoundRect(
                color = Color(0xFFFFCA28),
                topLeft = Offset(w * 0.14f, y),
                size = Size(w * 0.14f, h * 0.06f),
                cornerRadius = CornerRadius(w * 0.03f)
            )
        }

        dibujarCaritaObjeto(w * 0.54f, h * 0.50f, w * 0.20f)
    }
}

@Composable
private fun ObjetoLapizActividadKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color(0xFFF48FB1),
            topLeft = Offset(w * 0.34f, h * 0.12f),
            size = Size(w * 0.32f, h * 0.16f),
            cornerRadius = CornerRadius(w * 0.08f)
        )
        drawRect(
            color = Color(0xFF90CAF9),
            topLeft = Offset(w * 0.34f, h * 0.25f),
            size = Size(w * 0.32f, h * 0.06f)
        )
        drawRect(
            color = Color(0xFFFFCA28),
            topLeft = Offset(w * 0.34f, h * 0.31f),
            size = Size(w * 0.32f, h * 0.40f)
        )
        val punta = Path().apply {
            moveTo(w * 0.34f, h * 0.71f)
            lineTo(w * 0.66f, h * 0.71f)
            lineTo(w * 0.50f, h * 0.90f)
            close()
        }
        drawPath(punta, Color(0xFFFFE0B2))

        val mina = Path().apply {
            moveTo(w * 0.45f, h * 0.84f)
            lineTo(w * 0.55f, h * 0.84f)
            lineTo(w * 0.50f, h * 0.90f)
            close()
        }
        drawPath(mina, Color(0xFF37474F))

        dibujarCaritaObjeto(w * 0.50f, h * 0.50f, w * 0.15f)
    }
}

private fun DrawScope.dibujarCaritaObjeto(
    centroX: Float,
    centroY: Float,
    escala: Float
) {
    val ojoOffsetX = escala * 0.68f
    val ojoY = centroY - escala * 0.15f
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

@Preview(name = "Vista Celular - Actividad Lectura Kawaii", showBackground = true, widthDp = 360, heightDp = 820)
@Composable
fun ActividadLecturaScreenPreview() {
    MaterialTheme {
        ActividadLecturaScreen(
            titulo = "El perro y su pelota",
            instrucciones = "Explica qué hizo el perro.",
            respuestaEstudiante = "",
            errorRespuesta = null,
            respuestaEntregada = false,
            onRespuestaChange = {},
            onEnviarClick = {},
            onVolverClick = {}
        )
    }
}