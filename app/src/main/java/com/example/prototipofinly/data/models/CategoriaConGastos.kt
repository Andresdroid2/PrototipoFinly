package com.example.prototipofinly.data.models

import androidx.room.Embedded
import androidx.room.Relation

//Clase CategoriaConGastos que contiene la relación entre Gasto y Categoria (De Gastos a Categoria)
data class CategoriaConGastos(
    @Embedded val categoria: Categoria,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoriaId"
    )
    val gastos: List<Gasto>
) {
    val total: Double
        get() = gastos.sumOf { it.monto }
}