package com.example.prototipofinly.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prototipofinly.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.models.CategoriaConGastos
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.ui.screens.gastos.GastosViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val formatoFecha: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())

@Composable
fun HistorialScreen(vm: GastosViewModel = viewModel()) {
    val historial by vm.historial.collectAsStateWithLifecycle()
    HistorialContent(historial = historial)
}

@Composable
fun HistorialContent(
    historial: List<CategoriaConGastos>,
    expandidasIniciales: Set<Long> = emptySet()
) {
    var expandidas by remember { mutableStateOf(expandidasIniciales) }

    if (historial.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no hay categorías")
        }
        return
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(Color.White)){
        Image(
            painter = painterResource(R.drawable.icon3),
            contentDescription = "Logo de historial",
            modifier = Modifier.size(200.dp)
        )
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            items(historial, key = { it.categoria.id }) { item ->
                val id = item.categoria.id
                val abierta = id in expandidas

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandidas = if (abierta) expandidas - id else expandidas + id
                        }
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                item.categoria.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )
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
                                Modifier.fillMaxWidth()
                                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
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
}


private val historialDeEjemplo = listOf(
    CategoriaConGastos(
        categoria = Categoria(id = 1, nombre = "Comida"),
        gastos = listOf(
            Gasto(
                id = 1, nombre = "Almuerzo", descripcion = "Menú del día",
                monto = 15000.0, categoriaId = 1, usuarioId = 1,
                fecha = 1_760_000_000_000
            ),
            Gasto(
                id = 2, nombre = "Café", descripcion = "",
                monto = 4500.0, categoriaId = 1, usuarioId = 1,
                fecha = 1_760_100_000_000
            )
        )
    ),
    CategoriaConGastos(
        categoria = Categoria(id = 2, nombre = "Transporte"),
        gastos = listOf(
            Gasto(
                id = 3, nombre = "Bus", descripcion = "",
                monto = 3200.0, categoriaId = 2, usuarioId = 1,
                fecha = 1_760_200_000_000
            )
        )
    ),
    CategoriaConGastos(
        categoria = Categoria(id = 3, nombre = "Ocio"),
        gastos = emptyList()
    )
)

@Preview(showBackground = true, showSystemUi = true, name = "Contraído")
@Composable
private fun HistorialContraidoPreview() {
    MaterialTheme {
        HistorialContent(historial = historialDeEjemplo)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Expandido")
@Composable
private fun HistorialExpandidoPreview() {
    MaterialTheme {
        HistorialContent(
            historial = historialDeEjemplo,
            expandidasIniciales = setOf(1L, 3L)   // Comida y Ocio (sin gastos) abiertas
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Vacío")
@Composable
private fun HistorialVacioPreview() {
    MaterialTheme {
        HistorialContent(historial = emptyList())
    }
}