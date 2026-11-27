package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explora2.data.model.PlanificacionDto
import com.example.explora2.model.ConsultaUtpUiState
import com.example.explora2.repository.PlanificacionRepository
import java.net.ConnectException
import java.net.NoRouteToHostException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.explora2.model.GestionErrores
import com.example.explora2.model.GestionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GestionViewModel(
    private val repositorio: PlanificacionRepository = PlanificacionRepository()
) : ViewModel() {
    private val _consulta = MutableStateFlow(ConsultaUtpUiState())
    val consulta: StateFlow<ConsultaUtpUiState> = _consulta
    private var consultaActiva: Job? = null


    private val _estado = MutableStateFlow(GestionUiState())
    val estado: StateFlow<GestionUiState> = _estado

    // Cambios de los campos del docente

    fun onAsignaturaChange(valor: String) {
        _estado.update {
            it.copy(
                asignatura = valor,
                errores = it.errores.copy(asignatura = null),
                validado = false
            )
        }
    }

    fun onUnidadChange(valor: String) {
        _estado.update {
            it.copy(
                unidad = valor,
                errores = it.errores.copy(unidad = null),
                validado = false
            )
        }
    }

    fun onObjetivoChange(valor: String) {
        _estado.update {
            it.copy(
                objetivo = valor,
                errores = it.errores.copy(objetivo = null),
                validado = false
            )
        }
    }

    fun onTipoRecursoChange(valor: String) {
        _estado.update {
            it.copy(
                tipoRecurso = valor,
                errores = it.errores.copy(tipoRecurso = null),
                validado = false
            )
        }
    }

    fun onTituloChange(valor: String) {
        _estado.update {
            it.copy(
                titulo = valor,
                errores = it.errores.copy(titulo = null),
                validado = false
            )
        }
    }

    fun onDescripcionChange(valor: String) {
        _estado.update {
            it.copy(
                descripcion = valor,
                errores = it.errores.copy(descripcion = null),
                validado = false
            )
        }
    }

    // Validaciones del formulario docente

    fun validarFormulario(): Boolean {
        val actual = _estado.value

        val errores = GestionErrores(
            asignatura = if (actual.asignatura.isBlank()) "Campo obligatorio" else null,
            unidad = if (actual.unidad.isBlank()) "Campo obligatorio" else null,
            objetivo = if (actual.objetivo.isBlank()) "Campo obligatorio" else null,
            tipoRecurso = if (actual.tipoRecurso.isBlank()) "Campo obligatorio" else null,
            titulo = if (actual.titulo.isBlank()) "Campo obligatorio" else null,
            descripcion = if (actual.descripcion.isBlank()) "Campo obligatorio" else null
        )

        val hayErrores = listOfNotNull(
            errores.asignatura,
            errores.unidad,
            errores.objetivo,
            errores.tipoRecurso,
            errores.titulo,
            errores.descripcion
        ).isNotEmpty()

        _estado.update {
            it.copy(
                errores = errores,
                validado = !hayErrores
            )
        }

        return !hayErrores
    }

    // Envio del contenido a UTP
    fun enviarAUtp() {
        if (_estado.value.enviando || _estado.value.idRemoto != null || !validarFormulario()) return
        val borrador = _estado.value
        val datos = PlanificacionDto(
            asignatura = borrador.asignatura, unidad = borrador.unidad, objetivo = borrador.objetivo,
            tipoRecurso = borrador.tipoRecurso, titulo = borrador.titulo, descripcion = borrador.descripcion,
            validado = true, enviadoUtp = true, respuestaAlumno = ""
        )
        _estado.update { it.copy(enviando = true, errorEnvio = null) }
        viewModelScope.launch {
            try {
                val id = repositorio.crear(datos)
                _estado.update { it.copy(idRemoto = id, enviadoUtp = true, enviando = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val mensaje = when (e) {
                    is ConnectException, is NoRouteToHostException ->
                        "No se pudo conectar. Conservamos el borrador para reintentar."
                    else -> "No se pudo confirmar el envío. Revisa las solicitudes de UTP antes de volver a enviarlo."
                }
                _estado.update { it.copy(enviando = false, errorEnvio = mensaje) }
            }
        }
    }

    fun cargarSolicitudes() {
        consultaActiva?.cancel()
        _consulta.value = ConsultaUtpUiState()
        consultaActiva = viewModelScope.launch {
            try {
                _consulta.value = ConsultaUtpUiState(cargando = false, solicitudes = repositorio.listarEnviadas())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _consulta.value = ConsultaUtpUiState(cargando = false, error = "No se pudieron cargar las solicitudes.")
            }
        }
    }

    fun consultarDetalle(id: Long) {
        consultaActiva?.cancel()
        _consulta.update { it.copy(cargando = true, detalle = null, error = null) }
        consultaActiva = viewModelScope.launch {
            try {
                val plan = repositorio.obtenerPorId(id)
                _consulta.update { it.copy(cargando = false, detalle = plan) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val mensaje = if (e is HttpException && e.code() == 404)
                    "Esta solicitud ya no está disponible." else "No se pudo cargar el detalle."
                _consulta.update { it.copy(cargando = false, error = mensaje) }
            }
        }
    }

    // Autorizacion del contenido por UTP (habilita directamente al alumno)
    fun aprobarContenido() {
        _estado.update {
            it.copy(
                enviadoUtp = true,
                autorizado = true,
                asignadoAlumno = true
            )
        }
    }

    // Asignacion del contenido al estudiante (garantiza que ambos queden en true)
    fun asignarAlAlumno() {
        _estado.update {
            it.copy(
                autorizado = true,
                asignadoAlumno = true
            )
        }
    }

    // Actualizacion de la respuesta del alumno
    fun onRespuestaAlumnoChange(valor: String) {
        _estado.update {
            it.copy(
                respuestaAlumno = valor,
                errorRespuesta = null
            )
        }
    }

    // Entrega y validacion de la respuesta
    fun entregarRespuesta() {
        val actual = _estado.value

        if (actual.respuestaAlumno.isBlank()) {
            _estado.update {
                it.copy(errorRespuesta = "Debes escribir una respuesta")
            }
        } else {
            _estado.update {
                it.copy(
                    respuestaEntregada = true,
                    errorRespuesta = null
                )
            }
        }
    }

    // Reinicia el formulario para redactar una nueva planificacion
    fun nuevaPlanificacion() {
        _estado.update {
            GestionUiState()
        }
    }

}