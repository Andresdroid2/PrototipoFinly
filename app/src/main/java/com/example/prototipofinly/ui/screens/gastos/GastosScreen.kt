package com.example.prototipofinly.ui.screens.gastos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prototipofinly.data.models.Categoria
import kotlin.text.format

@Composable
fun GastosScreen(vm: GastosViewModel = viewModel()) {
    val gastos by vm.gastos.collectAsStateWithLifecycle()
    val categorias by vm.categorias.collectAsStateWithLifecycle()
    val total by vm.total.collectAsStateWithLifecycle()
    val totales by vm.totalesPorCategoria.collectAsStateWithLifecycle()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var elegida by remember { mutableStateOf<Categoria?>(null) }
    var menuAbierto by remember { mutableStateOf(false) }

    // Si el usuario no ha elegido, usa la primera categoría
    val categoriaActual = elegida ?: categorias.firstOrNull()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Total: $${"%.2f".format(total)}", style = MaterialTheme.typography.headlineMedium)

        if (totales.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            totales.forEach {
                Text("${it.nombre}: $${"%.2f".format(it.total)}", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(Modifier.padding(vertical = 8.dp)) {
            OutlinedButton(onClick = { menuAbierto = true }) {
                Text(categoriaActual?.nombre ?: "Categoría")
            }
            DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria.nombre) },
                        onClick = {
                            elegida = categoria
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                val cat = categoriaActual ?: return@Button
                vm.agregar(
                    nombre = nombre,
                    descripcion = descripcion,
                    monto = monto.toDoubleOrNull() ?: 0.0,
                    categoriaId = cat.id
                )
                nombre = ""
                descripcion = ""
                monto = ""
            },
            enabled = categoriaActual != null && nombre.isNotBlank()
        ) { Text("Agregar gasto") }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(gastos, key = { it.gasto.id }) { item ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(item.gasto.nombre, style = MaterialTheme.typography.bodyLarge)
                        if (item.gasto.descripcion.isNotBlank()) {
                            Text(item.gasto.descripcion, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(item.categoria.nombre, style = MaterialTheme.typography.labelSmall)
                    }
                    Text("$${"%.2f".format(item.gasto.monto)}")
                    TextButton(onClick = { vm.eliminar(item.gasto) }) { Text("X") }
                }
                HorizontalDivider()
            }
        }
    }
}