package com.example.explora2.ui.screens.alumno

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.explora2.R
import com.example.explora2.model.GestionUiState
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.CompaneroPokemonKawaii
import com.example.explora2.viewmodel.PokemonViewModel
import com.example.explora2.model.ConsultaAlumnoUiState

private fun reproducirCryPikachu(
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
fun PanelAlumnoScreen(
    navController: NavController,
    gestionUiState: GestionUiState,
    pokemonViewModel: PokemonViewModel = viewModel(),
    consultaAlumno: ConsultaAlumnoUiState =
        ConsultaAlumnoUiState(cargando = false),
    onActualizarActividades: () -> Unit = {},
    onAbrirActividad: (Long) -> Unit = {}
) {

    val tieneActividad = gestionUiState.autorizado && gestionUiState.asignadoAlumno
    val colorBordeNegro = Color(0xFF1E1E1E)

    // Estado para mostrar a Pikachu expandido dando la bienvenida a toda pantalla
    var mostrarBienvenidaPikachu by remember { mutableStateOf(true) }
    var yaSonoAlPasarDedo by remember { mutableStateOf(false) }
    var reproductorActivo by remember { mutableStateOf<MediaPlayer?>(null) }

    val urlCryPikachu = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/25.ogg"

    DisposableEffect(Unit) {
        onDispose {
            reproductorActivo?.runCatching {
                if (isPlaying) stop()
                release()
            }
            reproductorActivo = null
        }
    }

    LaunchedEffect(Unit) {
        pokemonViewModel.cargarPokemonEspecifico(25, " ¡Elige una asignatura para comenzar!")
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
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.gorra),
                                        contentDescription = "Avatar Gorra",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.White, CircleShape)
                                    )
                                    Column {
                                        Text(
                                            text = "Mi Espacio",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 19.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "2.° Básico · Explora2",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = Color(0xFFFFF8E1)
                                        )
                                    }
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        navController.navigate(Screen.SelectorPerfil.route)
                                    },
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.92f))
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver a perfiles",
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
                BarraNavegacionInferior(
                    navController = navController,
                    rutaActual = Screen.PanelEstudiante.route
                )
            }
        ) { espacioInterior ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(espacioInterior)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                CompaneroPokemonKawaii(pokemonViewModel = pokemonViewModel)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(3.dp, Color(0xFF66BB6A), RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .shadow(4.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(3.dp, Color(0xFF43A047), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.gorra),
                                contentDescription = "Avatar Gorra Explorador",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Hola, Explorador/a! ✨",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B5E20)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Toca una tarjeta para jugar y aprender",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Card(
                    onClick = { navController.navigate(Screen.Asignaturas.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(3.dp, Color(0xFF1565C0), RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF29B6F6)),
                    elevation = CardDefaults.cardElevation(6.dp)
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
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, Color(0xFF0D47A1), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ultra),
                                    contentDescription = "Mis Asignaturas",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Text(
                                text = "Mis Asignaturas",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "Lenguaje, Matemática, Ciencias Naturales e Historia.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE1F5FE)
                        )

                        Button(
                            onClick = { navController.navigate(Screen.Asignaturas.route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF0277BD)
                            )
                        ) {
                            Text(
                                text = "¡Explorar Asignaturas! 🚀",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(3.dp, Color(0xFFAB47BC), RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
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
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, Color(0xFF8E24AA), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.master),
                                    contentDescription = "Desafío de Lectura",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Text(
                                text = "Desafío de Lectura",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF4A148C)
                            )
                        }

                        Text(
                            text = "Escucha las preguntas, responde y demuestra cuánto sabes.",
                            fontSize = 15.sp,
                            color = Color(0xFF6A1B9A)
                        )

                        Button(
                            onClick = { navController.navigate(Screen.RendirPrueba.route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Rendir Evaluación 🎯",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(3.dp, Color(0xFFFFA726), RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.dp, Color(0xFFFB8C00), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                IconoPokeBallKawaii(modifier = Modifier.size(38.dp))
                            }
                            Text(
                                text = "Tarea del Profe",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE65100)
                            )
                        }


                        OutlinedButton(
                            onClick = onActualizarActividades,
                            enabled = !consultaAlumno.cargando &&
                                    !consultaAlumno.enviando,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Actualizar actividades")
                        }

                        if (consultaAlumno.cargando) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        consultaAlumno.error?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (!consultaAlumno.cargando &&
                            consultaAlumno.error == null &&
                            consultaAlumno.actividades.isEmpty()
                        ) {
                            Text(
                                text = "Todavía no tienes tareas asignadas.",
                                fontSize = 15.sp
                            )
                        }

                        consultaAlumno.actividades.forEach { plan ->

                            HorizontalDivider(
                                color = Color(0xFFFFCC80)
                            )

                            Text(
                                text = "#${plan.id} · ${plan.titulo.orEmpty()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text("Asignatura: ${plan.asignatura.orEmpty()}")
                            Text("Unidad: ${plan.unidad.orEmpty()}")
                            Text("Objetivo: ${plan.objetivo.orEmpty()}")

                            Text(
                                text = if (plan.respuestaEntregada)
                                    "✅ Actividad entregada"
                                else
                                    "⏳ Pendiente de entrega",
                                fontWeight = FontWeight.Bold,
                                color = if (plan.respuestaEntregada)
                                    Color(0xFF2E7D32)
                                else
                                    Color(0xFFE65100)
                            )

                            Button(
                                onClick = {
                                    plan.id?.let(onAbrirActividad)
                                },
                                enabled = plan.id != null &&
                                        !consultaAlumno.cargando &&
                                        !consultaAlumno.enviando,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800)
                                )
                            ) {
                                Text(
                                    text = if (plan.respuestaEntregada)
                                        "Ver mi respuesta"
                                    else
                                        "Abrir actividad 🌟",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                            // Conserva el acceso a las actividades de práctica local.
                        if (tieneActividad && gestionUiState.idRemoto == null) {
                            HorizontalDivider()

                            Text("Actividad de práctica local")

                            Button(
                                onClick = {
                                    navController.navigate(
                                        Screen.ActividadAsignada.route
                                    )
                                }
                            ) {
                                Text("Abrir práctica local")
                            }
                        }

                    }
                }
            }
        }

        // BIENVENIDA EXPANDIDA CON PIKACHU (ID 25) + CRY AL PASAR EL DEDO
        if (mostrarBienvenidaPikachu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFFDE7),
                                Color(0xFFFFF8E1)
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
                        text = "¡Pika-hola!",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100),
                        textAlign = TextAlign.Center
                    )

                    AsyncImage(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
                        placeholder = painterResource(id = R.drawable.poke),
                        error = painterResource(id = R.drawable.poke),
                        contentDescription = "Pikachu dando la bienvenida",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(290.dp)
                            .pointerInput(urlCryPikachu) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        when (event.type) {
                                            PointerEventType.Press, PointerEventType.Move -> {
                                                if (!yaSonoAlPasarDedo) {
                                                    yaSonoAlPasarDedo = true
                                                    reproducirCryPikachu(urlCryPikachu) { mp ->
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
                        text = "¡Bienvenido/a ! ⚡",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { mostrarBienvenidaPikachu = false },
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
                            text = "¡Comenzar a explorar! 🚀",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconoPokeBallKawaii(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, Color(0xFFE65100), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.poke),
            contentDescription = "Pokéball",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(name = "Panel Alumno Kawaii Unico", showBackground = true, widthDp = 360, heightDp = 820)
@Composable
fun PanelAlumnoScreenUnicoPreview() {
    MaterialTheme {
        PanelAlumnoScreen(
            navController = rememberNavController(),
            gestionUiState = GestionUiState()
        )
    }
}