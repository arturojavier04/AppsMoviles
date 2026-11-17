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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.BarraNavegacionInferiorContent
import com.example.explora2.ui.components.IconoLibroInfantil

@Composable
fun AsignaturasScreen(navController: NavController) {
    fun navegarAUnidades(materia: String) {
        navController.currentBackStackEntry?.savedStateHandle?.set("materia_seleccionada", materia)
        navController.navigate(Screen.UnidadesObjetivos.route)
    }

    AsignaturasContent(
        onVolverClick = { navController.popBackStack() },
        onLenguajeClick = { navegarAUnidades("Lenguaje") },
        onMatematicaClick = { navegarAUnidades("Matemática") },
        onCienciasClick = { navegarAUnidades("Ciencias") },
        onHistoriaClick = { navegarAUnidades("Historia") },
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
fun AsignaturasContent(
    onVolverClick: () -> Unit,
    onLenguajeClick: () -> Unit,
    onMatematicaClick: () -> Unit,
    onCienciasClick: () -> Unit,
    onHistoriaClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {
        BarraNavegacionInferiorContent(rutaActual = Screen.Asignaturas.route)
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
                                    IconoLibroInfantil(modifier = Modifier.size(28.dp))
                                }
                                Column {
                                    Text(
                                        text = "Mis Asignaturas",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
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
                                    contentDescription = "Volver",
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
    ) { espacioInterior ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterior)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "✨ Elige un ramo para jugar y aprender:",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5D4037),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // 1. LENGUAJE Y COMUNICACIÓN (CON BORDE AMARILLO BRILLANTE)
            TarjetaMateriaKawaii(
                titulo = "Lenguaje y Comunicación",
                descripcion = "Lectura fluida, fábulas y comprensión lectora",
                colorFondo = Color(0xFFE0F2F1),
                colorBorde = Color(0xFFFFD600),
                colorTexto = Color(0xFF00695C),
                onClick = onLenguajeClick
            ) {
                IconoLibroInfantil(modifier = Modifier.size(42.dp))
            }

            // 2. MATEMÁTICA
            TarjetaMateriaKawaii(
                titulo = "Matemática",
                descripcion = "Números, sumas y restas hasta el 100",
                colorFondo = Color(0xFFE3F2FD),
                colorBorde = Color(0xFF1E88E5),
                colorTexto = Color(0xFF0D47A1),
                onClick = onMatematicaClick
            ) {
                IconoMateKawaii(modifier = Modifier.size(42.dp))
            }

            // 3. CIENCIAS NATURALES
            TarjetaMateriaKawaii(
                titulo = "Ciencias Naturales",
                descripcion = "Seres vivos, el cuerpo humano y su hábitat",
                colorFondo = Color(0xFFE8F5E9),
                colorBorde = Color(0xFF43A047),
                colorTexto = Color(0xFF1B5E20),
                onClick = onCienciasClick
            ) {
                IconoCienciasKawaii(modifier = Modifier.size(42.dp))
            }

            // 4. HISTORIA Y GEOGRAFÍA
            TarjetaMateriaKawaii(
                titulo = "Historia y Geografía",
                descripcion = "Nuestra comunidad, pueblos y paisajes de Chile",
                colorFondo = Color(0xFFFFF8E1),
                colorBorde = Color(0xFFFB8C00),
                colorTexto = Color(0xFFE65100),
                onClick = onHistoriaClick
            ) {
                IconoHistoriaKawaii(modifier = Modifier.size(42.dp))
            }
        }
    }
}

@Composable
fun TarjetaMateriaKawaii(
    titulo: String,
    descripcion: String,
    colorFondo: Color,
    colorBorde: Color,
    colorTexto: Color,
    onClick: () -> Unit,
    iconoKawaii: @Composable () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(3.dp, colorBorde, RoundedCornerShape(26.dp)),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .shadow(3.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.5.dp, colorBorde, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                iconoKawaii()
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorTexto
                )
                Text(
                    text = descripcion,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colorTexto,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// =========================================================================
// PERSONAJES KAWAII PARA CADA ASIGNATURA
// =========================================================================

@Composable
private fun IconoMateKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Cuerpo de calculadorita azul
        drawRoundRect(
            color = Color(0xFF42A5F5),
            topLeft = Offset(w * 0.20f, h * 0.14f),
            size = Size(w * 0.60f, h * 0.72f),
            cornerRadius = CornerRadius(w * 0.14f)
        )
        drawRoundRect(
            color = Color(0xFF0D47A1),
            topLeft = Offset(w * 0.20f, h * 0.14f),
            size = Size(w * 0.60f, h * 0.72f),
            cornerRadius = CornerRadius(w * 0.14f),
            style = Stroke(width = w * 0.045f)
        )

        // Pantallita superior celeste claro
        drawRoundRect(
            color = Color(0xFFE1F5FE),
            topLeft = Offset(w * 0.28f, h * 0.22f),
            size = Size(w * 0.44f, h * 0.22f),
            cornerRadius = CornerRadius(w * 0.06f)
        )

        // Carita kawaii en el centro
        dibujarCaritaAsignatura(
            centroX = w * 0.50f,
            centroY = h * 0.62f,
            escala = w * 0.19f
        )
    }
}

