package com.example.veterinariaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veterinariaapp.data.model.ResumenVeterinaria
import com.example.veterinariaapp.ui.viewmodel.VeterinariaViewModel
import com.example.veterinariaapp.utils.ValidacionesUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    modifier: Modifier = Modifier,
    vm: VeterinariaViewModel = viewModel()
) {
    val resumen by vm.resumen.observeAsState(ResumenVeterinaria())
    val pedidos by vm.pedidos.observeAsState(emptyList())
    val cargando by vm.cargando.observeAsState(false)
    val errorMensaje by vm.errorMensaje.observeAsState(null)

    var nombreDueño by remember { mutableStateOf("") }
    var nombreMascota by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Perro") }
    var motivo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Veterinaria - Gestión Clínica", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cargando registros de veterinaria...")
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Resumen del Sistema",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TarjetaResumen(
                            titulo = "CLIENTES REGISTRADOS",
                            valor = "${resumen.totalClientes}",
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaResumen(
                            titulo = "PEDIDOS REALIZADOS",
                            valor = "${resumen.totalPedidos}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    TarjetaResumen(
                        titulo = "ÚLTIMO CLIENTE REGISTRADO",
                        valor = resumen.ultimoCliente,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Registrar Nuevo Cliente y Consulta", fontWeight = FontWeight.Bold)

                            OutlinedTextField(
                                value = nombreDueño,
                                onValueChange = { nombreDueño = it },
                                label = { Text("Nombre del dueño") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = nombreMascota,
                                onValueChange = { nombreMascota = it },
                                label = { Text("Nombre de la mascota") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = especie,
                                    onValueChange = { especie = it },
                                    label = { Text("Especie") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = motivo,
                                    onValueChange = { motivo = it },
                                    label = { Text("Motivo consulta") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            errorMensaje?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Button(
                                onClick = {
                                    vm.agregarAtencion(
                                        nombreDueño = nombreDueño,
                                        nombreMascota = nombreMascota,
                                        especie = especie,
                                        motivo = motivo
                                    )
                                    if (nombreDueño.isNotBlank() && nombreMascota.isNotBlank()) {
                                        nombreDueño = ""
                                        nombreMascota = ""
                                        motivo = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("+ Registrar Cliente y Pedido")
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Mascotas, Dueños y Consultas (${pedidos.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(pedidos) { pedido ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Mascota: ${pedido.cliente.nombreMascota} (${pedido.cliente.especie})",
                                fontWeight = FontWeight.Bold
                            )
                            Text("Dueño: ${pedido.cliente.nombreDueño}")
                            Text("Consulta #${pedido.id}: ${pedido.motivoConsulta}")
                            Text(
                                text = "Total: ${ValidacionesUtils.formatearClp(pedido.calcularTotal())}",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaResumen(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.labelSmall)
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}