package com.example.explora2.ui.screens.alumno

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.BarraNavegacionInferiorContent
import com.example.explora2.ui.components.IconoTrofeoInfantil

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
                                    IconoTrofeoInfantil(modifier = Modifier.size(28.dp))
                                }
                                Column {
                                    Text(
                                        text = "Mis Logros y Avance",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 19.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "2.° Básico · Escuela Marcela Paz",
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
            // 1. TARJETA PRINCIPAL DE AVANCE CON CONEJITO KAWAII
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
                                .border(3.dp, Color(0xFF00897B), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            ConejitoKawaii(modifier = Modifier.size(54.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Vas súper bien! 🌟",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF00695C)
                            )
                            Text(
                                text = "Unidades completadas del plan lector y matemática",
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

            // 2. SECCIÓN DE INSIGNIAS CON ANIMALITOS KAWAII
            Text(
                text = "🐾 Mis Animalitos de Logro",
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
                TarjetaAnimalitoLogro(
                    nombreLogro = "Lector Veloz",
                    detalle = "Lenguaje · U1",
                    colorFondo = Color(0xFFFFF3E0),
                    colorBorde = Color(0xFFFF9800),
                    colorTexto = Color(0xFFE65100),
                    modifier = Modifier.weight(1f)
                ) {
                    ZorritoKawaii(modifier = Modifier.size(52.dp))
                }

                TarjetaAnimalitoLogro(
                    nombreLogro = "Genio Suma",
                    detalle = "Matemática · U1",
                    colorFondo = Color(0xFFE3F2FD),
                    colorBorde = Color(0xFF2196F3),
                    colorTexto = Color(0xFF0D47A1),
                    modifier = Modifier.weight(1f)
                ) {
                    PerritoKawaii(modifier = Modifier.size(52.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaAnimalitoLogro(
                    nombreLogro = "Explorador",
                    detalle = "Ciencias · U1",
                    colorFondo = Color(0xFFF3E5F5),
                    colorBorde = Color(0xFFAB47BC),
                    colorTexto = Color(0xFF6A1B9A),
                    modifier = Modifier.weight(1f)
                ) {
                    GatitoKawaii(modifier = Modifier.size(52.dp))
                }

                TarjetaAnimalitoLogro(
                    nombreLogro = "Gran Meta",
                    detalle = "Próximo desafío",
                    colorFondo = Color(0xFFE8F5E9),
                    colorBorde = Color(0xFF66BB6A),
                    colorTexto = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                ) {
                    ConejitoKawaii(modifier = Modifier.size(52.dp))
                }
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
private fun TarjetaAnimalitoLogro(
    nombreLogro: String,
    detalle: String,
    colorFondo: Color,
    colorBorde: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier,
    animalito: @Composable () -> Unit
) {
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
                    .border(2.dp, colorBorde, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                animalito()
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

// =========================================================================
// ANIMALITOS KAWAII DIBUJADOS PARA LOGROS Y PROGRESO
// =========================================================================

@Composable
private fun ConejitoKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Orejas largas de conejito
        drawOval(color = Color.White, topLeft = Offset(w * 0.27f, h * 0.08f), size = Size(w * 0.16f, h * 0.38f))
        drawOval(color = Color(0xFFF8BBD0), topLeft = Offset(w * 0.30f, h * 0.13f), size = Size(w * 0.10f, h * 0.28f))
        drawOval(color = Color(0xFF78909C), topLeft = Offset(w * 0.27f, h * 0.08f), size = Size(w * 0.16f, h * 0.38f), style = Stroke(w * 0.035f))

        drawOval(color = Color.White, topLeft = Offset(w * 0.57f, h * 0.08f), size = Size(w * 0.16f, h * 0.38f))
        drawOval(color = Color(0xFFF8BBD0), topLeft = Offset(w * 0.60f, h * 0.13f), size = Size(w * 0.10f, h * 0.28f))
        drawOval(color = Color(0xFF78909C), topLeft = Offset(w * 0.57f, h * 0.08f), size = Size(w * 0.16f, h * 0.38f), style = Stroke(w * 0.035f))

        // Carita redonda
        drawCircle(color = Color.White, radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.62f))
        drawCircle(color = Color(0xFF78909C), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.62f), style = Stroke(w * 0.04f))

        // Naricita rosada
        drawCircle(color = Color(0xFFEC407A), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.66f))

        dibujarCaritaAnimalito(w * 0.50f, h * 0.60f, w * 0.22f)
    }
}

@Composable
private fun ZorritoKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Orejitas puntiagudas naranjas
        val orejaIzq = Path().apply {
            moveTo(w * 0.20f, h * 0.44f)
            lineTo(w * 0.24f, h * 0.14f)
            lineTo(w * 0.44f, h * 0.32f)
            close()
        }
        drawPath(orejaIzq, Color(0xFFFF7043))
        drawPath(orejaIzq, Color(0xFFBF360C), style = Stroke(w * 0.035f))

        val orejaDer = Path().apply {
            moveTo(w * 0.80f, h * 0.44f)
            lineTo(w * 0.76f, h * 0.14f)
            lineTo(w * 0.56f, h * 0.32f)
            close()
        }
        drawPath(orejaDer, Color(0xFFFF7043))
        drawPath(orejaDer, Color(0xFFBF360C), style = Stroke(w * 0.035f))

        // Carita naranja
        drawCircle(color = Color(0xFFFF7043), radius = w * 0.34f, center = Offset(w * 0.50f, h * 0.56f))

        // Mejillas blancas abajo
        drawOval(color = Color.White, topLeft = Offset(w * 0.22f, h * 0.54f), size = Size(w * 0.56f, h * 0.32f))

        // Contorno
        drawCircle(color = Color(0xFFBF360C), radius = w * 0.34f, center = Offset(w * 0.50f, h * 0.56f), style = Stroke(w * 0.04f))

        // Naricita negra
        drawCircle(color = Color(0xFF212121), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.62f))

        dibujarCaritaAnimalito(w * 0.50f, h * 0.54f, w * 0.22f)
    }
}

@Composable
private fun PerritoKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Orejitas caídas marrón
        drawRoundRect(color = Color(0xFF6D4C41), topLeft = Offset(w * 0.12f, h * 0.34f), size = Size(w * 0.18f, h * 0.38f), cornerRadius = CornerRadius(w * 0.10f))
        drawRoundRect(color = Color(0xFF6D4C41), topLeft = Offset(w * 0.70f, h * 0.34f), size = Size(w * 0.18f, h * 0.38f), cornerRadius = CornerRadius(w * 0.10f))

        // Cabecita dorada clara
        drawCircle(color = Color(0xFFFFE082), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.52f))
        drawCircle(color = Color(0xFF6D4C41), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.52f), style = Stroke(w * 0.04f))

        // Naricita oscura
        drawCircle(color = Color(0xFF3E2723), radius = w * 0.055f, center = Offset(w * 0.50f, h * 0.58f))

        dibujarCaritaAnimalito(w * 0.50f, h * 0.50f, w * 0.22f)
    }
}

