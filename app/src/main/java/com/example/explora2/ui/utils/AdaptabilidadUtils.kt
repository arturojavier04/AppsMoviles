package com.example.explora2.ui.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun calcularTamanoVentana(): WindowSizeClass {
    val contexto = LocalContext.current
    val actividadActual = contexto as? Activity
        ?: throw IllegalStateException("El contexto actual no corresponde a una Activity")
    return calculateWindowSizeClass(activity = actividadActual)
}