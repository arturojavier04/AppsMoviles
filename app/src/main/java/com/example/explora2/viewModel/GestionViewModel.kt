
package com.example.explora2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.explora2.model.GestionErrores
import com.example.explora2.model.GestionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GestionViewModel : ViewModel() {

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
            asignatura = if (actual.asignatura.isBlank())
                "Campo obligatorio" else null,

            unidad = if (actual.unidad.isBlank())
                "Campo obligatorio" else null,

            objetivo = if (actual.objetivo.isBlank())
                "Campo obligatorio" else null,

            tipoRecurso = if (actual.tipoRecurso.isBlank())
                "Campo obligatorio" else null,

            titulo = if (actual.titulo.isBlank())
                "Campo obligatorio" else null,

            descripcion = if (actual.descripcion.isBlank())
                "Campo obligatorio" else null
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
        if (_estado.value.validado &&
            !_estado.value.enviadoUtp) {

            _estado.update {
                it.copy(enviadoUtp = true)
            }
        }
    }

    // Autorizacion del contenido

    fun aprobarContenido() {
        if (_estado.value.enviadoUtp) {
            _estado.update {
                it.copy(autorizado = true)
            }
        }
    }

    // Asignacion del contenido

    fun asignarAlAlumno() {
        if (_estado.value.autorizado) {
            _estado.update {
                it.copy(asignadoAlumno = true)
            }
        }
    }

    // Actualizacion de la respuesta del alumno

    fun onRespuestaAlumnoChange(valor: String) {

        if (_estado.value.asignadoAlumno &&
            !_estado.value.respuestaEntregada) {

            _estado.update {
                it.copy(
                    respuestaAlumno = valor,
                    errorRespuesta = null
                )
            }
        }
    }

    // Entrega y validacion de la respuesta

    fun entregarRespuesta() {

        val actual = _estado.value

        if (actual.autorizado &&
            actual.asignadoAlumno &&
            !actual.respuestaEntregada) {

            if (actual.respuestaAlumno.isBlank()) {

                _estado.update {
                    it.copy(
                        errorRespuesta = "Debes escribir una respuesta"
                    )
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
    }
}
