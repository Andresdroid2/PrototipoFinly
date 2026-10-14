package com.example.prototipofinly.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.data.models.GastoConCategoria
import com.example.prototipofinly.data.models.TotalPorCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Transaction
    @Query("SELECT * FROM gastos WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun observarConCategoria(usuarioId: Long): Flow<List<GastoConCategoria>>

    @Query("SELECT COALESCE(SUM(monto), 0) FROM gastos WHERE usuarioId = :usuarioId")
    fun observarTotal(usuarioId: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(monto), 0) FROM gastos")
    fun observarTotalGeneral(): Flow<Double>

    @Query(
        """
        SELECT c.nombre AS nombre, SUM(g.monto) AS total
        FROM gastos g
        INNER JOIN categorias c ON c.id = g.categoriaId
        WHERE g.usuarioId = :usuarioId
        GROUP BY c.id
        ORDER BY total DESC
        """
    )
    fun observarTotalesPorCategoria(usuarioId: Long): Flow<List<TotalPorCategoria>>

    @Insert
    suspend fun agregar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)
}