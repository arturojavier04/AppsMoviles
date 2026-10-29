package com.example.explora2.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController, viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro / Perfil") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección / Colegio") },
                isError = estado.errores.direccion != null,
                supportingText = {
                    estado.errores.direccion?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Acepto los términos")
            }

            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        navController.navigate(Screen.Resumen.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}