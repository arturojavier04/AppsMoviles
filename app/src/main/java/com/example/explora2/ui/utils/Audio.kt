package com.example.explora2.ui.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class Audio(context: Context) {
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) { estado ->
            if (estado == TextToSpeech.SUCCESS) {
                tts?.language = Locale.Builder().setLanguage("es").setRegion("CL").build()
            }
        }
    }

    fun hablar(mensaje: String) {
        tts?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun liberar() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}