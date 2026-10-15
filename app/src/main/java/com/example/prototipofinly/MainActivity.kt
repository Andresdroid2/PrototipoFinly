package com.example.prototipofinly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prototipofinly.ui.screens.HistorialScreen
import com.example.prototipofinly.ui.screens.gastos.GastosScreen
import com.example.prototipofinly.ui.screens.gastos.GastosViewModel
import com.example.prototipofinly.ui.screens.login.AuthScreen
import com.example.prototipofinly.ui.screens.login.AuthViewModel
import com.example.prototipofinly.ui.widget.GastosWidget
import com.example.prototipofinly.ui.widget.WidgetPrefs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            MaterialTheme {
                val context = LocalContext.current
                val authVm: AuthViewModel = viewModel()
                val gastosVm: GastosViewModel = viewModel()
                val usuario by authVm.usuario.collectAsStateWithLifecycle()
                var pestana by remember { mutableIntStateOf(0) }
                val actual = usuario

                LaunchedEffect(actual?.id) {
                    if (actual == null) gastosVm.cerrarSesion()
                    else gastosVm.seleccionarUsuario(actual.id)

                    WidgetPrefs.guardarUsuario(context, actual?.id)
                    GastosWidget().updateAll(context)
                }

                if (actual == null) {
                    AuthScreen(authVm)
                } else {
                    Column(Modifier.fillMaxSize()) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF2F2D28))
                                .statusBarsPadding()
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = actual.nombre,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                TextButton(
                                    onClick = authVm::cerrarSesion,
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                                ) {
                                    Text("Cerrar sesión")
                                }
                            }

                            MaterialTheme(
                                colorScheme = MaterialTheme.colorScheme.copy(primary = Color.White)
                            ) {
                                SecondaryTabRow(
                                    selectedTabIndex = pestana,
                                    modifier = Modifier,
                                    containerColor = Color(0xFF2F2D28),
                                    contentColor = Color.White,
                                    indicator = {
                                        TabRowDefaults.SecondaryIndicator(
                                            modifier = Modifier.tabIndicatorOffset(pestana),
                                            color = Color.White
                                        )
                                    },
                                    divider = {}
                                ) {
                                        Tab(
                                            selected = pestana == 0,
                                            onClick = { pestana = 0 },
                                            selectedContentColor = Color.White,
                                            unselectedContentColor = Color.White.copy(alpha = 0.6f),
                                            text = { Text("Gastos") }
                                        )
                                        Tab(
                                            selected = pestana == 1,
                                            onClick = { pestana = 1 },
                                            selectedContentColor = Color.White,
                                            unselectedContentColor = Color.White.copy(alpha = 0.6f),
                                            text = { Text("Historial") }
                                        )
                                    }
                            }
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