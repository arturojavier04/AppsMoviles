package com.example.explora2.navigation

sealed class DestinoRuta(val ruta: String) {
    data object Portada : DestinoRuta("portada_inicio")
    data object Materias : DestinoRuta("materias_nivel")
    data object ModuloDetalle : DestinoRuta("modulo_detalle")
    data object Seguimiento : DestinoRuta("seguimiento_avance")
    data object MiAvatar : DestinoRuta("mi_avatar")
    data object Formulario : DestinoRuta("formulario_registro")
    data object FichaResumen : DestinoRuta("ficha_resumen")
}