package com.example.trivialnavidad.core.conexion.onffline

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida

class BaseDatos(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Bloque companion object para definir constantes que serán usadas en toda la clase.
    // Son como los valores estáticos en Java
    companion object {
        private const val DATABASE_NAME = "trivial"
        private const val DATABASE_VERSION = 1
        private const val TABLE_JUGADORES = "jugador"
        private const val KEY_ID_J = "id_jugador"
        private const val KEY_NOMBRE_J = "nombre"
        private const val KEY_AVATAR_J = "avatar"

        private const val TABLE_PARTIDA = "partida"
        private const val KEY_ID_P = "id_partida"
        private const val KEY_NOMBRE_P = "nombre"

        private const val TABLE_JUGADOR_EN_PARTIDA = "jugador_en_partida"
        private const val KEY_CASILLA_ACTUAL = "casilla_actual"
        private const val KEY_JUGADOR_ACTUAL = "jugador_actual"
        private const val KEY_juego1 = "juego1"
        private const val KEY_juego2 = "juego2"
        private const val KEY_juego3 = "juego3"
        private const val KEY_juego4 = "juego4"
        private const val KEY_AVATAR_JP = "avatar"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createJugadorTable = ("CREATE TABLE " + TABLE_JUGADORES + " ( "
                + KEY_ID_J + " INTEGER PRIMARY KEY," + KEY_NOMBRE_J + " TEXT,"
                + KEY_AVATAR_J + " TEXT " + ")")

        val createPartidaTable = ("CREATE TABLE " + TABLE_PARTIDA + " ( "
                + KEY_ID_P + " INTEGER PRIMARY KEY, " + KEY_NOMBRE_P + " TEXT "
                + " ) ")

        val createJugadorEnPartidaTable = ("CREATE TABLE " + TABLE_JUGADOR_EN_PARTIDA + " ( "
                + KEY_ID_J + " INTEGER, " + KEY_ID_P + " INTEGER, "
                + KEY_CASILLA_ACTUAL + " TEXT, "
                + KEY_JUGADOR_ACTUAL + " INTEGER, "
                + KEY_AVATAR_JP + " TEXT, "
                + KEY_juego1 + " INTEGER, "
                + KEY_juego2 + " INTEGER, "
                + KEY_juego3 + " INTEGER, "
                + KEY_juego4 + " INTEGER, "
                + " PRIMARY KEY (" + KEY_ID_J + ", " + KEY_ID_P + " ), "
                + " FOREIGN KEY (" + KEY_ID_J + " ) REFERENCES " + TABLE_JUGADORES + " ( " + KEY_ID_J + " ),"
                + " FOREIGN KEY (" + KEY_ID_P + " ) REFERENCES " + TABLE_PARTIDA + " ( " + KEY_ID_P + " ) "
                + ")")

        db.execSQL(createJugadorTable)
        db.execSQL(createPartidaTable)
        db.execSQL(createJugadorEnPartidaTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente y crea una nueva.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JUGADORES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARTIDA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JUGADOR_EN_PARTIDA")
        onCreate(db)
    }

    // Método para obtener todos los discos de la base de datos.

}