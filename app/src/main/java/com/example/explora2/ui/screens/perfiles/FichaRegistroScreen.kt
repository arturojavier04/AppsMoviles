package com.example.explora2.ui.screens.perfiles

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaRegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val datosUsuario by viewModel.estado.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de Ficha") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
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
                    Text("Nombre: ${datosUsuario.nombre}")
                    Text("Correo: ${datosUsuario.correo}")
                    Text("Dirección: ${datosUsuario.direccion}")
                    Text("Contraseña: ${"•".repeat(datosUsuario.clave.length)}")
                    Text("Términos: ${if (datosUsuario.aceptaTerminos) "Aceptados" else "Pendientes"}")
                }
            }

            Button(
                onClick = { navController.navigate(Screen.SelectorPerfil.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a Selector de Perfiles")
            }
        }
    }
}