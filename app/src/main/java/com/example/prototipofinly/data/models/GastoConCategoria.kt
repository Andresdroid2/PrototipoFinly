package com.example.prototipofinly.data.models

import androidx.room.Embedded
import androidx.room.Relation

//Clase GastoConCategoria que contiene la relación entre Gasto y Categoria (De Gastos a Categoria)
data class GastoConCategoria(
    @Embedded val gasto: Gasto,
    @Relation(parentColumn = "categoriaId", entityColumn = "id")
    val categoria: Categoria
)
