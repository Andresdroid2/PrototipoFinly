package com.example.prototipofinly.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    private const val ITERACIONES = 120_000
    private const val LONGITUD_BITS = 256

    fun nuevaSal(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun hash(password: String, salBase64: String): String {
        val sal = Base64.getDecoder().decode(salBase64)
        val spec = PBEKeySpec(password.toCharArray(), sal, ITERACIONES, LONGITUD_BITS)
        val bytes = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .encoded
        spec.clearPassword()
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun verificar(password: String, salBase64: String, hashEsperado: String): Boolean {
        val calculado = hash(password, salBase64)
        return MessageDigest.isEqual(calculado.toByteArray(), hashEsperado.toByteArray())
    }
}