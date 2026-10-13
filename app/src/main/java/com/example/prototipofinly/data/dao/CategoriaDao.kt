package com.example.prototipofinly.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.models.GastosConCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias ORDER BY nombre")
    fun observarTodas(): Flow<List<Categoria>>

    @Transaction
    @Query("SELECT * FROM categorias ORDER BY nombre")
    fun observarConGastos(): Flow<List<GastosConCategoria>>

    @Insert
    suspend fun insertar(categoria: Categoria)
}