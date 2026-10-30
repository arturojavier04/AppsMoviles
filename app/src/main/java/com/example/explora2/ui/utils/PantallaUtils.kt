package com.example.explora2.ui.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun calcularTamanoVentana(): WindowSizeClass {
    val actividadActual = requireNotNull(LocalActivity.current)
    return calculateWindowSizeClass(actividadActual)
}