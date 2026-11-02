package com.example.explora2.ui.screens.perfiles

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
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

    AvatarPersonalizacionContent(
        avatarUriString = avatarUri?.lastPathSegment,
        onGaleriaClick = { selectorGaleria.launch("image/*") },
        onCamaraClick = {
            val archivoFoto = File.createTempFile("avatar_", ".jpg", context.cacheDir)
            val uriGenerada = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                archivoFoto
            )
            archivoTemporalUri.value = uriGenerada
            capturaCamara.launch(uriGenerada)
        }
    )
}

@Composable
fun AvatarPersonalizacionContent(
    avatarUriString: String?,
    onGaleriaClick: () -> Unit,
    onCamaraClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Foto de Perfil ", style = MaterialTheme.typography.titleLarge)

        // Círculo representativo de Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar de Usuario",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Text(
            text = if (avatarUriString != null) "Foto cargada: $avatarUriString" else "Sin imagen asignada",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onGaleriaClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar de Galería")
        }

        OutlinedButton(
            onClick = onCamaraClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar Foto con Cámara")
        }
    }
}

@Preview(
    name = "Vista Celular - Avatar Perfil",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun AvatarPersonalizacionScreenPreview() {
    MaterialTheme {
        AvatarPersonalizacionContent(
            avatarUriString = "avatar_usuario_demo.jpg",
            onGaleriaClick = {},
            onCamaraClick = {}
        )
    }
}