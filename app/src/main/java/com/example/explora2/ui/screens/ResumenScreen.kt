package com.example.explora2.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explora2.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen del Registro") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Datos Ingresados:", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Nombre: ${estado.nombre}")
            Text(text = "Correo: ${estado.correo}")
            Text(text = "Dirección: ${estado.direccion}")
            Text(text = "Contraseña: ${"*".repeat(estado.clave.length)}")
            Text(text = "Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}")
        }
    }
}