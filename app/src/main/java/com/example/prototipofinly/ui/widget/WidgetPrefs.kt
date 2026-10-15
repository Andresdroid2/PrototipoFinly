package com.example.prototipofinly.ui.widget

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class EstadoWidget(
    val monto: String = "",
    val categoriaId: Long = -1L,
    val usuarioId: Long = -1L
)

object WidgetPrefs {
    private const val ARCHIVO = "widget_prefs"

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    fun leer(context: Context): EstadoWidget {
        val p = prefs(context)
        return EstadoWidget(
            monto = p.getString("monto", "") ?: "",
            categoriaId = p.getLong("categoriaId", -1L),
            usuarioId = p.getLong("usuarioId", -1L)
        )
    }

    fun observar(context: Context): Flow<EstadoWidget> = callbackFlow {
        val p = prefs(context)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(leer(context))
        }
        p.registerOnSharedPreferenceChangeListener(listener)
        trySend(leer(context))
        awaitClose { p.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun usuarioId(context: Context): Long = prefs(context).getLong("usuarioId", -1L)

    fun guardarUsuario(context: Context, id: Long?) {
        val editor = prefs(context).edit()
        if (id == null) editor.remove("usuarioId") else editor.putLong("usuarioId", id)
        editor.apply()
    }

    fun monto(context: Context): String = prefs(context).getString("monto", "") ?: ""

    fun guardarMonto(context: Context, monto: String) {
        prefs(context).edit().putString("monto", monto).apply()
    }

    fun categoriaId(context: Context): Long = prefs(context).getLong("categoriaId", -1L)

    fun guardarCategoria(context: Context, id: Long) {
        prefs(context).edit().putLong("categoriaId", id).apply()
    }

    fun limpiarPendiente(context: Context) {
        prefs(context).edit().remove("monto").remove("categoriaId").apply()
    }
}