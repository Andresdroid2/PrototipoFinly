package com.example.prototipofinly.data.repository

import com.example.prototipofinly.data.dao.UsuarioDao
import com.example.prototipofinly.data.models.Usuario
import com.example.prototipofinly.util.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val dao: UsuarioDao) {

    suspend fun registrar(nombre: String, email: String, password: String): Result<Usuario> =
        withContext(Dispatchers.Default) {
            val emailNormalizado = email.trim().lowercase()

            if (dao.buscarPorEmail(emailNormalizado) != null) {
                return@withContext Result.failure(
                    IllegalArgumentException("Ya existe una cuenta con ese correo")
                )
            }

            val sal = PasswordHasher.nuevaSal()
            val usuario = Usuario(
                nombre = nombre.trim(),
                email = emailNormalizado,
                passwordHash = PasswordHasher.hash(password, sal),
                salt = sal
            )
            val id = dao.insertar(usuario)

            if (id == -1L) {
                Result.failure(IllegalArgumentException("Ya existe una cuenta con ese correo"))
            } else {
                Result.success(usuario.copy(id = id))
            }
        }

    suspend fun iniciarSesion(email: String, password: String): Result<Usuario> =
        withContext(Dispatchers.Default) {
            val usuario = dao.buscarPorEmail(email.trim().lowercase())

            if (usuario == null || !PasswordHasher.verificar(
                    password,
                    usuario.salt,
                    usuario.passwordHash
                )
            ) {
                Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
            } else {
                Result.success(usuario)
            }
        }
}