import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.explora2.R

private fun reproducirCryDarkrai(
    urlAudio: String,
    onPlayerCreated: (MediaPlayer) -> Unit
) {
    try {
        val player = MediaPlayer().apply {
            setDataSource(urlAudio)
            setOnPreparedListener {
                onPlayerCreated(it)
                it.start()
            }
            setOnCompletionListener {
                it.release()
            }
            setOnErrorListener { mp, _, _ ->
                mp.release()
                true
            }
            prepareAsync()
        }
        onPlayerCreated(player)
    } catch (_: Exception) {
    }
}

@Composable
fun FeedbackDarkraiPantallaCompleta(
    visible: Boolean,
    explicacion: String,
    puedeReintentar: Boolean,
    onContinuar: () -> Unit
) {
    if (!visible) return

    val colorBordeNegro = Color(0xFF1E1E1E)
    val urlCryDarkrai = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/491.ogg"
    var yaSonoAlPasarDedo by remember { mutableStateOf(false) }
    var reproductorActivo by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            reproductorActivo?.runCatching {
                if (isPlaying) stop()
                release()
            }
            reproductorActivo = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEDE7F6).copy(alpha = 0.98f),
                        Color(0xFFD1C4E9).copy(alpha = 0.98f)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "¡Casi lo logras!",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF311B92),
                textAlign = TextAlign.Center
            )

            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/491.png",
                placeholder = painterResource(id = R.drawable.poke),
                error = painterResource(id = R.drawable.poke),
                contentDescription = "Darkrai al responder de forma incorrecta",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .pointerInput(urlCryDarkrai) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    PointerEventType.Press, PointerEventType.Move -> {
                                        if (!yaSonoAlPasarDedo) {
                                            yaSonoAlPasarDedo = true
                                            reproducirCryDarkrai(urlCryDarkrai) { mp ->
                                                reproductorActivo = mp
                                            }
                                        }
                                    }
                                    PointerEventType.Release, PointerEventType.Exit -> {
                                        yaSonoAlPasarDedo = false
                                    }
                                }
                            }
                        }
                    },
                contentScale = ContentScale.Fit
            )

            Text(
                text = explicacion,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(3.dp, colorBordeNegro),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB300),
                    contentColor = colorBordeNegro
                )
            ) {
                Text(
                    text = if (puedeReintentar) "¡Intentar de nuevo! 💪" else "Entendido 👍",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

//datos