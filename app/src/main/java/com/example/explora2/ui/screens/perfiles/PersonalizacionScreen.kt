package com.example.explora2.ui.screens.perfiles

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.explora2.viewmodel.PersonalizacionViewModel
import java.io.File

@Composable
fun AvatarPersonalizacionScreen(viewModel: PersonalizacionViewModel) {
    val context = LocalContext.current
    val avatarUri by viewModel.fotoAvatarUri.collectAsState()
    val archivoTemporalUri = remember { mutableStateOf<Uri?>(null) }

    val selectorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.actualizarDesdeGaleria(uri) }

    val capturaCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitoso ->
        if (exitoso) {
            viewModel.actualizarDesdeCamara(archivoTemporalUri.value)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Foto de Perfil del Usuario", style = MaterialTheme.typography.titleLarge)
        Text(text = if (avatarUri != null) "Foto cargada: ${avatarUri?.lastPathSegment}" else "Sin imagen asignada")

        Button(
            onClick = { selectorGaleria.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar de Galería")
        }

        Button(
            onClick = {
                val archivoFoto = File.createTempFile("avatar_", ".jpg", context.cacheDir)
                val uriGenerada = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    archivoFoto
                )
                archivoTemporalUri.value = uriGenerada
                capturaCamara.launch(uriGenerada)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar Foto con Cámara")
        }
    }
}