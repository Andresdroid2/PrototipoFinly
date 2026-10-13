package com.example.prototipofinly.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
//Clase categoria
data class Categoria (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String
)