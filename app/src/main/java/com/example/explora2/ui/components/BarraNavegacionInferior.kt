package com.example.explora2.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.example.explora2.navigation.Screen

@Composable
fun BarraNavegacionInferior(
    navController: NavController,
    rutaActual: String = Screen.PanelEstudiante.route
) {
    BarraNavegacionInferiorContent(
        rutaActual = rutaActual,
        onInicioClick = {
            if (rutaActual != Screen.PanelEstudiante.route) {
                navController.navigate(Screen.PanelEstudiante.route) {
                    popUpTo(Screen.PanelEstudiante.route) { inclusive = true }
                }
            }
        },
        onMateriasClick = {
            if (rutaActual != Screen.Asignaturas.route) {
                navController.navigate(Screen.Asignaturas.route)
            }
        },
        onAvanceClick = {
            if (rutaActual != Screen.ProgresoAlumno.route) {
                navController.navigate(Screen.ProgresoAlumno.route)
            }
        }
    )
}

@Composable
fun BarraNavegacionInferiorContent(
    rutaActual: String = Screen.PanelEstudiante.route,
    onInicioClick: () -> Unit = {},
    onMateriasClick: () -> Unit = {},
    onAvanceClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 12.dp,
        color = Color(0xFFFFF8E1) // Amarillo crema cálido
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            // 1. MI ESPACIO (Casita verde con carita feliz)
            NavigationBarItem(
                selected = rutaActual == Screen.PanelEstudiante.route,
                onClick = onInicioClick,
                icon = {
                    BurbujaCaricatura(
                        colorFondo = Color(0xFFC8E6C9),
                        colorBorde = Color(0xFF43A047),
                        seleccionado = rutaActual == Screen.PanelEstudiante.route
                    ) {
                        IconoCasitaInfantil(modifier = Modifier.size(30.dp))
                    }
                },
                label = {
                    Text(
                        text = "Mi Espacio",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFF1B5E20)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )

            // 2. MIS RAMOS (Libro azul con carita infantil)
            NavigationBarItem(
                selected = rutaActual == Screen.Asignaturas.route,
                onClick = onMateriasClick,
                icon = {
                    BurbujaCaricatura(
                        colorFondo = Color(0xFFBBDEFB),
                        colorBorde = Color(0xFF1E88E5),
                        seleccionado = rutaActual == Screen.Asignaturas.route
                    ) {
                        IconoLibroInfantil(modifier = Modifier.size(30.dp))
                    }
                },
                label = {
                    Text(
                        text = "Mis Ramos",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFF0D47A1)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )

            // 3. MIS LOGROS (Trofeo dorado con carita feliz)
            NavigationBarItem(
                selected = rutaActual == Screen.ProgresoAlumno.route,
                onClick = onAvanceClick,
                icon = {
                    BurbujaCaricatura(
                        colorFondo = Color(0xFFFFE082),
                        colorBorde = Color(0xFFFB8C00),
                        seleccionado = rutaActual == Screen.ProgresoAlumno.route
                    ) {
                        IconoTrofeoInfantil(modifier = Modifier.size(30.dp))
                    }
                },
                label = {
                    Text(
                        text = "Mis Logros",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFFE65100)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun BurbujaCaricatura(
    colorFondo: Color,
    colorBorde: Color,
    seleccionado: Boolean,
    contenido: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(if (seleccionado) 52.dp else 46.dp)
            .shadow(elevation = if (seleccionado) 6.dp else 2.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(if (seleccionado) colorFondo else Color.White)
            .border(
                width = if (seleccionado) 3.dp else 2.dp,
                color = colorBorde,
                shape = CircleShape
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        contenido()
    }
}

// =========================================================================
// DIBUJOS DE CARICATURA INFANTIL (KAWAII) CON OJOS BRILLANTES Y SONRISA
// =========================================================================

@Composable
fun IconoLibroInfantil(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Hojas blancas detrás
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.20f, h * 0.14f),
            size = Size(w * 0.64f, h * 0.72f),
            cornerRadius = CornerRadius(w * 0.12f)
        )
        // Portada azul vibrante
        drawRoundRect(
            color = Color(0xFF2196F3),
            topLeft = Offset(w * 0.16f, h * 0.18f),
            size = Size(w * 0.64f, h * 0.70f),
            cornerRadius = CornerRadius(w * 0.12f)
        )
        // Lomo azul oscuro
        drawRoundRect(
            color = Color(0xFF0D47A1),
            topLeft = Offset(w * 0.16f, h * 0.18f),
            size = Size(w * 0.14f, h * 0.70f),
            cornerRadius = CornerRadius(w * 0.08f)
        )
        // Contorno caricatura
        drawRoundRect(
            color = Color(0xFF0D47A1),
            topLeft = Offset(w * 0.16f, h * 0.18f),
            size = Size(w * 0.64f, h * 0.70f),
            cornerRadius = CornerRadius(w * 0.12f),
            style = Stroke(width = w * 0.05f)
        )

        // Carita feliz en la portada
        dibujarCaritaKawaii(
            centroX = w * 0.53f,
            centroY = h * 0.54f,
            escala = w * 0.22f
        )
    }
}

@Composable
fun IconoTrofeoInfantil(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Asas laterales del trofeo
        drawCircle(
            color = Color(0xFFFFA000),
            radius = w * 0.16f,
            center = Offset(w * 0.24f, h * 0.36f),
            style = Stroke(width = w * 0.07f)
        )
        drawCircle(
            color = Color(0xFFFFA000),
            radius = w * 0.16f,
            center = Offset(w * 0.76f, h * 0.36f),
            style = Stroke(width = w * 0.07f)
        )

        // Base marrón oscuro
        drawRoundRect(
            color = Color(0xFF6D4C41),
            topLeft = Offset(w * 0.30f, h * 0.76f),
            size = Size(w * 0.40f, h * 0.14f),
            cornerRadius = CornerRadius(w * 0.06f)
        )
        // Pedestal dorado
        drawRect(
            color = Color(0xFFFFB300),
            topLeft = Offset(w * 0.43f, h * 0.58f),
            size = Size(w * 0.14f, h * 0.18f)
        )

        // Copa dorada
        drawRoundRect(
            color = Color(0xFFFFCA28),
            topLeft = Offset(w * 0.24f, h * 0.16f),
            size = Size(w * 0.52f, h * 0.46f),
            cornerRadius = CornerRadius(w * 0.18f)
        )
        drawRoundRect(
            color = Color(0xFFE65100),
            topLeft = Offset(w * 0.24f, h * 0.16f),
            size = Size(w * 0.52f, h * 0.46f),
            cornerRadius = CornerRadius(w * 0.18f),
            style = Stroke(width = w * 0.05f)
        )

        // Carita feliz en la copa
        dibujarCaritaKawaii(
            centroX = w * 0.50f,
            centroY = h * 0.39f,
            escala = w * 0.20f
        )
    }
}

@Composable
fun IconoCasitaInfantil(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Paredes verde menta alegre
        drawRoundRect(
            color = Color(0xFF66BB6A),
            topLeft = Offset(w * 0.22f, h * 0.42f),
            size = Size(w * 0.56f, h * 0.44f),
            cornerRadius = CornerRadius(w * 0.08f)
        )
        drawRoundRect(
            color = Color(0xFF1B5E20),
            topLeft = Offset(w * 0.22f, h * 0.42f),
            size = Size(w * 0.56f, h * 0.44f),
            cornerRadius = CornerRadius(w * 0.08f),
            style = Stroke(width = w * 0.05f)
        )

        // Techo rojo coral tipo caricatura
        val techo = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            lineTo(w * 0.12f, h * 0.46f)
            lineTo(w * 0.88f, h * 0.46f)
            close()
        }
        drawPath(path = techo, color = Color(0xFFFF7043))
        drawPath(
            path = techo,
            color = Color(0xFFBF360C),
            style = Stroke(width = w * 0.05f)
        )

        // Carita feliz en la casita
        dibujarCaritaKawaii(
            centroX = w * 0.50f,
            centroY = h * 0.65f,
            escala = w * 0.21f
        )
    }
}

