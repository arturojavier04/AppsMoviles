package com.example.explora2.ui.screens.perfiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.model.UsuarioErrores
import com.example.explora2.model.UsuarioUiState
import com.example.explora2.viewmodel.UsuarioViewModel

@Composable
fun RegistroAccesoScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    RegistroAccesoContent(
        estado = estado,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onClaveChange = viewModel::onClaveChange,
        onDireccionChange = viewModel::onDireccionChange,
        onAceptarTerminosChange = viewModel::onAceptarTerminosChange,
        onGuardarClick = {
            if (viewModel.validarFormulario()) {
                navController.navigate("resumen_registro")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroAccesoContent(
    estado: UsuarioUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    onAceptarTerminosChange: (Boolean) -> Unit,
    onGuardarClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro / Acceso Institucional") }) }
    ) { paddingValores ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre Completo") },
                isError = estado.errores.nombre != null,
                supportingText = { estado.errores.nombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo Institucional (@)") },
                isError = estado.errores.correo != null,
                supportingText = { estado.errores.correo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.clave,
                onValueChange = onClaveChange,
                label = { Text("Contraseña (mínimo 6 caracteres)") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = { estado.errores.clave?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.direccion,
                onValueChange = onDireccionChange,
                label = { Text("Dirección / Colegio") },
                isError = estado.errores.direccion != null,
                supportingText = { estado.errores.direccion?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = onAceptarTerminosChange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Acepto términos escolares")
            }

            Button(
                onClick = onGuardarClick,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Guardar y Continuar")
            }
        }
    }
}

@Preview(
    name = "Vista Celular - Registro Formulario",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun RegistroAccesoScreenPreview() {
    MaterialTheme {
        RegistroAccesoContent(
            estado = UsuarioUiState(
                nombre = "Prof. Roberto Vidal",
                correo = "roberto.vidal@marcelapaz.cl",
                clave = "123456",
                direccion = "Escuela Marcela Paz",
                aceptaTerminos = true,
                errores = UsuarioErrores()
            ),
            onNombreChange = {},
            onCorreoChange = {},
            onClaveChange = {},
            onDireccionChange = {},
            onAceptarTerminosChange = {},
            onGuardarClick = {}
        )
    }
}