package com.example.veterinariaapp.data.repository

import com.example.veterinariaapp.data.model.Cliente
import com.example.veterinariaapp.data.model.Pedido
import com.example.veterinariaapp.data.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class VeterinariaRepository {

    private val catalogoInicial = listOf(
        Producto(1, "Consulta General", "Consulta", 18000),
        Producto(2, "Vacuna Séxtuple", "Vacuna", 22000),
        Producto(3, "Desparasitación Interna", "Medicamento", 12000),
        Producto(4, "Alimento Premium 3kg", "Alimento", 25000)
    )

    private val clientes = mutableListOf(
        Cliente(1, "Camila Rojas", "Max", "Perro", "+56911112222"),
        Cliente(2, "Matías Soto", "Luna", "Gato", "+56933334444"),
        Cliente(3, "Diego Muñoz", "Toby", "Perro", "+56955556666")
    )

    private val pedidos = mutableListOf(
        Pedido(101, clientes[0], listOf(catalogoInicial[0], catalogoInicial[2]), "Control anual"),
        Pedido(102, clientes[1], listOf(catalogoInicial[1]), "Refuerzo vacuna")
    )

    suspend fun obtenerClientes(): List<Cliente> = withContext(Dispatchers.IO) {
        delay(1000)
        clientes.toList()
    }

    suspend fun obtenerPedidos(): List<Pedido> = withContext(Dispatchers.IO) {
        delay(500)
        pedidos.toList()
    }

    fun obtenerCatalogo(): List<Producto> = catalogoInicial

    suspend fun registrarClienteYConsulta(
        nombreDueño: String,
        nombreMascota: String,
        especie: String,
        motivo: String,
        productoSeleccionado: Producto
    ): Pair<List<Cliente>, List<Pedido>> = withContext(Dispatchers.IO) {
        delay(400)
        val nuevoCliente = Cliente(
            id = clientes.size + 1,
            nombreDueño = nombreDueño.trim(),
            nombreMascota = nombreMascota.trim(),
            especie = especie.ifBlank { "Mascota" }.trim(),
            telefono = "+56900000000"
        )
        clientes.add(nuevoCliente)

        val nuevoPedido = Pedido(
            id = 100 + pedidos.size + 1,
            cliente = nuevoCliente,
            productos = listOf(productoSeleccionado),
            motivoConsulta = motivo.ifBlank { "Atención general" }
        )
        pedidos.add(nuevoPedido)

        Pair(clientes.toList(), pedidos.toList())
    }
}