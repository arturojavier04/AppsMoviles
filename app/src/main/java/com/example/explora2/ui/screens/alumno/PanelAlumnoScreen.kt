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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.explora2.model.GestionUiState
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.components.BarraNavegacionInferior
import com.example.explora2.ui.components.IconoLibroInfantil
import com.example.explora2.ui.components.IconoTrofeoInfantil
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelAlumnoScreen(
    navController: NavController,
    gestionUiState: GestionUiState
) {
    val tieneActividad = gestionUiState.autorizado && gestionUiState.asignadoAlumno

    Scaffold(
        containerColor = Color(0xFFFFFDE7), // Fondo crema pastel suave y alegre
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
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconoSolKawaii(modifier = Modifier.size(34.dp))
                                Column {
                                    Text(
                                        text = "Mi Espacio Escolar",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
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
                                        modifier = Modifier.size(19.dp)
                                    )
                                    Text(
                                        text = "¡A jugar!",
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

            // 1. CABECERA KAWAII DE BIENVENIDA CON OSITO EXPLORADOR (SIN CÁMARA)
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
                        AvatarOsitoKawaii(modifier = Modifier.size(62.dp))
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
                            text = "2.° Básico · Toca una tarjeta para jugar y aprender",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            // 2. MIS ASIGNATURAS (TARJETA AZUL CON LIBRO KAWAII)
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
                            IconoLibroInfantil(modifier = Modifier.size(38.dp))
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

            // 3. EVALUACIÓN FINAL DE UNIDAD (TARJETA LILA CON TROFEO KAWAII)
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
                            IconoTrofeoInfantil(modifier = Modifier.size(38.dp))
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

            // 4. ACTIVIDAD ASIGNADA POR EL DOCENTE (TARJETA DURAZNO CON ESTRELLITA KAWAII)
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
                            IconoEstrellaKawaii(modifier = Modifier.size(38.dp))
                        }
                        Text(
                            text = "Tarea del Profe",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )
                    }

                    if (tieneActividad) {
                        Text(
                            text = "¡Tienes una nueva actividad! 🎉",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3E2723)
                        )
                        Text(text = "Título: ${gestionUiState.titulo}", fontSize = 15.sp)
                        Text(text = "Asignatura: ${gestionUiState.asignatura}", fontSize = 15.sp)
                        Text(text = "Unidad: ${gestionUiState.unidad}", fontSize = 15.sp)
                        Text(text = "Objetivo: ${gestionUiState.objetivo}", fontSize = 15.sp)
                        Text(
                            text = "Instrucciones: ${gestionUiState.descripcion}",
                            fontSize = 15.sp
                        )

                        HorizontalDivider(color = Color(0xFFFFCC80))

                        if (gestionUiState.respuestaEntregada) {
                            Text(
                                text = "✅ Estado: Actividad entregada",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        } else {
                            Text(
                                text = "⏳ Estado: Pendiente de entrega",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                        }
                    } else {
                        Text(
                            text = "Todavía no tienes tareas extra asignadas.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Cuando tu docente envíe un contenido aprobado por UTP aparecerá aquí.",
                            fontSize = 14.sp,
                            color = Color(0xFF6D4C41)
                        )
                    }

                    Button(
                        onClick = { navController.navigate(Screen.ActividadAsignada.route) },
                        enabled = tieneActividad,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Text(
                            text = "Abrir Actividad 🌟",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// PERSONAJES KAWAII DIBUJADOS PARA EL PANEL DEL ESTUDIANTE
// =========================================================================

@Composable
private fun AvatarOsitoKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Orejitas redondas
        drawCircle(color = Color(0xFF8D6E63), radius = w * 0.16f, center = Offset(w * 0.24f, h * 0.26f))
        drawCircle(color = Color(0xFFFFCCBC), radius = w * 0.09f, center = Offset(w * 0.24f, h * 0.26f))

        drawCircle(color = Color(0xFF8D6E63), radius = w * 0.16f, center = Offset(w * 0.76f, h * 0.26f))
        drawCircle(color = Color(0xFFFFCCBC), radius = w * 0.09f, center = Offset(w * 0.76f, h * 0.26f))

        // Cabecita del osito
        drawCircle(color = Color(0xFFA1887F), radius = w * 0.36f, center = Offset(w * 0.50f, h * 0.54f))
        drawCircle(
            color = Color(0xFF5D4037),
            radius = w * 0.36f,
            center = Offset(w * 0.50f, h * 0.54f),
            style = Stroke(width = w * 0.04f)
        )

        // Hocico cremita
        drawOval(
            color = Color(0xFFFFF8E1),
            topLeft = Offset(w * 0.34f, h * 0.54f),
            size = Size(w * 0.32f, h * 0.22f)
        )

        // Naricita
        drawCircle(color = Color(0xFF3E2723), radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.60f))

        // Ojitos kawaii brillantes
        dibujarOjosYMejillasKawaii(
            centroX = w * 0.50f,
            centroY = h * 0.52f,
            escala = w * 0.24f
        )
    }
}
@Composable
private fun IconoSolKawaii(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Rayitos del sol alrededor
        val radioRayos = w * 0.44f
        for (i in 0 until 8) {
            val angulo = (i * 45.0) * (PI / 180.0)
            val rx = cx + (radioRayos * cos(angulo)).toFloat()
            val ry = cy + (radioRayos * sin(angulo)).toFloat()
            drawCircle(
                color = Color(0xFFFFF59D),
                radius = w * 0.07f,
                center = Offset(rx, ry)
            )
        }

        // Cuerpo redondo amarillo brillante
        drawCircle(
            color = Color(0xFFFFEE58),
            radius = w * 0.32f,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color(0xFFF57F17),
            radius = w * 0.32f,
            center = Offset(cx, cy),
            style = Stroke(width = w * 0.045f)
        )

        // Ojitos brillantes, mejillas rosadas y sonrisa
        dibujarOjosYMejillasKawaii(
            centroX = cx,
            centroY = cy + h * 0.02f,
            escala = w * 0.16f
        )
    }
}

@Composable
private fun IconoEstrellaKawaii(modifier: Modifier = Modifier) {
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

        dibujarOjosYMejillasKawaii(
            centroX = cx,
            centroY = cy + h * 0.03f,
            escala = w * 0.18f
        )
    }
}

