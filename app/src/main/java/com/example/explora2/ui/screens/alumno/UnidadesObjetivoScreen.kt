package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.IconoLibroInfantil
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class ObjetivoItem(
    val id: String,
    val unidad: String,
    val titulo: String,
    val descripcion: String,
    val asignatura: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnidadesObjetivoScreen(navController: NavController) {
    val listaObjetivos = listOf(
        // Matemática
        ObjetivoItem("OA 08", "Unidad 1", "Sumas y Restas hasta 100", "Resolver adiciones y sustracciones con números de dos dígitos.", "Matemática"),
        ObjetivoItem("OA 01", "Unidad 2", "Conteo y Patrones hasta 1000", "Contar números del 0 al 1000 de 5 en 5 y 10 en 10.", "Matemática"),

        // Lenguaje y Comunicación
        ObjetivoItem("OA 03", "Unidad 1", "Lectura Fluida y Fábulas", "Leer en voz alta palabras frecuentes y comprender fábulas breves.", "Lenguaje"),
        ObjetivoItem("OA 12", "Unidad 2", "Escritura Guiada y Signos", "Escribir oraciones legibles utilizando mayúsculas y punto final.", "Lenguaje"),

        // Ciencias Naturales
        ObjetivoItem("OA 01", "Unidad 1", "El Cuerpo y Órganos Vitales", "Identificar la función de órganos vitales como el corazón y pulmones.", "Ciencias"),
        ObjetivoItem("OA 05", "Unidad 2", "Animales Nativos y Hábitat", "Observar e identificar animales nativos de Chile y su entorno.", "Ciencias"),

        // Historia, Geografía y Cs. Sociales
        ObjetivoItem("OA 06", "Unidad 1", "Nuestra Comunidad y Servicios", "Reconocer profesiones, oficios y servicios comunitarios.", "Historia"),
        ObjetivoItem("OA 08", "Unidad 2", "Pueblos Originarios de Chile", "Conocer costumbres y ubicación de los pueblos originarios.", "Historia")
    )

    val materiaRecibida = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("materia_seleccionada") ?: "Lenguaje"

    var materiaSeleccionada by remember { mutableStateOf(materiaRecibida) }
    val categorias = listOf("Lenguaje", "Matemática", "Ciencias", "Historia")

    // Borde negro estilo caricatura kawaii y fondo pastel por asignatura
    val colorBordeNegro = Color(0xFF1E1E1E)
    val (colorFondoTarjeta, colorTextoTarjeta) = when (materiaSeleccionada) {
        "Lenguaje" -> Pair(Color(0xFFE0F2F1), Color(0xFF00695C))
        "Matemática" -> Pair(Color(0xFFE3F2FD), Color(0xFF0D47A1))
        "Ciencias" -> Pair(Color(0xFFE8F5E9), Color(0xFF1B5E20))
        else -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
    }

    Scaffold(
        containerColor = Color(0xFFFFFDE7), // Fondo crema pastel infantil
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
                                    IconoLibroInfantil(modifier = Modifier.size(28.dp))
                                }
                                Column {
                                    Text(
                                        text = "Unidades · $materiaSeleccionada",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 19.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "2.° Básico · Elige tu aventura",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = Color(0xFFFFF8E1)
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.92f))
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver a Asignaturas",
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(22.dp)
                                )
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
                rutaActual = Screen.Asignaturas.route
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "✨ Explora por asignatura:",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5D4037),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // PÍLDORAS DE ASIGNATURA ESTILO KAWAII CON BORDE NEGRO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { materia ->
                    val seleccionada = materiaSeleccionada == materia
                    val emojiMateria = when (materia) {
                        "Lenguaje" -> "📖"
                        "Matemática" -> "🔢"
                        "Ciencias" -> "🌱"
                        else -> "🌎"
                    }
                    FilterChip(
                        selected = seleccionada,
                        onClick = { materiaSeleccionada = materia },
                        shape = RoundedCornerShape(18.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFF7043),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF1E1E1E)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = seleccionada,
                            borderColor = colorBordeNegro,
                            borderWidth = 2.dp
                        ),
                        label = {
                            Text(
                                text = "$emojiMateria $materia",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    )
                }
            }

            // LISTA DE UNIDADES CON BORDE NEGRO EN LAS CARDS
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                val unidadesFiltradas = listaObjetivos.filter { it.asignatura == materiaSeleccionada }

                items(unidadesFiltradas) { item ->
                    Card(
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("materia", item.asignatura)
                            navController.currentBackStackEntry?.savedStateHandle?.set("unidad", item.unidad)
                            navController.navigate(Screen.RendirPrueba.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(3.dp, colorBordeNegro, RoundedCornerShape(26.dp)),
                        shape = RoundedCornerShape(26.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = colorFondoTarjeta)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Burbuja con borde negro y personaje kawaii
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .shadow(3.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(2.5.dp, colorBordeNegro, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    PersonajeUnidadKawaii(
                                        asignatura = item.asignatura,
                                        unidad = item.unidad,
                                        modifier = Modifier.size(42.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFFF7043)
                                    ) {
                                        Text(
                                            text = "${item.unidad} · ${item.id}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.titulo,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = colorTextoTarjeta
                                    )
                                }
                            }

                            Text(
                                text = item.descripcion,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF334155)
                            )

                            // Botón visual inferior con borde negro
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(2.dp, colorBordeNegro, RoundedCornerShape(14.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "🎯 ¡Jugar desafío de ${item.unidad}!",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF1E1E1E)
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color(0xFF1E1E1E)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// PERSONAJES KAWAII TEMÁTICOS PARA CADA UNIDAD Y OBJETIVO
// =========================================================================

@Composable
private fun PersonajeUnidadKawaii(
    asignatura: String,
    unidad: String,
    modifier: Modifier = Modifier
) {
    when {
        asignatura == "Lenguaje" && unidad == "Unidad 1" -> DibujoLeoncitoFabulaKawaii(modifier)
        asignatura == "Lenguaje" -> DibujoLapicitoKawaii(modifier)
        asignatura == "Matemática" && unidad == "Unidad 1" -> DibujoNumeroKawaii(texto = "+100", modifier = modifier)
        asignatura == "Matemática" -> DibujoNumeroKawaii(texto = "10·20", modifier = modifier)
        asignatura == "Ciencias" && unidad == "Unidad 1" -> DibujoCorazonUnidadKawaii(modifier)
        asignatura == "Ciencias" -> DibujoCondoritoUnidadKawaii(modifier)
        asignatura == "Historia" && unidad == "Unidad 1" -> DibujoCascoComunidadKawaii(modifier)
        else -> DibujoSolOriginarioKawaii(modifier)
    }
}

@Composable
private fun DibujoLeoncitoFabulaKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFFFF8F00), radius = w * 0.42f, center = Offset(w * 0.50f, h * 0.50f))
        drawCircle(color = Color(0xFFFFCA28), radius = w * 0.30f, center = Offset(w * 0.50f, h * 0.50f))
        dibujarCaritaUnidad(w * 0.50f, h * 0.50f, w * 0.20f)
    }
}