// Función compartida que dibuja ojitos negros con brillo blanco, mejillas rosadas y sonrisa
private fun DrawScope.dibujarCaritaKawaii(
    centroX: Float,
    centroY: Float,
    escala: Float
) {
    val ojoOffsetX = escala * 0.72f
    val ojoY = centroY - escala * 0.15f
    val radioOjo = escala * 0.24f

    // Ojo izquierdo y brillo
    drawCircle(color = Color(0xFF212121), radius = radioOjo, center = Offset(centroX - ojoOffsetX, ojoY))
    drawCircle(color = Color.White, radius = radioOjo * 0.40f, center = Offset(centroX - ojoOffsetX - radioOjo * 0.25f, ojoY - radioOjo * 0.25f))

    // Ojo derecho y brillo
    drawCircle(color = Color(0xFF212121), radius = radioOjo, center = Offset(centroX + ojoOffsetX, ojoY))
    drawCircle(color = Color.White, radius = radioOjo * 0.40f, center = Offset(centroX + ojoOffsetX - radioOjo * 0.25f, ojoY - radioOjo * 0.25f))

    // Mejillas rosadas
    drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.85f), radius = radioOjo * 0.85f, center = Offset(centroX - ojoOffsetX * 1.25f, centroY + escala * 0.22f))
    drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.85f), radius = radioOjo * 0.85f, center = Offset(centroX + ojoOffsetX * 1.25f, centroY + escala * 0.22f))

    // Sonrisa curva
    drawArc(
        color = Color(0xFF212121),
        startAngle = 15f,
        sweepAngle = 150f,
        useCenter = false,
        topLeft = Offset(centroX - escala * 0.42f, centroY - escala * 0.05f),
        size = Size(escala * 0.84f, escala * 0.65f),
        style = Stroke(width = escala * 0.18f, cap = StrokeCap.Round)
    )
}

@Preview(name = "Bottom Bar Caricatura Kawaii", showBackground = true)
@Composable
fun BarraNavegacionInferiorPreview() {
    MaterialTheme {
        BarraNavegacionInferiorContent()
    }
}