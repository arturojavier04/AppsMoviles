
package com.example.explora2.ui.screens.alumno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen

@Composable
fun PanelAlumnoScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Panel del Estudiante")
        Text(text = "Segundo Básico")

        Text(text = "Mis asignaturas")

        Text(text = "Lenguaje y Comunicación")
        Text(text = "Matemática")
        Text(text = "Ciencias Naturales")
        Text(text = "Historia, Geografía y Ciencias Sociales")

        Button(
            onClick = {
                navController.navigate(Screen.SelectorPerfil.route)
            }
        ) {
            Text(text = "Volver a perfiles")
        }
    }
}
