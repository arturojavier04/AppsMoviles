package com.example.explora2.ui.screens.alumno

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
        onHistoriaClick = { navegarAUnidades("Historia") }
    )
}

@Composable
fun AsignaturasContent(
    onVolverClick: () -> Unit,
    onLenguajeClick: () -> Unit,
    onMatematicaClick: () -> Unit,
    onCienciasClick: () -> Unit,
    onHistoriaClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolverClick) {
                    Text(text = "←", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mis Asignaturas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }

            Text(
                text = "2.° Básico - Escuela Marcela Paz",
                fontSize = 15.sp,
                color = Color(0xFF64748B)
            )

            Text(
                text = "Elige un ramo para ver sus unidades:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            TarjetaMateria(
                emoji = "📚",
                titulo = "Lenguaje y Comunicación",
                descripcion = "Lectura fluida, fábulas y comprensión lectora",
                colorFondo = Color(0xFFE0F2F1),
                colorTexto = Color(0xFF00796B),
                onClick = onLenguajeClick
            )

            TarjetaMateria(
                emoji = "➕➖",
                titulo = "Matemática",
                descripcion = "Números, sumas y restas hasta el 100",
                colorFondo = Color(0xFFE3F2FD),
                colorTexto = Color(0xFF1976D2),
                onClick = onMatematicaClick
            )

            TarjetaMateria(
                emoji = "🌱",
                titulo = "Ciencias Naturales",
                descripcion = "Seres vivos, el cuerpo humano y su hábitat",
                colorFondo = Color(0xFFE8F5E9),
                colorTexto = Color(0xFF2E7D32),
                onClick = onCienciasClick
            )

            TarjetaMateria(
                emoji = "🗺️",
                titulo = "Historia y Geografía",
                descripcion = "Nuestra comunidad, pueblos y paisajes de Chile",
                colorFondo = Color(0xFFFFF8E1),
                colorTexto = Color(0xFFF57F17),
                onClick = onHistoriaClick
            )
        }
    }
}

@Composable
fun TarjetaMateria(
    emoji: String,
    titulo: String,
    descripcion: String,
    colorFondo: Color,
    colorTexto: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = emoji, fontSize = 32.sp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto
                )
                Text(
                    text = descripcion,
                    fontSize = 13.sp,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = "›",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
        }
    }
}

@Preview(name = "Vista Previa Asignaturas", showBackground = true, widthDp = 360, heightDp = 800)
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