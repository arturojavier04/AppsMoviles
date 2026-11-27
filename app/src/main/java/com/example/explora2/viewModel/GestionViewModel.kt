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
import com.example.explora2.model.ConsultaDocenteUiState
import com.example.explora2.model.ConsultaAlumnoUiState
import com.example.explora2.model.RevisionEntregasUiState


class GestionViewModel(
    private val repositorio: PlanificacionRepository = PlanificacionRepository()
) : ViewModel() {
    private val _consulta = MutableStateFlow(ConsultaUtpUiState())
    val consulta: StateFlow<ConsultaUtpUiState> = _consulta
    private var consultaActiva: Job? = null


    private val _consultaDocente =
        MutableStateFlow(ConsultaDocenteUiState())

    val consultaDocente: StateFlow<ConsultaDocenteUiState> =
        _consultaDocente

    private var consultaDocenteActiva: Job? = null


    private val _consultaAlumno =
        MutableStateFlow(ConsultaAlumnoUiState())

    val consultaAlumno: StateFlow<ConsultaAlumnoUiState> =
        _consultaAlumno

    private var consultaAlumnoActiva: Job? = null
    private var detalleAlumnoActivo: Job? = null



    private val _revisionEntregas =
        MutableStateFlow(RevisionEntregasUiState())

    val revisionEntregas: StateFlow<RevisionEntregasUiState> =
        _revisionEntregas

    private var revisionEntregasActiva: Job? = null





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
        if (_consulta.value.autorizando) return
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
        if (_consulta.value.autorizando) return
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


    fun autorizarPlanificacion(id: Long) {

        if (_consulta.value.autorizando ||
            _consulta.value.cargando
        ) return

        _consulta.update {
            it.copy(
                autorizando = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                val actual = repositorio.obtenerPorId(id)

                if (!actual.validado || !actual.enviadoUtp) {
                    _consulta.update {
                        it.copy(
                            error = "La planificación no está lista para autorizarse."
                        )
                    }
                    return@launch
                }

                val actualizado = if (actual.autorizado) {
                    actual
                } else {
                    repositorio.actualizar(
                        id,
                        actual.copy(autorizado = true)
                    )
                }

                if (!actualizado.autorizado) {
                    throw IllegalStateException(
                        "El servidor no confirmó la autorización"
                    )
                }

                _consulta.update { estado ->
                    estado.copy(
                        detalle = actualizado,
                        solicitudes = estado.solicitudes.map {
                            if (it.id == id) actualizado else it
                        },
                        error = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                val mensaje =
                    if (e is HttpException && e.code() == 404) {
                        "La planificación ya no existe."
                    } else {
                        "No se pudo confirmar la autorización. Actualiza la lista."
                    }

                _consulta.update {
                    it.copy(error = mensaje)
                }

            } finally {
                _consulta.update {
                    it.copy(autorizando = false)
                }
            }
        }
    }

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

    fun cargarPlanificacionesDocente() {

        if (_consultaDocente.value.asignandoId != null) return

        consultaDocenteActiva?.cancel()

        _consultaDocente.update {
            it.copy(
                cargando = true,
                error = null
            )
        }

        consultaDocenteActiva = viewModelScope.launch {
            try {
                val planificaciones =
                    repositorio.listarAutorizadas()

                _consultaDocente.update {
                    it.copy(
                        cargando = false,
                        planificaciones = planificaciones,
                        error = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _consultaDocente.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron cargar las planificaciones."
                    )
                }
            }
        }
    }

    fun asignarPlanificacion(id: Long) {

        if (_consultaDocente.value.asignandoId != null ||
            _consultaDocente.value.cargando
        ) return

        _consultaDocente.update {
            it.copy(
                asignandoId = id,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                val actual = repositorio.obtenerPorId(id)

                if (!actual.validado ||
                    !actual.enviadoUtp ||
                    !actual.autorizado
                ) {
                    _consultaDocente.update {
                        it.copy(
                            error = "La planificación debe estar autorizada por UTP."
                        )
                    }

                    return@launch
                }

                val actualizado = if (actual.asignadoAlumno) {
                    actual
                } else {
                    repositorio.actualizar(
                        id,
                        actual.copy(asignadoAlumno = true)
                    )
                }

                if (!actualizado.asignadoAlumno) {
                    throw IllegalStateException(
                        "El servidor no confirmó la asignación"
                    )
                }

                _consultaDocente.update { estado ->
                    estado.copy(
                        planificaciones = estado.planificaciones.map {
                            if (it.id == id) actualizado else it
                        },
                        error = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                val mensaje =
                    if (e is HttpException && e.code() == 404) {
                        "La planificación ya no existe."
                    } else {
                        "No se pudo confirmar la asignación. Actualiza la lista."
                    }

                _consultaDocente.update {
                    it.copy(error = mensaje)
                }

            } finally {
                _consultaDocente.update {
                    it.copy(asignandoId = null)
                }
            }
        }
    }


    fun asignarAlAlumno() {
        _estado.update {
            it.copy(
                autorizado = true,
                asignadoAlumno = true
            )
        }
    }

    // Actualizacion de la respuesta del alumno


    fun cargarActividadesAlumno() {




        if (_consultaAlumno.value.enviando) return

        consultaAlumnoActiva?.cancel()

        _consultaAlumno.update {
            it.copy(
                cargando = true,
                error = null
            )
        }

        consultaAlumnoActiva = viewModelScope.launch {
            try {
                val actividades = repositorio.listarAsignadas()

                _consultaAlumno.update {
                    it.copy(
                        cargando = false,
                        actividades = actividades
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _consultaAlumno.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron cargar las actividades."
                    )
                }
            }
        }
    }


    fun abrirActividadAlumno(id: Long) {

        if (_consultaAlumno.value.enviando) return

        detalleAlumnoActivo?.cancel()

        _consultaAlumno.update {
            it.copy(
                cargandoDetalle = true,
                detalle = null,
                respuesta = "",
                errorDetalle = null,
                errorRespuesta = null
            )
        }

        detalleAlumnoActivo = viewModelScope.launch {
            try {
                val plan = repositorio.obtenerPorId(id)

                if (plan.id != id ||
                    !plan.validado ||
                    !plan.enviadoUtp ||
                    !plan.autorizado ||
                    !plan.asignadoAlumno
                ) {
                    _consultaAlumno.update {
                        it.copy(
                            cargandoDetalle = false,
                            errorDetalle = "Esta actividad no está disponible."
                        )
                    }
                    return@launch
                }

                _consultaAlumno.update {
                    it.copy(
                        cargandoDetalle = false,
                        detalle = plan,
                        respuesta = plan.respuestaAlumno.orEmpty()
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _consultaAlumno.update {
                    it.copy(
                        cargandoDetalle = false,
                        errorDetalle = "No se pudo abrir la actividad."
                    )
                }
            }
        }
    }

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


    fun cambiarRespuestaRemota(valor: String) {

        val actual = _consultaAlumno.value

        if (actual.enviando ||
            actual.detalle == null ||
            actual.detalle.respuestaEntregada
        ) return

        _consultaAlumno.update {
            it.copy(
                respuesta = valor,
                errorRespuesta = null
            )
        }
    }


    fun entregarRespuestaRemota() {

        val estado = _consultaAlumno.value
        val detalle = estado.detalle ?: return
        val id = detalle.id ?: return

        if (estado.enviando ||
            estado.cargandoDetalle ||
            detalle.respuestaEntregada
        ) return

        val respuesta = estado.respuesta.trim()

        if (respuesta.isBlank()) {
            _consultaAlumno.update {
                it.copy(
                    errorRespuesta = "Debes escribir una respuesta."
                )
            }
            return
        }

        _consultaAlumno.update {
            it.copy(
                enviando = true,
                errorRespuesta = null
            )
        }

        viewModelScope.launch {
            try {
                val actual = repositorio.obtenerPorId(id)

                if (actual.id != id ||
                    !actual.validado ||
                    !actual.enviadoUtp ||
                    !actual.autorizado ||
                    !actual.asignadoAlumno
                ) {
                    _consultaAlumno.update {
                        it.copy(
                            errorRespuesta = "La actividad ya no está disponible."
                        )
                    }
                    return@launch
                }

                val actualizado = if (actual.respuestaEntregada) {
                    actual
                } else {
                    repositorio.actualizar(
                        id,
                        actual.copy(
                            respuestaAlumno = respuesta,
                            respuestaEntregada = true
                        )
                    ).also {
                        if (it.respuestaAlumno != respuesta) {
                            throw IllegalStateException(
                                "La respuesta no coincide"
                            )
                        }
                    }
                }

                if (!actualizado.respuestaEntregada) {
                    throw IllegalStateException(
                        "El servidor no confirmó la entrega"
                    )
                }

                _consultaAlumno.update { anterior ->
                    anterior.copy(
                        detalle = actualizado,
                        respuesta =
                            actualizado.respuestaAlumno.orEmpty(),
                        actividades = anterior.actividades.map {
                            if (it.id == id) actualizado else it
                        },
                        errorRespuesta = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _consultaAlumno.update {
                    it.copy(
                        errorRespuesta =
                            "No se pudo confirmar la entrega. Intenta nuevamente."
                    )
                }

            } finally {
                _consultaAlumno.update {
                    it.copy(enviando = false)
                }
            }
        }
    }


    // Reinicia el formulario para redactar una nueva planificacion


    fun cargarEntregasDocente() {

        revisionEntregasActiva?.cancel()

        _revisionEntregas.update {
            it.copy(
                cargando = true,
                error = null
            )
        }

        revisionEntregasActiva = viewModelScope.launch {
            try {
                val entregas = repositorio.listarEntregadas()

                _revisionEntregas.update {
                    it.copy(
                        cargando = false,
                        entregas = entregas,
                        error = null
                    )
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _revisionEntregas.update {
                    it.copy(
                        cargando = false,
                        entregas = emptyList(),
                        error = "No se pudieron cargar las entregas."
                    )
                }
            }
        }
    }

    fun nuevaPlanificacion() {
        _estado.update {
            GestionUiState()
        }
    }

}