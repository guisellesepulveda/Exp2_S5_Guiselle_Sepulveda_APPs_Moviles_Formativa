package com.example.veterinariaapp.data.model

import com.example.veterinariaapp.utils.ValidacionesUtils

data class Cliente(
    val id: Int,
    val nombreDueño: String,
    val nombreMascota: String,
    val especie: String,
    val telefono: String
) {
    init {
        ValidacionesUtils.validarTextoNoVacio(nombreDueño, "Nombre del dueño")
        ValidacionesUtils.validarTextoNoVacio(nombreMascota, "Nombre de la mascota")
    }
}

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Int
) {
    init {
        ValidacionesUtils.validarTextoNoVacio(nombre, "Nombre del servicio/producto")
        ValidacionesUtils.validarMontoPositivo(precio, "Precio")
    }
}

data class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<Producto>,
    val motivoConsulta: String
) {
    fun calcularTotal(): Int = productos.sumOf { it.precio }
}

data class ResumenVeterinaria(
    val totalClientes: Int = 0,
    val totalPedidos: Int = 0,
    val ultimoCliente: String = "Sin registros"
)