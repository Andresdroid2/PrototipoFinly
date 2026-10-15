package com.example.prototipofinly.ui.widget


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.prototipofinly.MainActivity
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.repository.appDatabase
import kotlinx.coroutines.flow.first



class GastosWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val categoriasDao = appDatabase.get(context).categoriaDao()
        val categoriasFlow = categoriasDao.observarTodas()
        val estadoFlow = WidgetPrefs.observar(context)

        val categoriasIniciales = categoriasFlow.first()
        val estadoInicial = WidgetPrefs.leer(context)

        provideContent {
            val categorias by categoriasFlow.collectAsState(categoriasIniciales)
            val estado by estadoFlow.collectAsState(estadoInicial)

            GlanceTheme {
                ContenidoWidget(
                    categorias = categorias,
                    montoTexto = estado.monto,
                    categoriaElegidaId = estado.categoriaId,
                    haySesion = estado.usuarioId != -1L
                )
            }
        }
    }
}

private val Amarillo = Color(0xFFFFD903)

private fun iconoDe(nombre: String): String = when (nombre.lowercase()) {
    "comida" -> "🍔"
    "transporte" -> "🚌"
    "hogar" -> "🏠"
    "ocio" -> "🎮"
    "otros" -> "➕"
    else -> "🏷️"
}

@Composable
fun ContenidoWidget(
    categorias: List<Categoria>,
    montoTexto: String,
    categoriaElegidaId: Long,
    haySesion: Boolean
) {
    if (!haySesion) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .padding(16.dp)
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Abre la app e inicia sesión para registrar gastos",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        return
    }

    val monto = montoTexto.ifBlank { "0" }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.widgetBackground)
            .padding(12.dp)
    ) {
        Text(
            text = "Registrar gasto",
            style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 12.sp)
        )

        Text(
            text = "\$$monto",
            modifier = GlanceModifier.clickable(actionStartActivity<MontoActivity>()),
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(GlanceModifier.height(8.dp))

        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Row(modifier = GlanceModifier.defaultWeight()) {
                categorias.take(5).forEach { categoria ->
                    val seleccionada = categoria.id == categoriaElegidaId
                    Column(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .padding(vertical = 4.dp)
                            .background(
                                if (seleccionada) GlanceTheme.colors.secondaryContainer
                                else GlanceTheme.colors.widgetBackground
                            )
                            .cornerRadius(12.dp)
                            .clickable(
                                actionRunCallback<SeleccionarCategoriaAction>(
                                    actionParametersOf(CategoriaKey to categoria.id)
                                )
                            ),
                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                    ) {
                        Text(
                            text = iconoDe(categoria.nombre),
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Text(
                            text = categoria.nombre,
                            maxLines = 1,
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(GlanceModifier.width(8.dp))

            Box(
                modifier = GlanceModifier
                    .size(56.dp)
                    .background(GlanceTheme.colors.background)
                    .cornerRadius(28.dp)
                    .clickable(actionRunCallback<ConfirmarGastoAction>()),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = TextStyle(
                        color = GlanceTheme.colors.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}


private val categoriasDeEjemplo = listOf(
    Categoria(id = 1, nombre = "Comida"),
    Categoria(id = 2, nombre = "Transporte"),
    Categoria(id = 3, nombre = "Hogar"),
    Categoria(id = 4, nombre = "Ocio"),
    Categoria(id = 5, nombre = "Otros")
)

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 320, heightDp = 140)
@Composable
private fun WidgetPreview() {
    GlanceTheme {
        ContenidoWidget(
            categorias = categoriasDeEjemplo,
            montoTexto = "15000",
            categoriaElegidaId = 1,
            haySesion = true
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 320, heightDp = 140)
@Composable
private fun WidgetSinSesionPreview() {
    GlanceTheme {
        ContenidoWidget(
            categorias = categoriasDeEjemplo,
            montoTexto = "",
            categoriaElegidaId = -1,
            haySesion = false
        )
    }
}