@Composable
private fun IconoCienciasKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Macetita terracota
        drawRoundRect(
            color = Color(0xFFFF8A65),
            topLeft = Offset(w * 0.30f, h * 0.56f),
            size = Size(w * 0.40f, h * 0.28f),
            cornerRadius = CornerRadius(w * 0.08f)
        )
        drawRoundRect(
            color = Color(0xFFBF360C),
            topLeft = Offset(w * 0.30f, h * 0.56f),
            size = Size(w * 0.40f, h * 0.28f),
            cornerRadius = CornerRadius(w * 0.08f),
            style = Stroke(width = w * 0.04f)
        )

        // Tallito verde
        drawLine(
            color = Color(0xFF2E7D32),
            start = Offset(w * 0.50f, h * 0.56f),
            end = Offset(w * 0.50f, h * 0.34f),
            strokeWidth = w * 0.06f,
            cap = StrokeCap.Round
        )

        // Hojitas verdes felices
        drawOval(
            color = Color(0xFF66BB6A),
            topLeft = Offset(w * 0.22f, h * 0.24f),
            size = Size(w * 0.28f, h * 0.18f)
        )
        drawOval(
            color = Color(0xFF66BB6A),
            topLeft = Offset(w * 0.50f, h * 0.24f),
            size = Size(w * 0.28f, h * 0.18f)
        )

        // Carita kawaii en la maceta
        dibujarCaritaAsignatura(
            centroX = w * 0.50f,
            centroY = h * 0.70f,
            escala = w * 0.15f
        )
    }
}

@Composable
private fun IconoHistoriaKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.48f

        // Base del globo terráqueo
        drawRoundRect(
            color = Color(0xFF8D6E63),
            topLeft = Offset(w * 0.32f, h * 0.80f),
            size = Size(w * 0.36f, h * 0.10f),
            cornerRadius = CornerRadius(w * 0.05f)
        )

        // Esfera del planeta agua
        drawCircle(
            color = Color(0xFF4FC3F7),
            radius = w * 0.31f,
            center = Offset(cx, cy)
        )

        // Continentes verdes suaves
        drawCircle(
            color = Color(0xFF81C784),
            radius = w * 0.11f,
            center = Offset(cx - w * 0.14f, cy - h * 0.10f)
        )
        drawCircle(
            color = Color(0xFF81C784),
            radius = w * 0.10f,
            center = Offset(cx + w * 0.15f, cy - h * 0.06f)
        )

        // Contorno
        drawCircle(
            color = Color(0xFF0277BD),
            radius = w * 0.31f,
            center = Offset(cx, cy),
            style = Stroke(width = w * 0.045f)
        )

        // Carita kawaii del planeta
        dibujarCaritaAsignatura(
            centroX = cx,
            centroY = cy + h * 0.04f,
            escala = w * 0.18f
        )
    }
}

private fun DrawScope.dibujarCaritaAsignatura(
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
        topLeft = Offset(centroX - escala * 0.36f, centroY + escala * 0.04f),
        size = Size(escala * 0.72f, escala * 0.52f),
        style = Stroke(width = escala * 0.15f, cap = StrokeCap.Round)
    )
}

@Preview(name = "Vista Previa Asignaturas Kawaii", showBackground = true, widthDp = 360, heightDp = 820)
@Composable
fun AsignaturasScreenPreview() {
    MaterialTheme {
        AsignaturasContent(
            onVolverClick = {},
            onLenguajeClick = {},
            onMatematicaClick = {},
            onCienciasClick = {},
            onHistoriaClick = {}
        )
    }
}