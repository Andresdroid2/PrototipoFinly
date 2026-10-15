package com.example.prototipofinly.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.data.repository.appDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

val CategoriaKey = ActionParameters.Key<Long>("categoriaId")

class SeleccionarCategoriaAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val id = parameters[CategoriaKey] ?: return
        WidgetPrefs.guardarCategoria(context, id)
        GastosWidget().updateAll(context)
    }
}

class ConfirmarGastoAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val monto = WidgetPrefs.monto(context).toDoubleOrNull() ?: 0.0
        val categoriaId = WidgetPrefs.categoriaId(context)
        val usuarioId = WidgetPrefs.usuarioId(context)

        when {
            usuarioId == -1L -> return avisar(context, "Inicia sesión en la app")
            monto <= 0 -> return avisar(context, "Escribe el monto")
            categoriaId == -1L -> return avisar(context, "Elige una categoría")
        }

        val db = appDatabase.get(context)
        val categoria = db.categoriaDao().observarTodas().first()
            .firstOrNull { it.id == categoriaId }
            ?: return avisar(context, "Categoría no encontrada")

        db.gastoDao().agregar(
            Gasto(
                nombre = categoria.nombre,
                descripcion = "",
                monto = monto,
                categoriaId = categoriaId,
                usuarioId = usuarioId
            )
        )

        WidgetPrefs.limpiarPendiente(context)
        GastosWidget().updateAll(context)
        avisar(context, "Gasto guardado")
    }

    private suspend fun avisar(context: Context, mensaje: String) =
        withContext(Dispatchers.Main) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }
}