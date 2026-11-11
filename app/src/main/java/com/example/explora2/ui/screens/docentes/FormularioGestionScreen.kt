package com.example.explora2.ui.screens.docentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.model.GestionErrores
import com.example.explora2.model.GestionUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioGestionScreen(
    navController: NavController? = null,
    uiState: GestionUiState,
    errores: GestionErrores,
    onAsignaturaChange: (String) -> Unit,
    onUnidadChange: (String) -> Unit,
    onObjetivoChange: (String) -> Unit,
    onTipoRecursoChange: (String) -> Unit,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    onEnviarClick: () -> Unit,
    onNuevaPlanificacionClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planificación Docente") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver al Panel Docente"
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Planificación de Contenido Pedagógico",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = uiState.asignatura,
                onValueChange = onAsignaturaChange,
                enabled = !uiState.enviadoUtp,
                label = { Text("Asignatura") },
                isError = errores.asignatura != null,
                trailingIcon = {
                    if (errores.asignatura != null) Icon(Icons.Default.Error, contentDescription = "Error")
                },
                supportingText = { errores.asignatura?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.unidad,
                onValueChange = onUnidadChange,
                enabled = !uiState.enviadoUtp,
                label = { Text("Unidad Curricular") },
                isError = errores.unidad != null,
                trailingIcon = {
                    if (errores.unidad != null) Icon(Icons.Default.Error, contentDescription = "Error")
                },
                supportingText = { errores.unidad?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.objetivo,
                onValueChange = onObjetivoChange,
                enabled = !uiState.enviadoUtp,
                label = { Text("Objetivo de Aprendizaje (OA)") },
                isError = errores.objetivo != null,
                trailingIcon = {
                    if (errores.objetivo != null) Icon(Icons.Default.Error, contentDescription = "Error")
                },
                supportingText = { errores.objetivo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.tipoRecurso,
                onValueChange = onTipoRecursoChange,
                enabled = !uiState.enviadoUtp,
                label = { Text("Tipo de Material (Texto / Audio / Visual)") },
                isError = errores.tipoRecurso != null,
                trailingIcon = {
                    if (errores.tipoRecurso != null) Icon(Icons.Default.Error, contentDescription = "Error")
                },
                supportingText = { errores.tipoRecurso?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = onTituloChange,
                enabled = !uiState.enviadoUtp,
                label = { Text("Título de la Actividad") },
                isError = errores.titulo != null,
                trailingIcon = {
                    if (errores.titulo != null) Icon(Icons.Default.Error, contentDescription = "Error")
                },
                supportingText = { errores.titulo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                enabled = !uiState.enviadoUtp,
                label = { Text("Instrucción o Contenido Didáctico") },
                isError = errores.descripcion != null,
                trailingIcon = {
                    if (errores.descripcion != null) Icon(Icons.Default.Error, contentDescription = "Error")
                },
                supportingText = { errores.descripcion?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            if (!uiState.enviadoUtp) {
                Button(
                    onClick = onGuardarClick,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Validar Contenido")
                }
            }

            if (uiState.validado && !uiState.enviadoUtp) {
                Text(
                    text = "✓ Formulario validado correctamente. Listo para enviar a UTP.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onEnviarClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar borrador a UTP")
                }
            }

            if (uiState.enviadoUtp) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (uiState.autorizado) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (uiState.autorizado)
                            "Estado: Autorizado por UTP ✓"
                        else
                            "Estado: Pendiente de revisión por UTP ⌛",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // BOTÓN PARA CREAR OTRA PLANIFICACIÓN
                Button(
                    onClick = onNuevaPlanificacionClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Crear otra planificación (+)")
                }
            }

            if (navController != null) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Volver al Panel Docente")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioGestionScreenPreview() {
    MaterialTheme {
        FormularioGestionScreen(
            navController = null,
            uiState = GestionUiState(titulo = "Lectura 2.° Básico", descripcion = "Lectura guiada"),
            errores = GestionErrores(),
            onAsignaturaChange = {},
            onUnidadChange = {},
            onObjetivoChange = {},
            onTipoRecursoChange = {},
            onTituloChange = {},
            onDescripcionChange = {},
            onGuardarClick = {},
            onEnviarClick = {},
            onNuevaPlanificacionClick = {}
        )
    }
}