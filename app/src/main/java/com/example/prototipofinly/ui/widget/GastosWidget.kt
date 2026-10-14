package com.example.prototipofinly.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.prototipofinly.MainActivity
import com.example.prototipofinly.data.repository.appDatabase
import kotlinx.coroutines.flow.first

class GastosWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Suma de todos los gastos. Se lee una sola vez; el widget no observa el Flow
        val total = appDatabase.get(context).gastoDao().observarTotalGeneral().first()

        provideContent {
            GlanceTheme {
                Contenido(total)
            }
        }
    }

    @Composable
    private fun Contenido(total: Double) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .padding(16.dp)
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "Total gastado",
                style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 12.sp)
            )
            Text(
                text = "$${"%.2f".format(total)}",
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}