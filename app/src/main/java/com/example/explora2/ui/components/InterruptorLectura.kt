package com.example.explora2.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InterruptorLectura(
    habilitado: Boolean,
    onAlternar: () -> Unit
) {
    var persistido by rememberSaveable(habilitado) { mutableStateOf(habilitado) }

    val etiquetaAccion by remember(persistido) {
        derivedStateOf {
            if (persistido) "Desactivar Modo Apoyo Lectura" else "Activar Modo Apoyo Lectura"
        }
    }

    val colorDinamico by animateColorAsState(
        targetValue = if (persistido) Color(0xFF2E7D32) else Color(0xFFC62828),
        label = "animacionColorLectura"
    )

    Column {
        Text(
            text = if (persistido) "Modo Lectura Guiada: ACTIVO" else "Modo Lectura Guiada: INACTIVO",
            color = colorDinamico,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(onClick = {
            persistido = !persistido
            onAlternar()
        }) {
            Text(text = etiquetaAccion)
        }
    }
}