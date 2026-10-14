package com.example.prototipofinly.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioConGastos(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "id",
        entityColumn = "usuarioId"
    )
    val gastos: List<Gasto>
)
