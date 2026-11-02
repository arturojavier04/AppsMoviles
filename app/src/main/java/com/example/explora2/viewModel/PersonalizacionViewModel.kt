package com.example.explora2.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PersonalizacionViewModel : ViewModel() {

    private val _fotoAvatarUri = MutableStateFlow<Uri?>(null)
    val fotoAvatarUri: StateFlow<Uri?> = _fotoAvatarUri.asStateFlow()

    fun actualizarDesdeGaleria(archivoUri: Uri?) {
        archivoUri?.let {
            _fotoAvatarUri.value = it
        }
    }

    fun actualizarDesdeCamara(archivoUri: Uri?) {
        archivoUri?.let {
            _fotoAvatarUri.value = it
        }
    }
}