package com.example.veterinariaapp.utils

import java.text.NumberFormat
import java.util.Locale

object ValidacionesUtils {

    fun validarTextoNoVacio(valor: String, campo: String) {
        require(valor.isNotBlank()) { "El campo '$campo' no puede estar vacío." }
    }

    fun validarMontoPositivo(monto: Int, campo: String) {
        require(monto > 0) { "El campo '$campo' debe ser mayor a 0." }
    }

    fun formatearClp(monto: Int): String {
        val formato = NumberFormat.getNumberInstance(Locale("es", "CL"))
        return "$ ${formato.format(monto)}"
    }
}