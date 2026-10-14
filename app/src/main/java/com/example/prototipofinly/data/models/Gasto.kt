package com.example.prototipofinly.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Gastos",
    //Configuración de la ForeinKeys
    foreignKeys = [
        //ForeinKey con Categoria
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.RESTRICT
        ),
        //ForeinKey con Usuario
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoriaId"), Index("usuarioId")]
)
//Clase Gasto
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val monto: Double,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis(),
    //ForeinKey con Categoria
    val categoriaId: Long,
    //ForeinKey con Usuario
    val usuarioId: Long
)