@Composable
private fun DibujoLapicitoKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFFF48FB1), topLeft = Offset(w * 0.34f, h * 0.14f), size = Size(w * 0.32f, h * 0.16f), cornerRadius = CornerRadius(w * 0.08f))
        drawRect(color = Color(0xFFFFCA28), topLeft = Offset(w * 0.34f, h * 0.28f), size = Size(w * 0.32f, h * 0.42f))
        val punta = Path().apply {
            moveTo(w * 0.34f, h * 0.70f)
            lineTo(w * 0.66f, h * 0.70f)
            lineTo(w * 0.50f, h * 0.88f)
            close()
        }
        drawPath(punta, Color(0xFFFFE0B2))
        dibujarCaritaUnidad(w * 0.50f, h * 0.48f, w * 0.15f)
    }
}

@Composable
private fun DibujoNumeroKawaii(texto: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFFBBDEFB)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            color = Color(0xFF0D47A1)
        )
    }
}

@Composable
private fun DibujoCorazonUnidadKawaii(modifier: Modifier) {
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
        dibujarCaritaUnidad(w * 0.50f, h * 0.52f, w * 0.18f)
    }
}

@Composable
private fun DibujoCondoritoUnidadKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(color = Color(0xFFECEFF1), topLeft = Offset(w * 0.22f, h * 0.62f), size = Size(w * 0.56f, h * 0.18f), cornerRadius = CornerRadius(w * 0.09f))
        drawCircle(color = Color(0xFF37474F), radius = w * 0.31f, center = Offset(w * 0.50f, h * 0.44f))
        dibujarCaritaUnidad(w * 0.50f, h * 0.44f, w * 0.19f)
    }
}

@Composable
private fun DibujoCascoComunidadKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawCircle(color = Color(0xFFE53935), radius = w * 0.32f, center = Offset(w * 0.50f, h * 0.50f))
        drawRoundRect(color = Color(0xFFB71C1C), topLeft = Offset(w * 0.14f, h * 0.58f), size = Size(w * 0.72f, h * 0.14f), cornerRadius = CornerRadius(w * 0.07f))
        dibujarCaritaUnidad(w * 0.50f, h * 0.50f, w * 0.17f)
    }
}

@Composable
private fun DibujoSolOriginarioKawaii(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        for (i in 0 until 8) {
            val ang = (i * 45.0) * (PI / 180.0)
            drawCircle(
                color = Color(0xFFFFB300),
                radius = w * 0.07f,
                center = Offset(cx + (w * 0.42f * cos(ang)).toFloat(), cy + (w * 0.42f * sin(ang)).toFloat())
            )
        }
        drawCircle(color = Color(0xFFFFCA28), radius = w * 0.32f, center = Offset(cx, cy))
        dibujarCaritaUnidad(cx, cy + h * 0.02f, w * 0.17f)
    }
}

private fun DrawScope.dibujarCaritaUnidad(
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

@Preview(name = "Vista Celular - Unidades Kawaii", showBackground = true, widthDp = 360, heightDp = 820)
@Composable
fun UnidadesObjetivoScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        UnidadesObjetivoScreen(navController = navController)
    }
}