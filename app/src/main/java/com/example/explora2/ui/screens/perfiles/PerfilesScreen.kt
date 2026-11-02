package com.example.explora2.ui.screens.perfiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen

@Composable
fun PerfilesScreen(navController: NavController) {
    PerfilesContent(
        onDocenteClick = { navController.navigate(Screen.PanelDocente.route) },
        onUtpClick = { navController.navigate(Screen.PanelUtp.route) },
        onEstudianteClick = { navController.navigate(Screen.PanelEstudiante.route) }
    )
}

@Composable
fun PerfilesContent(
    onDocenteClick: () -> Unit,
    onUtpClick: () -> Unit,
    onEstudianteClick: () -> Unit
) {
    val fondoPantalla = Color(0xFFF8FAFC)

    // Docente -> AZUL
    val fondoCardDocente = Color(0xFFE3F2FD)
    val colorBotonDocente = Color(0xFF1976D2)

    // Jefatura UTP -> VERDE
    val fondoCardUtp = Color(0xFFE8F5E9)
    val colorBotonUtp = Color(0xFF2E7D32)

    // Estudiante / Alumno -> VERDE ESMERALDA
    val fondoCardAlumno = Color(0xFFE0F2F1)
    val colorBotonAlumno = Color(0xFF00796B)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = fondoPantalla
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Tarjeta de bienvenida institucional
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📖", fontSize = 34.sp)
                    Text(
                        text = "EXPLORA 2",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = "Escuela Marcela Paz",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF424242)
                    )
                    Text(
                        text = "🌟 Aprende Jugando 🌟",
                        fontSize = 13.sp,
                        color = Color(0xFF616161),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Text(
                text = "¡Hola! Bienvenid@ 👋",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = "¿Cómo quieres ingresar?",
                fontSize = 16.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = "2.° Básico",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            // --- 1. ROL DOCENTE (AZUL) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = fondoCardDocente)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "👩‍🏫", fontSize = 30.sp, modifier = Modifier.padding(end = 12.dp))
                        Column {
                            Text(
                                text = "Docente",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )
                            Text(
                                text = "Organiza contenidos, evaluaciones y actividades",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF37474F)
                            )
                        }
                    }

                    Button(
                        onClick = onDocenteClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorBotonDocente),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Docente", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- 2. ROL JEFATURA UTP (VERDE) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = fondoCardUtp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "👩‍💼", fontSize = 30.sp, modifier = Modifier.padding(end = 12.dp))
                        Column {
                            Text(
                                text = "Jefatura UTP",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "Supervisa Objetivos de Aprendizaje y autorizaciones",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E3D30)
                            )
                        }
                    }

                    Button(
                        onClick = onUtpClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorBotonUtp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("UTP", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- 3. ROL ESTUDIANTE / ALUMNO (VERDE ESMERALDA) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = fondoCardAlumno)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "👦", fontSize = 30.sp, modifier = Modifier.padding(end = 12.dp))
                        Column {
                            Text(
                                text = "Estudiante",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF004D40)
                            )
                            Text(
                                text = "Aprende, rinde tus pruebas y suma estrellas",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF263238)
                            )
                        }
                    }

                    Button(
                        onClick = onEstudianteClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorBotonAlumno),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("¡Comenzar a Explorar!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(
    name = "Vista Celular - Selector Perfiles",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun PerfilesScreenPreview() {
    MaterialTheme {
        PerfilesContent(
            onDocenteClick = {},
            onUtpClick = {},
            onEstudianteClick = {}
        )
    }
}