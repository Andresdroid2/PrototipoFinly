package com.example.prototipofinly.ui.screens


import androidx.compose.foundation.clickable
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
import com.example.prototipofinly.ui.screens.gastos.GastosViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val formatoFecha: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())

@Composable
fun HistorialScreen(vm: GastosViewModel = viewModel()) {
    val historial by vm.historial.collectAsStateWithLifecycle()
    var expandidas by remember { mutableStateOf(setOf<Long>()) }

    if (historial.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no hay categorías")
        }
        return
    }

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        items(historial, key = { it.categoria.id }) { item ->
            val id = item.categoria.id
            val abierta = id in expandidas

            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable { expandidas = if (abierta) expandidas - id else expandidas + id }
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.categoria.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${item.gastos.size} gastos",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        "$${"%.2f".format(item.total)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (abierta) {
                    Spacer(Modifier.height(8.dp))
                    if (item.gastos.isEmpty()) {
                        Text("Sin gastos", style = MaterialTheme.typography.bodySmall)
                    }
                    item.gastos.sortedByDescending { it.fecha }.forEach { gasto ->
                        Row(
                            Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(gasto.nombre, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    formatoFecha.format(Instant.ofEpochMilli(gasto.fecha)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text("$${"%.2f".format(gasto.monto)}")
                        }
                    }
                }
            }
            HorizontalDivider()
        }
    }
}