private fun DrawScope.dibujarOjosYMejillasKawaii(
    centroX: Float,
    centroY: Float,
    escala: Float
) {
    val ojoOffsetX = escala * 0.68f
    val ojoY = centroY - escala * 0.18f
    val radioOjo = escala * 0.22f

    // Ojos y brillos blancos
    drawCircle(color = Color(0xFF212121), radius = radioOjo, center = Offset(centroX - ojoOffsetX, ojoY))
    drawCircle(color = Color.White, radius = radioOjo * 0.42f, center = Offset(centroX - ojoOffsetX - radioOjo * 0.25f, ojoY - radioOjo * 0.25f))

    drawCircle(color = Color(0xFF212121), radius = radioOjo, center = Offset(centroX + ojoOffsetX, ojoY))
    drawCircle(color = Color.White, radius = radioOjo * 0.42f, center = Offset(centroX + ojoOffsetX - radioOjo * 0.25f, ojoY - radioOjo * 0.25f))

    // Mejillas rosadas
    drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.85f), radius = radioOjo * 0.85f, center = Offset(centroX - ojoOffsetX * 1.25f, centroY + escala * 0.18f))
    drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.85f), radius = radioOjo * 0.85f, center = Offset(centroX + ojoOffsetX * 1.25f, centroY + escala * 0.18f))

    // Sonrisa
    drawArc(
        color = Color(0xFF212121),
        startAngle = 15f,
        sweepAngle = 150f,
        useCenter = false,
        topLeft = Offset(centroX - escala * 0.36f, centroY + escala * 0.05f),
        size = Size(escala * 0.72f, escala * 0.52f),
        style = Stroke(width = escala * 0.15f, cap = StrokeCap.Round)
    )
}

@Preview(
    name = "Vista Celular - Panel Estudiante Kawaii",
    showBackground = true,
    widthDp = 360,
    heightDp = 820
)
@Composable
fun PanelAlumnoScreenPreview() {
    MaterialTheme {
        PanelAlumnoScreen(
            navController = rememberNavController(),
            gestionUiState = GestionUiState()
        )
    }
}