package com.example.explora2.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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

    Column(modifier = Modifier.padding(16.dp)) {
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

@Preview(name = "Interruptor Lectura - Activo", showBackground = true)
@Composable
fun InterruptorLecturaActivoPreview() {
    MaterialTheme {
        InterruptorLectura(habilitado = true, onAlternar = {})
    }
}

@Preview(name = "Interruptor Lectura - Inactivo", showBackground = true)
@Composable
fun InterruptorLecturaInactivoPreview() {
    MaterialTheme {
        InterruptorLectura(habilitado = false, onAlternar = {})
    }
}