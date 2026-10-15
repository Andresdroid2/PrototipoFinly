package com.example.prototipofinly.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prototipofinly.R
import com.example.prototipofinly.ui.theme.titanone

@Composable
fun AuthScreen(vm: AuthViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    AuthContent(
        estado = estado,
        onRegistrar = vm::registrar,
        onIniciarSesion = vm::iniciarSesion,
        onLimpiarError = vm::limpiarError
    )
}

@Composable
fun AuthContent(
    estado: AuthUiState,
    onRegistrar: (nombre: String, email: String, password: String, confirmacion: String) -> Unit,
    onIniciarSesion: (email: String, password: String) -> Unit,
    onLimpiarError: () -> Unit,
    modoRegistroInicial: Boolean = false
) {
    var modoRegistro by remember { mutableStateOf(modoRegistroInicial) }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.icon),
            modifier = Modifier.size(200.dp),
            contentDescription = "Logo de la app"
        )

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .clip(ShapeDefaults.ExtraLarge.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)))
                .background(color = Color(252, 203, 84))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                if (modoRegistro) "Crear cuenta" else "Iniciar sesión",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = titanone
            )

            Spacer(Modifier.height(24.dp))

            if (modoRegistro) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Nombre", color = Color.Gray,fontFamily = titanone, fontSize = 19.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF2F2F2),
                        unfocusedContainerColor = Color(0xFFF2F2F2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo",color = Color.Gray,fontFamily = titanone, fontSize = 19.sp) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña", color = Color.Gray,fontFamily = titanone, fontSize = 19.sp) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2)
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (modoRegistro) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmacion,
                    onValueChange = { confirmacion = it },
                    placeholder = { Text("Confirmar Contraseña",color = Color.Gray,fontFamily = titanone, fontSize = 19.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF2F2F2),
                        unfocusedContainerColor = Color(0xFFF2F2F2)
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            estado.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (modoRegistro) onRegistrar(nombre, email, password, confirmacion)
                    else onIniciarSesion(email, password)
                },
                enabled = !estado.cargando,
                colors = ButtonDefaults.buttonColors(Color(0xFF2F2D28)),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.cargando) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(text = if (modoRegistro) "Registrarme" else "Entrar",color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            TextButton(
                onClick = {
                    modoRegistro = !modoRegistro
                    password = ""
                    confirmacion = ""
                    onLimpiarError()
                }
            ) {
                Text(text = if (modoRegistro) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate",color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login")
@Composable
private fun AuthLoginPreview() {
    MaterialTheme {
        AuthContent(
            estado = AuthUiState(),
            onRegistrar = { _, _, _, _ -> },
            onIniciarSesion = { _, _ -> },
            onLimpiarError = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Registro")
@Composable
private fun AuthRegistroPreview() {
    MaterialTheme {
        AuthContent(
            estado = AuthUiState(),
            onRegistrar = { _, _, _, _ -> },
            onIniciarSesion = { _, _ -> },
            onLimpiarError = {},
            modoRegistroInicial = true
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Con error")
@Composable
private fun AuthErrorPreview() {
    MaterialTheme {
        AuthContent(
            estado = AuthUiState(error = "Correo o contraseña incorrectos"),
            onRegistrar = { _, _, _, _ -> },
            onIniciarSesion = { _, _ -> },
            onLimpiarError = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Cargando")
@Composable
private fun AuthCargandoPreview() {
    MaterialTheme {
        AuthContent(
            estado = AuthUiState(cargando = true),
            onRegistrar = { _, _, _, _ -> },
            onIniciarSesion = { _, _ -> },
            onLimpiarError = {}
        )
    }
}