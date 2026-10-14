package com.example.prototipofinly.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prototipofinly.data.models.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(usuario: Usuario): Long

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun eliminarPorId(id: Long)
}