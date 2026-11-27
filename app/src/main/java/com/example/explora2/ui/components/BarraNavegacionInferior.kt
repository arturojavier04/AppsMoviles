package com.example.explora2.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explora2.R // Asegúrate de importar tu R local
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
            // 1. MI ESPACIO (Poke)
            NavigationBarItem(
                selected = rutaActual == Screen.PanelEstudiante.route,
                onClick = onInicioClick,
                icon = {
                    BurbujaCaricatura(
                        colorFondo = Color(0xFFC8E6C9),
                        colorBorde = Color(0xFF43A047),
                        seleccionado = rutaActual == Screen.PanelEstudiante.route
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.poke),
                            contentDescription = "Mi Espacio",
                            modifier = Modifier.size(34.dp)
                        )
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

            // 2. MIS RAMOS (Ultra)
            NavigationBarItem(
                selected = rutaActual == Screen.Asignaturas.route,
                onClick = onMateriasClick,
                icon = {
                    BurbujaCaricatura(
                        colorFondo = Color(0xFFBBDEFB),
                        colorBorde = Color(0xFF1E88E5),
                        seleccionado = rutaActual == Screen.Asignaturas.route
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ultra),
                            contentDescription = "Mis Ramos",
                            modifier = Modifier.size(34.dp)
                        )
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

            // 3. MIS LOGROS (Master)
            NavigationBarItem(
                selected = rutaActual == Screen.ProgresoAlumno.route,
                onClick = onAvanceClick,
                icon = {
                    BurbujaCaricatura(
                        colorFondo = Color(0xFFFFE082),
                        colorBorde = Color(0xFFFB8C00),
                        seleccionado = rutaActual == Screen.ProgresoAlumno.route
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.master),
                            contentDescription = "Mis Logros",
                            modifier = Modifier.size(34.dp)
                        )
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
            .size(if (seleccionado) 54.dp else 46.dp)
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

@Preview(name = "Bottom Bar Pokébolas", showBackground = true)
@Composable
fun BarraNavegacionInferiorPreview() {
    MaterialTheme {
        BarraNavegacionInferiorContent()
    }
}