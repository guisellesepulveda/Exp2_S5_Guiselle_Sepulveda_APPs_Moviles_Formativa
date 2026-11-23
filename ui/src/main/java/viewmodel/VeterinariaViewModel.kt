package com.example.veterinariaapp.ui.viewmodel

import com.example.veterinariaapp.data.repository.VeterinariaRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinariaapp.data.model.Cliente
import com.example.veterinariaapp.data.model.Pedido
import com.example.veterinariaapp.data.model.ResumenVeterinaria
import kotlinx.coroutines.launch

class VeterinariaViewModel(
    private val repository: VeterinariaRepository = VeterinariaRepository()
) : ViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>(emptyList())
    val clientes: LiveData<List<Cliente>> = _clientes

    private val _pedidos = MutableLiveData<List<Pedido>>(emptyList())
    val pedidos: LiveData<List<Pedido>> = _pedidos

    private val _resumen = MutableLiveData(ResumenVeterinaria())
    val resumen: LiveData<ResumenVeterinaria> = _resumen

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _errorMensaje = MutableLiveData<String?>(null)
    val errorMensaje: LiveData<String?> = _errorMensaje

    init {
        cargarDatosIniciales()
    }

    fun cargarDatosIniciales() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val listaClientes = repository.obtenerClientes()
                val listaPedidos = repository.obtenerPedidos()
                _clientes.value = listaClientes
                _pedidos.value = listaPedidos
                actualizarResumen(listaClientes, listaPedidos)
                _errorMensaje.value = null
            } catch (e: Exception) {
                _errorMensaje.value = "Error al cargar datos: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun agregarAtencion(
        nombreDueño: String,
        nombreMascota: String,
        especie: String,
        motivo: String
    ) {
        viewModelScope.launch {
            try {
                val servicio = repository.obtenerCatalogo().first()
                val (nuevosClientes, nuevosPedidos) = repository.registrarClienteYConsulta(
                    nombreDueño = nombreDueño,
                    nombreMascota = nombreMascota,
                    especie = especie,
                    motivo = motivo,
                    productoSeleccionado = servicio
                )
                _clientes.value = nuevosClientes
                _pedidos.value = nuevosPedidos
                actualizarResumen(nuevosClientes, nuevosPedidos)
                _errorMensaje.value = null
            } catch (e: IllegalArgumentException) {
                _errorMensaje.value = e.message
            }
        }
    }

    private fun actualizarResumen(listaClientes: List<Cliente>, listaPedidos: List<Pedido>) {
        _resumen.value = ResumenVeterinaria(
            totalClientes = listaClientes.size,
            totalPedidos = listaPedidos.size,
            ultimoCliente = listaClientes.lastOrNull()?.nombreDueño ?: "Sin registros"
        )
    }
}