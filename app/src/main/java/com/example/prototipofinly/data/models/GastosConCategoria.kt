package com.example.prototipofinly.data.models

import androidx.room.Embedded
import androidx.room.Relation

//Clase GastosConCategoria que contiene la relación entre Gasto y Categoria (De Gastos a Categoria)
data class GastosConCategoria(
    @Embedded val categoria: Categoria,
    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val gastos: List<Gasto>
){
    val total: Double
        get() = gastos.sumOf { it.monto.toDouble() }
}
