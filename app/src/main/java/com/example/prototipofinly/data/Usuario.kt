package com.example.prototipofinly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
//Clase Usuario
data class Usuario(@PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val edad: String,
    val gmail: String,
    val contraseña: String)
