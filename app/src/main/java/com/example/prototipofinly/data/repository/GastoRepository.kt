package com.example.prototipofinly.data.repository

import com.example.prototipofinly.data.dao.CategoriaDao
import com.example.prototipofinly.data.dao.GastoDao
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.models.Gasto

class GastoRepository(private val gastoDao: GastoDao, private val categoriaDao: CategoriaDao) {

    val categorias = categoriaDao.observarTodas()

    fun gastos(usuarioId: Long) = gastoDao.observarConCategoria(usuarioId)
    fun total(usuarioId: Long) = gastoDao.observarTotal(usuarioId)
    fun totalesPorCategoria(usuarioId: Long) = gastoDao.observarTotalesPorCategoria(usuarioId)

    suspend fun agregar(gasto: Gasto) = gastoDao.agregar(gasto)
    suspend fun eliminar(gasto: Gasto) = gastoDao.eliminar(gasto)
    suspend fun agregarCategoria(nombre: String) = categoriaDao.insertar(Categoria(nombre = nombre))
}