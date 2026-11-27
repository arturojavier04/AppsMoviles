
package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.data.model.ConfiguracionExamenDto
import com.example.explora2.model.ConfiguracionExamenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionExamenScreen(
    navController: NavController? = null,
    estado: ConfiguracionExamenUiState,
    onCambiar: (ConfiguracionExamenDto) -> Unit,
    onGuardar: () -> Unit,
    onActualizar: () -> Unit
) {

    val datos = estado.borrador

    val editable = estado.cargado &&
            !estado.cargando &&
            !estado.guardando

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Configuración de Prueba")
                },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver al panel"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Parámetros de Evaluación",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedButton(
                onClick = onActualizar,
                enabled = !estado.cargando &&
                        !estado.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar desde servidor")
            }

            if (estado.cargando || estado.guardando) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "Número de Intentos Permitidos: " +
                        datos.intentosPermitidos,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = {
                        onCambiar(
                            datos.copy(
                                intentosPermitidos =
                                    datos.intentosPermitidos - 1
                            )
                        )
                    },
                    enabled = editable &&
                            datos.intentosPermitidos > 1
                ) {
                    Text("-")
                }

                Button(
                    onClick = {
                        onCambiar(
                            datos.copy(
                                intentosPermitidos =
                                    datos.intentosPermitidos + 1
                            )
                        )
                    },
                    enabled = editable &&
                            datos.intentosPermitidos < 5
                ) {
                    Text("+")
                }
            }

            OutlinedTextField(
                value = datos.fechaInicio.orEmpty(),
                onValueChange = {
                    onCambiar(datos.copy(fechaInicio = it))
                },
                label = {
                    Text("Fecha de inicio")
                },
                placeholder = {
                    Text("DD/MM/AAAA")
                },
                enabled = editable,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = datos.fechaFin.orEmpty(),
                onValueChange = {
                    onCambiar(datos.copy(fechaFin = it))
                },
                label = {
                    Text("Fecha de cierre")
                },
                placeholder = {
                    Text("DD/MM/AAAA")
                },
                enabled = editable,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Control de Versiones de la Prueba",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                listOf("v1.0", "v1.2", "v2.0")
                    .forEach { version ->

                        FilterChip(
                            selected =
                                datos.version == version,
                            onClick = {
                                onCambiar(
                                    datos.copy(
                                        version = version
                                    )
                                )
                            },
                            enabled = editable,
                            label = {
                                Text(version)
                            }
                        )
                    }
            }

            Text(
                "Versión actual: ${datos.version.orEmpty()}"
            )

            Button(
                onClick = onGuardar,
                enabled = editable,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (estado.guardando)
                        "Guardando..."
                    else
                        "Guardar Parámetros de Examen"
                )
            }

            estado.error?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            estado.mensaje?.let {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(12.dp),
                        color =
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            datos.id?.let {
                Text(
                    text = "Configuración remota #$it",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(
    name = "Configuración Examen",
    showBackground = true
)
@Composable
fun ConfiguracionExamenScreenPreview() {
    MaterialTheme {
        ConfiguracionExamenScreen(
            estado = ConfiguracionExamenUiState(
                cargando = false,
                cargado = true
            ),
            onCambiar = {},
            onGuardar = {},
            onActualizar = {}
        )
    }
}
