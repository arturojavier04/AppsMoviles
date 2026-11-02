package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.explora2.navigation.Screen

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
        ObjetivoItem("OA 01", "Unidad 2", "Conteo y Números", "Contar números del 0 al 1000 de 2 en 2, de 5 en 5 y de 10 en 10.", "Matemática"),

        // Lenguaje y Comunicación
        ObjetivoItem("OA 03", "Unidad 1", "Lectura Fluida y Comprensión", "Leer en voz alta palabras frecuentes y comprender fábulas breves.", "Lenguaje"),
        ObjetivoItem("OA 12", "Unidad 2", "Escritura Guiada", "Escribir oraciones legibles utilizando mayúsculas y puntos.", "Lenguaje"),

        // Ciencias Naturales
        ObjetivoItem("OA 01", "Unidad 1", "El Cuerpo y Hábitos Saludables", "Identificar la función de órganos vitales como el corazón y pulmones.", "Ciencias"),
        ObjetivoItem("OA 05", "Unidad 2", "Animales y su Hábitat", "Observar e identificar animales nativos de Chile y su entorno.", "Ciencias"),

        // Historia, Geografía y Cs. Sociales
        ObjetivoItem("OA 06", "Unidad 1", "Nuestra Comunidad y Servicios", "Reconocer profesiones, oficios y servicios comunitarios.", "Historia"),
        ObjetivoItem("OA 08", "Unidad 2", "Pueblos Originarios y Paisajes", "Conocer costumbres y ubicación de los pueblos originarios de Chile.", "Historia")
    )

    var materiaSeleccionada by remember { mutableStateOf("Lenguaje") }
    val categorias = listOf("Lenguaje", "Matemática", "Ciencias", "Historia")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unidades y Objetivos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a Asignaturas"
                        )
                    }
                }
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
                text = "Selecciona una materia:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { materia ->
                    FilterChip(
                        selected = materiaSeleccionada == materia,
                        onClick = { materiaSeleccionada = materia },
                        label = { Text(materia) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val unidadesFiltradas = listaObjetivos.filter { it.asignatura == materiaSeleccionada }

                items(unidadesFiltradas) { item ->
                    Card(
                        onClick = {
                            // Pasa la materia exacta seleccionada para que abra su evaluación correspondiente
                            navController.currentBackStackEntry?.savedStateHandle?.set("materia", item.asignatura)
                            navController.navigate(Screen.RendirPrueba.route)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${item.unidad} • ${item.id}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = item.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toca para abrir evaluación de ${item.asignatura} ›",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Vista Celular - 4 Asignaturas",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun UnidadesObjetivoScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        UnidadesObjetivoScreen(navController = navController)
    }
}