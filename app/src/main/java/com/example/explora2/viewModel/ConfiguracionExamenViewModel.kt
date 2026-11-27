package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.data.model.ConfiguracionExamenDto
import com.example.explora2.model.ConfiguracionExamenUiState
import com.example.explora2.repository.ConfiguracionExamenRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class ConfiguracionExamenViewModel(
    private val repositorio: ConfiguracionExamenRepository =
        ConfiguracionExamenRepository()
) : ViewModel() {

    private val _estado =
        MutableStateFlow(ConfiguracionExamenUiState())

    val estado: StateFlow<ConfiguracionExamenUiState> =
        _estado

    private var cargaActiva: Job? = null

    fun cargar() {

        if (_estado.value.guardando) return

        cargaActiva?.cancel()

        _estado.update {
            it.copy(
                cargando = true,
                cargado = false,
                error = null,
                mensaje = null
            )
        }

        cargaActiva = viewModelScope.launch {
            try {
                val configuracion = repositorio.obtener()

                _estado.update {
                    it.copy(
                        borrador = configuracion
                            ?: ConfiguracionExamenDto(),
                        cargando = false,
                        cargado = true
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _estado.update {
                    it.copy(
                        cargando = false,
                        cargado = false,
                        error = "No se pudo cargar la configuración."
                    )
                }
            }
        }
    }

    fun cambiarBorrador(datos: ConfiguracionExamenDto) {

        val actual = _estado.value

        if (actual.cargando ||
            actual.guardando ||
            !actual.cargado
        ) return

        _estado.update {
            it.copy(
                borrador = datos,
                error = null,
                mensaje = null
            )
        }
    }

    private fun validar(
        datos: ConfiguracionExamenDto
    ): String? {

        if (datos.intentosPermitidos !in 1..5) {
            return "Los intentos deben estar entre 1 y 5."
        }

        if (datos.version.isNullOrBlank()) {
            return "Debes seleccionar una versión."
        }

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.ROOT
        ).apply {
            isLenient = false
        }

        val patron = Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}")

        val inicioTexto = datos.fechaInicio.orEmpty()
        val finTexto = datos.fechaFin.orEmpty()

        if (!patron.matches(inicioTexto) ||
            !patron.matches(finTexto)
        ) {
            return "Utiliza fechas con formato DD/MM/AAAA."
        }

        val inicio = runCatching {
            formato.parse(inicioTexto)
        }.getOrNull()

        val fin = runCatching {
            formato.parse(finTexto)
        }.getOrNull()

        if (inicio == null || fin == null) {
            return "Debes ingresar fechas válidas."
        }

        if (fin.before(inicio)) {
            return "La fecha de cierre no puede ser anterior al inicio."
        }

        return null
    }

    fun guardar() {

        val actual = _estado.value

        if (actual.guardando ||
            actual.cargando ||
            !actual.cargado
        ) return

        val datos = actual.borrador
        val errorValidacion = validar(datos)

        if (errorValidacion != null) {
            _estado.update {
                it.copy(
                    error = errorValidacion,
                    mensaje = null
                )
            }
            return
        }

        _estado.update {
            it.copy(
                guardando = true,
                error = null,
                mensaje = null
            )
        }

        viewModelScope.launch {
            try {
                val guardada = repositorio.guardar(datos)

                _estado.update {
                    it.copy(
                        borrador = guardada,
                        mensaje = "Configuración guardada en el servidor."
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _estado.update {
                    it.copy(
                        error = "No se pudo confirmar el guardado. Revisa la configuración antes de reintentar."
                    )
                }

            } finally {
                _estado.update {
                    it.copy(guardando = false)
                }
            }
        }
    }
}
