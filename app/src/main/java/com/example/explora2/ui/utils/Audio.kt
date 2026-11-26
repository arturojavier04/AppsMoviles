package com.example.explora2.ui.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import java.util.Locale

class Audio(context: Context) {
    private var tts: TextToSpeech? = null
    private var mediaPlayer: MediaPlayer? = null

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

    fun reproducirUrl(urlAudio: String?) {
        if (urlAudio.isNullOrBlank()) return
        try {
            mediaPlayer?.runCatching {
                if (isPlaying) stop()
                release()
            }
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build()
                )
                setDataSource(urlAudio)
                setOnPreparedListener { mp ->
                    mp.start()
                }
                setOnCompletionListener { mp ->
                    mp.release()
                    if (mediaPlayer === mp) mediaPlayer = null
                }
                setOnErrorListener { mp, _, _ ->
                    mp.release()
                    if (mediaPlayer === mp) mediaPlayer = null
                    true
                }
                prepareAsync()
            }
        } catch (_: Exception) {
            // Evita fallos si no hay conexión o el stream no puede reproducirse
        }
    }

    fun liberar() {
        tts?.stop()
        tts?.shutdown()
        tts = null

        mediaPlayer?.runCatching {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }
}