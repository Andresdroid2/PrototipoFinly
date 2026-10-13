package com.example.prototipofinly.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.data.models.GastosConCategoria
import com.example.prototipofinly.data.models.TotalPorCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Transaction
    @Query("SELECT * FROM gastos WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun observarConCategoria(usuarioId: Long): Flow<List<GastosConCategoria>>

    @Query("SELECT COALESCE(SUM(monto), 0) FROM gastos WHERE usuarioId = :usuarioId")
    fun observarTotal(usuarioId: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(monto), 0) FROM gastos")
    fun observarTotalGeneral(): Flow<Double>

    @Query(
        """
        SELECT c.nombre AS nombre, COALESCE(SUM(g.monto), 0) AS total
        FROM gastos g
        INNER JOIN categorias c ON g.categoriaId = c.id
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
