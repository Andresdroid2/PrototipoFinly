package com.example.prototipofinly.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["nombre"], unique = true)])
//Clase Usuario
data class Usuario(@PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val edad: String,
    val gmail: String,
    val contrasenia: String)
