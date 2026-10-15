package com.example.prototipofinly.ui.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MontoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var texto by remember { mutableStateOf(WidgetPrefs.monto(this@MontoActivity)) }
                val foco = remember { FocusRequester() }

                LaunchedEffect(Unit) { foco.requestFocus() }

                AlertDialog(
                    onDismissRequest = { finish() },
                    title = { Text("Monto del gasto") },
                    text = {
                        OutlinedTextField(
                            value = texto,
                            onValueChange = { nuevo ->
                                if (nuevo.matches(Regex("^\\d{0,9}([.,]\\d{0,2})?$"))) texto = nuevo
                            },
                            placeholder = { Text("0") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.focusRequester(foco)
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { guardar(texto) }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { finish() }) { Text("Cancelar") }
                    }
                )
            }
        }
    }

    private fun guardar(texto: String) {
        WidgetPrefs.guardarMonto(this, texto.replace(',', '.'))
        lifecycleScope.launch {
            GastosWidget().updateAll(applicationContext)
            finish()
        }
    }
}