package com.example.prototipofinly.data

import androidx.room.Embedded
import androidx.room.Relation

//Clase GastosConCategoria que contiene la relación entre Gasto y Categoria (De Gastos a Categoria)
data class GastosConCategoria(
    @Embedded val gasto: Gasto,
    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val categoria: Categoria
)
