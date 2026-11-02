package com.example.explora2.ui.screens.perfiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.model.UsuarioUiState
import com.example.explora2.navigation.Screen
import com.example.explora2.viewmodel.UsuarioViewModel

@Composable
fun FichaRegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val datosUsuario by viewModel.estado.collectAsState()

    FichaRegistroContent(
        estado = datosUsuario,
        onIrAPerfilesClick = { navController.navigate(Screen.SelectorPerfil.route) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaRegistroContent(
    estado: UsuarioUiState,
    onIrAPerfilesClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de Ficha") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Datos de Usuario Registrados",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Nombre: ${estado.nombre}")
                    Text("Correo: ${estado.correo}")
                    Text("Dirección: ${estado.direccion}")
                    Text("Contraseña: ${"•".repeat(estado.clave.length.coerceAtLeast(6))}")
                    Text("Términos: ${if (estado.aceptaTerminos) "Aceptados" else "Pendientes"}")
                }
            }

            Button(
                onClick = onIrAPerfilesClick,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Ir a Selector de Perfiles")
            }
        }
    }
}

@Preview(
    name = "Vista Celular - Ficha Registro",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun FichaRegistroScreenPreview() {
    MaterialTheme {
        FichaRegistroContent(
            estado = UsuarioUiState(
                nombre = "Andrea Campos",
                correo = "andrea.campos@marcelapaz.cl",
                clave = "123456",
                direccion = "Av. Gabriela Mistral 450",
                aceptaTerminos = true
            ),
            onIrAPerfilesClick = {}
        )
    }
}