package com.example.prototipofinly.ui.screens.gastos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.models.CategoriaConGastos
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.data.models.GastoConCategoria
import com.example.prototipofinly.data.models.TotalPorCategoria
import com.example.prototipofinly.data.repository.GastoRepository
import com.example.prototipofinly.data.repository.appDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import androidx.glance.appwidget.updateAll
import com.example.prototipofinly.ui.widget.GastosWidget
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class GastosViewModel(private val app: Application) : AndroidViewModel(app) {
    private val db = appDatabase.get(app)
    private val repo = GastoRepository(db.gastoDao(), db.categoriaDao())

    private val inicio = SharingStarted.WhileSubscribed(5000)
    private val usuarioId = MutableStateFlow<Long?>(null)

    val categorias: StateFlow<List<Categoria>> =
        repo.categorias.stateIn(viewModelScope, inicio, emptyList())

    val gastos: StateFlow<List<GastoConCategoria>> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList<GastoConCategoria>()) else repo.gastos(id)
        }
        .stateIn(viewModelScope, inicio, emptyList())

    val total: StateFlow<Double> = usuarioId
        .flatMapLatest { id -> if (id == null) flowOf(0.0) else repo.total(id) }
        .stateIn(viewModelScope, inicio, 0.0)

    val totalesPorCategoria: StateFlow<List<TotalPorCategoria>> = usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList<TotalPorCategoria>()) else repo.totalesPorCategoria(id)
        }
        .stateIn(viewModelScope, inicio, emptyList())

    val historial: StateFlow<List<CategoriaConGastos>> =
        combine(categorias, gastos) { cats, gs ->
            cats.map { c ->
                CategoriaConGastos(
                    categoria = c,
                    gastos = gs.filter { it.gasto.categoriaId == c.id }.map { it.gasto }
                )
            }
        }.stateIn(viewModelScope, inicio, emptyList())

    // La sesión la maneja AuthViewModel; MainActivity le pasa el id aquí
    fun seleccionarUsuario(id: Long) {
        usuarioId.value = id
    }

    fun cerrarSesion() {
        usuarioId.value = null
    }

    fun agregar(nombre: String, descripcion: String, monto: Double, categoriaId: Long) {
        val uid = usuarioId.value ?: return
        if (nombre.isBlank() || monto <= 0) return
        viewModelScope.launch {
            repo.agregar(
                Gasto(
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    monto = monto,
                    categoriaId = categoriaId,
                    usuarioId = uid
                )
            )
            GastosWidget().updateAll(app)
        }
    }

    fun eliminar(gasto: Gasto) {
        viewModelScope.launch {
            repo.eliminar(gasto)
            GastosWidget().updateAll(app)
        }
    }

    fun agregarCategoria(nombre: String) {
        if (nombre.isBlank()) return
        viewModelScope.launch { repo.agregarCategoria(nombre.trim()) }
    }
}