package com.example.prototipofinly.ui.screens.gastos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prototipofinly.R
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.data.models.GastoConCategoria
import com.example.prototipofinly.data.models.TotalPorCategoria
import com.example.prototipofinly.ui.theme.titanone
import kotlin.text.format


@Composable
fun GastosScreen(vm: GastosViewModel = viewModel()) {
    val gastos by vm.gastos.collectAsStateWithLifecycle()
    val categorias by vm.categorias.collectAsStateWithLifecycle()
    val total by vm.total.collectAsStateWithLifecycle()
    val totales by vm.totalesPorCategoria.collectAsStateWithLifecycle()

    GastosContent(
        gastos = gastos,
        categorias = categorias,
        total = total,
        totales = totales,
        onAgregar = vm::agregar,
        onEliminar = vm::eliminar
    )
}

@Composable
fun GastosContent(
    gastos: List<GastoConCategoria>,
    categorias: List<Categoria>,
    total: Double,
    totales: List<TotalPorCategoria>,
    onAgregar: (nombre: String, descripcion: String, monto: Double, categoriaId: Long) -> Unit,
    onEliminar: (Gasto) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var elegida by remember { mutableStateOf<Categoria?>(null) }
    var menuAbierto by remember { mutableStateOf(false) }

    val categoriaActual = elegida ?: categorias.firstOrNull()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.icon2),
            modifier = Modifier.size(140.dp),
            contentDescription = "Logo de la app"
        )

        Column(Modifier.fillMaxSize().clip(ShapeDefaults.ExtraLarge.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)))
            .background(color = Color(252, 203, 84)).padding(10.dp)) {
            Text("Total \n $${"%.2f".format(total)}", style = MaterialTheme.typography.headlineSmall,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = titanone, color = Color.Black)

            if (totales.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                totales.forEach {
                    Text(
                        "${it.nombre}: $${"%.2f".format(it.total)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text("Nombre", color = Color.Gray,fontFamily = titanone, fontSize = 14.sp) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                placeholder = { Text("Descripción (opcional)", color = Color.Gray,fontFamily = titanone, fontSize = 14.sp)  },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                placeholder = { Text("Monto", color = Color.Gray,fontFamily = titanone, fontSize = 15.sp) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Box(Modifier.padding(vertical = 8.dp)) {
                    OutlinedButton(
                        onClick = { menuAbierto = true },
                        colors = ButtonDefaults.buttonColors(Color(0xFF2F2D28))
                    ) {
                        Text(
                            categoriaActual?.nombre ?: "Categoría",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }) {
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
                        onAgregar(nombre, descripcion, monto.toDoubleOrNull() ?: 0.0, cat.id)
                        nombre = ""
                        descripcion = ""
                        monto = ""
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF2F2D28)),
                    enabled = categoriaActual != null && nombre.isNotBlank()
                ) { Text("Agregar gasto") }
            }
            Spacer(Modifier.height(8.dp))

            LazyColumn(Modifier.fillMaxSize()) {
                items(gastos, key = { it.gasto.id }) { item ->
                    Row(
                        Modifier.fillMaxWidth().background(Color(224,224,224)).padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(item.gasto.nombre, style = MaterialTheme.typography.bodyLarge)
                            if (item.gasto.descripcion.isNotBlank()) {
                                Text(
                                    item.gasto.descripcion,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(item.categoria.nombre, style = MaterialTheme.typography.labelSmall)
                        }
                        Text("$${"%.2f".format(item.gasto.monto)}")
                        TextButton(onClick = { onEliminar(item.gasto) }) { Text("X",fontWeight = FontWeight.Bold, color = Color.Red) }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

private val comida = Categoria(id = 1, nombre = "Comida")
private val transporte = Categoria(id = 2, nombre = "Transporte")

private val gastosDeEjemplo = listOf(
    GastoConCategoria(
        gasto = Gasto(
            id = 1, nombre = "Almuerzo", descripcion = "Menú del día",
            monto = 15000.0, categoriaId = 1, usuarioId = 1
        ),
        categoria = comida
    ),
    GastoConCategoria(
        gasto = Gasto(
            id = 2, nombre = "Bus", descripcion = "",
            monto = 3200.0, categoriaId = 2, usuarioId = 1
        ),
        categoria = transporte
    )
)

@Preview(showBackground = true, showSystemUi = true, name = "Con datos")
@Composable
private fun GastosConDatosPreview() {
    MaterialTheme {
        GastosContent(
            gastos = gastosDeEjemplo,
            categorias = listOf(comida, transporte),
            total = 18200.0,
            totales = listOf(
                TotalPorCategoria("Comida", 15000.0),
                TotalPorCategoria("Transporte", 3200.0)
            ),
            onAgregar = { _, _, _, _ -> },
            onEliminar = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Vacío")
@Composable
private fun GastosVacioPreview() {
    MaterialTheme {
        GastosContent(
            gastos = emptyList(),
            categorias = listOf(comida, transporte),
            total = 0.0,
            totales = emptyList(),
            onAgregar = { _, _, _, _ -> },
            onEliminar = {}
        )
    }
}