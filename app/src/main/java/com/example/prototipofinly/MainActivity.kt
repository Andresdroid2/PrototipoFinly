package com.example.prototipofinly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prototipofinly.ui.screens.HistorialScreen
import com.example.prototipofinly.ui.screens.gastos.GastosScreen
import com.example.prototipofinly.ui.screens.gastos.GastosViewModel
import com.example.prototipofinly.ui.screens.login.AuthScreen
import com.example.prototipofinly.ui.screens.login.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val authVm: AuthViewModel = viewModel()
                val gastosVm: GastosViewModel = viewModel()
                val usuario by authVm.usuario.collectAsStateWithLifecycle()
                var pestana by remember { mutableIntStateOf(0) }
                val actual = usuario

                LaunchedEffect(actual?.id) {
                    if (actual == null) gastosVm.cerrarSesion()
                    else gastosVm.seleccionarUsuario(actual.id)
                }

                if (actual == null) {
                    AuthScreen(authVm)
                } else {
                    Column(Modifier.fillMaxSize().statusBarsPadding()) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(actual.nombre, style = MaterialTheme.typography.titleMedium)
                            TextButton(onClick = authVm::cerrarSesion) { Text("Cerrar sesión") }
                        }
                        TabRow(selectedTabIndex = pestana) {
                            Tab(
                                selected = pestana == 0,
                                onClick = { pestana = 0 },
                                text = { Text("Gastos") })
                            Tab(
                                selected = pestana == 1,
                                onClick = { pestana = 1 },
                                text = { Text("Historial") })
                        }
                        when (pestana) {
                            0 -> GastosScreen(gastosVm)
                            1 -> HistorialScreen(gastosVm)
                        }
                    }
                }
            }
        }
    }
}