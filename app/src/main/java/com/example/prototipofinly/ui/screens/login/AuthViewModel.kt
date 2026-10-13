package com.example.prototipofinly.ui.screens.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prototipofinly.data.models.Usuario
import com.example.prototipofinly.data.repository.AuthRepository
import com.example.prototipofinly.data.repository.appDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val cargando: Boolean = false,
    val error: String? = null
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AuthRepository(appDatabase.get(app).usuarioDao())

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado.asStateFlow()

    fun registrar(nombre: String, email: String, password: String, confirmacion: String) {
        val error = when {
            nombre.isBlank() -> "Escribe tu nombre"
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Correo no válido"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            password != confirmacion -> "Las contraseñas no coinciden"
            else -> null
        }
        if (error != null) {
            _estado.value = AuthUiState(error = error)
            return
        }
        ejecutar { repo.registrar(nombre, email, password) }
    }

    fun iniciarSesion(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _estado.value = AuthUiState(error = "Completa correo y contraseña")
            return
        }
        ejecutar { repo.iniciarSesion(email, password) }
    }

    fun cerrarSesion() {
        _usuario.value = null
        _estado.value = AuthUiState()
    }

    fun limpiarError() {
        _estado.value = _estado.value.copy(error = null)
    }

    private fun ejecutar(accion: suspend () -> Result<Usuario>) {
        if (_estado.value.cargando) return
        _estado.value = AuthUiState(cargando = true)
        viewModelScope.launch {
            accion()
                .onSuccess { usuario ->
                    _usuario.value = usuario
                    _estado.value = AuthUiState()
                }
                .onFailure { e ->
                    _estado.value = AuthUiState(error = e.message ?: "Ocurrió un error")
                }
        }
    }
}