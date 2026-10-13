package com.example.prototipofinly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gastos",
    //Configuración de la ForeinKey con Categoria se hace por medio de una tabla intermedia por GastosConCategoria
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
//Clase Gasto
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val monto: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis(),
    //ForeinKey con Categoria
    val categoriaId:Long
)
