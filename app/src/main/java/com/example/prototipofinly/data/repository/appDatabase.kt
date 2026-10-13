package com.example.prototipofinly.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.prototipofinly.data.dao.CategoriaDao
import com.example.prototipofinly.data.dao.GastoDao
import com.example.prototipofinly.data.dao.UsuarioDao
import com.example.prototipofinly.data.models.Categoria
import com.example.prototipofinly.data.models.Gasto
import com.example.prototipofinly.data.models.Usuario
import kotlin.jvm.java

@Database(entities = [Usuario::class, Categoria::class, Gasto::class], version = 1)
abstract class appDatabase: RoomDatabase() {
    abstract fun gastroDao(): GastoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object{
        @Volatile private var INSTANCE: appDatabase? = null

        fun get(context: Context): appDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    appDatabase::class.java,
                    "gastos.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            listOf("Comida", "Transporte", "Hogar", "Ocio", "Otros").forEach {
                                db.execSQL("INSERT INTO categorias (nombre) VALUES ('$it')")
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
    }
}