@Composable
private fun GatitoKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Orejitas triangulares lilas/grises
        val orejaIzq = Path().apply {
            moveTo(w * 0.22f, h * 0.44f)
            lineTo(w * 0.26f, h * 0.16f)
            lineTo(w * 0.44f, h * 0.32f)
            close()
        }
        drawPath(orejaIzq, Color(0xFFCE93D8))
        drawPath(orejaIzq, Color(0xFF6A1B9A), style = Stroke(w * 0.035f))

        val orejaDer = Path().apply {
            moveTo(w * 0.78f, h * 0.44f)
            lineTo(w * 0.74f, h * 0.16f)
            lineTo(w * 0.56f, h * 0.32f)
            close()
        }
        drawPath(orejaDer, Color(0xFFCE93D8))
        drawPath(orejaDer, Color(0xFF6A1B9A), style = Stroke(w * 0.035f))

        // Carita
        drawCircle(color = Color(0xFFF3E5F5), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.56f))
        drawCircle(color = Color(0xFF6A1B9A), radius = w * 0.33f, center = Offset(w * 0.50f, h * 0.56f), style = Stroke(w * 0.04f))

        // Bigotitos laterales
        drawLine(color = Color(0xFF6A1B9A), start = Offset(w * 0.14f, h * 0.56f), end = Offset(w * 0.26f, h * 0.58f), strokeWidth = w * 0.03f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFF6A1B9A), start = Offset(w * 0.86f, h * 0.56f), end = Offset(w * 0.74f, h * 0.58f), strokeWidth = w * 0.03f, cap = StrokeCap.Round)

        // Naricita rosada
        drawCircle(color = Color(0xFFEC407A), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.62f))

        dibujarCaritaAnimalito(w * 0.50f, h * 0.54f, w * 0.21f)
    }
}

private fun DrawScope.dibujarCaritaAnimalito(
    centroX: Float,
    centroY: Float,
    escala: Float
) {
    val ojoOffsetX = escala * 0.68f
    val ojoY = centroY - escala * 0.16f
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
        topLeft = Offset(centroX - escala * 0.36f, centroY + escala * 0.06f),
        size = Size(escala * 0.72f, escala * 0.50f),
        style = Stroke(width = escala * 0.16f, cap = StrokeCap.Round)
    )
}

@Preview(
    name = "Vista Celular - Mis Logros Kawaii",
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