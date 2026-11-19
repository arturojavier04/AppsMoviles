package com.example.explora2.ui.screens.alumno

import android.media.MediaPlayer
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.explora2.R
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.BarraNavegacionInferiorContent

private fun reproducirCryLogro(
    pokemonId: Int,
    onPlayerCreated: (MediaPlayer) -> Unit
) {
    val urlAudio = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/$pokemonId.ogg"
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

@Composable
private fun Modifier.sonidoAlPasarDedo(
    pokemonId: Int,
    onPlayerCreated: (MediaPlayer) -> Unit
): Modifier {
    var yaSonoEnEsteToque by remember(pokemonId) { mutableStateOf(false) }
    return this.pointerInput(pokemonId) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                when (event.type) {
                    PointerEventType.Press, PointerEventType.Move -> {
                        if (!yaSonoEnEsteToque) {
                            yaSonoEnEsteToque = true
                            reproducirCryLogro(pokemonId, onPlayerCreated)
                        }
                    }
                    PointerEventType.Release, PointerEventType.Exit -> {
                        yaSonoEnEsteToque = false
                    }
                }
            }
        }
    }
}

// Sobrecarga para AppNavigation (mantiene navController opcional)
@Composable
fun ProgresoEstudianteScreen(navController: NavController? = null) {
    ProgresoEstudianteContent(
        onVolverClick = { navController?.popBackStack() },
        bottomBar = {
            if (navController != null) {
                BarraNavegacionInferior(
                    navController = navController,
                    rutaActual = Screen.ProgresoAlumno.route
                )
            } else {
                BarraNavegacionInferiorContent(rutaActual = Screen.ProgresoAlumno.route)
            }
        }
    )
}

// Vista desacoplada de navController para Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgresoEstudianteContent(
    onVolverClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {
        BarraNavegacionInferiorContent(rutaActual = Screen.ProgresoAlumno.route)
    }
) {
    var reproductorActivo by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            reproductorActivo?.runCatching {
                if (isPlaying) stop()
                release()
            }
            reproductorActivo = null
        }
    }

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
                                    painter = painterResource(id = R.drawable.poke),
                                    contentDescription = "Pokéball",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.White, CircleShape)
                                )
                                Column {
                                    Text(
                                        text = "Mis Logros",
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
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. TARJETA PRINCIPAL DE AVANCE CON EEVEE (ID 133)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(3.dp, Color(0xFF26A69A), RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .shadow(3.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(3.dp, Color(0xFF00897B), CircleShape)
                                .sonidoAlPasarDedo(133) { mp -> reproductorActivo = mp },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/133.png",
                                placeholder = painterResource(id = R.drawable.poke),
                                error = painterResource(id = R.drawable.poke),
                                contentDescription = "Eevee",
                                modifier = Modifier.size(54.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Vas super bien!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF00695C)
                            )
                            Text(
                                text = "Pasa el dedo sobre tus Pokémon para oírlos",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF004D40)
                            )
                        }
                    }

                    // Barra de progreso gruesa y redondeada
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(22.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, Color(0xFF00897B), CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF26A69A), Color(0xFF66BB6A))
                                    )
                                )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        modifier = Modifier.border(2.dp, Color(0xFF80CBC4), RoundedCornerShape(14.dp))
                    ) {
                        Text(
                            text = "🎯 65% completado · ¡Sigue así!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF00695C),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 2. SECCIÓN DE INSIGNIAS CON POKÉMON DE LA API REST
            Text(
                text = "🐾 Mis Logros Pokemon",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5D4037),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Fennekin (ID 653)
                TarjetaPokemonApiLogro(
                    nombreLogro = "Fennekin",
                    detalle = "Lenguaje · U1",
                    pokemonId = 653,
                    colorFondo = Color(0xFFFFF3E0),
                    colorBorde = Color(0xFFFF9800),
                    colorTexto = Color(0xFFE65100),
                    onPlayerCreated = { mp -> reproductorActivo = mp },
                    modifier = Modifier.weight(1f)
                )

                // Froakie (ID 656)
                TarjetaPokemonApiLogro(
                    nombreLogro = "Froakie",
                    detalle = "Matemática · U1",
                    pokemonId = 656,
                    colorFondo = Color(0xFFE3F2FD),
                    colorBorde = Color(0xFF2196F3),
                    colorTexto = Color(0xFF0D47A1),
                    onPlayerCreated = { mp -> reproductorActivo = mp },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Drilbur (ID 529)
                TarjetaPokemonApiLogro(
                    nombreLogro = "Drilbur",
                    detalle = "Ciencias · U1",
                    pokemonId = 529,
                    colorFondo = Color(0xFFF3E5F5),
                    colorBorde = Color(0xFFAB47BC),
                    colorTexto = Color(0xFF6A1B9A),
                    onPlayerCreated = { mp -> reproductorActivo = mp },
                    modifier = Modifier.weight(1f)
                )

                // Teddiursa (ID 216)
                TarjetaPokemonApiLogro(
                    nombreLogro = "Teddiursa",
                    detalle = "Gran Meta",
                    pokemonId = 216,
                    colorFondo = Color(0xFFE8F5E9),
                    colorBorde = Color(0xFF66BB6A),
                    colorTexto = Color(0xFF2E7D32),
                    onPlayerCreated = { mp -> reproductorActivo = mp },
                    modifier = Modifier.weight(1f)
                )
            }

            // 3. BOTÓN DE REGRESO KAWAII
            Button(
                onClick = onVolverClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
            ) {
                Text(
                    text = "Volver a Mi Espacio 🏡",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun TarjetaPokemonApiLogro(
    nombreLogro: String,
    detalle: String,
    pokemonId: Int,
    colorFondo: Color,
    colorBorde: Color,
    colorTexto: Color,
    onPlayerCreated: (MediaPlayer) -> Unit,
    modifier: Modifier = Modifier
) {
    val spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"

    Card(
        modifier = modifier.border(3.dp, colorBorde, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(3.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, colorBorde, CircleShape)
                    .sonidoAlPasarDedo(pokemonId, onPlayerCreated),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = spriteUrl,
                    placeholder = painterResource(id = R.drawable.poke),
                    error = painterResource(id = R.drawable.poke),
                    contentDescription = nombreLogro,
                    modifier = Modifier.size(52.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = nombreLogro,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorTexto,
                textAlign = TextAlign.Center
            )
            Text(
                text = detalle,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF455A64),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    name = "Vista Celular - Mis Logros Con API REST",
    showBackground = true,
    widthDp = 360,
    heightDp = 820
)
@Composable
fun ProgresoEstudianteScreenPreview() {
    MaterialTheme {
        ProgresoEstudianteContent(onVolverClick = {})
    }
}