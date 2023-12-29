package com.example.trivialnavidad.core.conexion.onffline

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida

class Conexion(context: Context) {
    private val dbHelper: BaseDatos = BaseDatos(context)
    companion object {
        private const val TABLE_JUGADORES = "jugador"
        private const val KEY_ID_J = "id"
        private const val KEY_NOMBRE_J = "nombre"
        private const val KEY_AVATAR_J = "avatar"

        private const val TABLE_PARTIDA = "partida"
        private const val KEY_ID_P = "id"
        private const val KEY_NOMBRE_P = "nombre"

        private const val TABLE_JUGADOR_EN_PARTIDA = "jugador_en_partida"
        private const val KEY_CASILLA_ACTUAL = "casilla_actual"
        private const val KEY_JUGADOR_ACTUAL = "jugador_actual"
    }
    fun agregarJugador(jugador: Jugador) {
        val db = dbHelper.writableDatabase

        // Comprobar si el nombre del jugador ya existe en la base de datos
        if (!existeJugador(jugador.nombre)) {
            val values = ContentValues().apply {
                put(KEY_NOMBRE_J, jugador.nombre)
                put(KEY_AVATAR_J, jugador.avatar)
            }

            val jugadorId = db.insert(TABLE_JUGADORES, null, values)
            jugador.id = jugadorId.toInt()
        }

        db.close()
    }

    private fun existeJugador(nombreJugador: String): Boolean {
        val db = dbHelper.readableDatabase

        val query = "SELECT $KEY_NOMBRE_J FROM $TABLE_JUGADORES WHERE $KEY_NOMBRE_J = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(nombreJugador))

        val existe = cursor.count > 0

        cursor.close()
        db.close()

        return existe
    }

    fun agregarPartida(partida: Partida) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE_P, partida.nombre)
        }
        val partidaId = db.insert(TABLE_PARTIDA, null, values)
        partida.id = partidaId.toInt()
        db.close()
    }

    fun agregarJugadorEnPartida(jugadorEnPartida: JugadorEnPartida) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(KEY_ID_J, jugadorEnPartida.jugador.id)
            put(KEY_ID_P, jugadorEnPartida.partida)
            put(KEY_CASILLA_ACTUAL, jugadorEnPartida.casillaActual)
        }
        db.insert(TABLE_JUGADOR_EN_PARTIDA, null, values)
        db.close()
    }
    fun obtenerPartidas(): List<Partida> {
        val db = dbHelper.readableDatabase
        val listaPartidas = mutableListOf<Partida>()

        val query = "SELECT * FROM $TABLE_PARTIDA"

        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(KEY_ID_P)
            val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE_P)

            if (idIndex != -1 && nombreIndex != -1) {
                val id = cursor.getInt(idIndex)
                val nombre = cursor.getString(nombreIndex)
                val partida = Partida(id, nombre)
                listaPartidas.add(partida)
            }
        }

        cursor.close()
        db.close()

        return listaPartidas
    }


    fun obtenerJugadoresEnPartida(partidaId: Int): List<JugadorEnPartida> {
        val db = dbHelper.readableDatabase
        val listaJugadoresEnPartida = mutableListOf<JugadorEnPartida>()

        val query = "SELECT * FROM $TABLE_JUGADOR_EN_PARTIDA " +
                "WHERE $KEY_ID_P = ? "

        val cursor: Cursor = db.rawQuery(query, arrayOf(partidaId.toString()))

        while (cursor.moveToNext()) {
            val idJugadorIndex = cursor.getColumnIndex(KEY_ID_J)
            val idPartidaIndex = cursor.getColumnIndex(KEY_ID_P)
            val casillaActualIndex = cursor.getColumnIndex(KEY_CASILLA_ACTUAL)
            val jugadorActualIndex = cursor.getColumnIndex(KEY_JUGADOR_ACTUAL)

            if (idJugadorIndex != -1 && idPartidaIndex != -1 && casillaActualIndex != -1 && jugadorActualIndex != -1) {
                val idJugador = cursor.getInt(idJugadorIndex)
                val idPartida = cursor.getInt(idPartidaIndex)
                val casillaActual = cursor.getInt(casillaActualIndex)
                val jugadorActual = cursor.getInt(jugadorActualIndex) == 1
                val jugador = obtenerJugador(idJugador)
                val jugadorEnPartida = JugadorEnPartida(jugador, idPartida, casillaActual, jugadorActual)
                listaJugadoresEnPartida.add(jugadorEnPartida)
            }
        }

        cursor.close()
        db.close()

        return listaJugadoresEnPartida
    }

    private fun obtenerJugador(idJugador: Int): Jugador {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM $TABLE_JUGADORES WHERE $KEY_ID_J = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(idJugador.toString()))
        var jugador = Jugador(0, "", "")
        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(KEY_ID_J)
            val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE_J)
            val avatarIndex = cursor.getColumnIndex(KEY_AVATAR_J)

            if (idIndex != -1 && nombreIndex != -1 && avatarIndex != -1) {
                val id = cursor.getInt(idIndex)
                val nombre = cursor.getString(nombreIndex)
                val avatar = cursor.getString(avatarIndex)
                jugador = Jugador(id, nombre, avatar)
            }
        }

        cursor.close()
        db.close()

        return jugador

    }

    fun actualizarCasillaActual(jugadorEnPartida: JugadorEnPartida) {
        val db = dbHelper.writableDatabase
        val jugadorId = jugadorEnPartida.jugador.id
        val partidaId = jugadorEnPartida.partida
        val nuevaCasilla = jugadorEnPartida.casillaActual
        val jugadorActual = if (jugadorEnPartida.jugadorActual) 1 else 0
        val values = ContentValues().apply {
            put(KEY_CASILLA_ACTUAL, nuevaCasilla)
            put(KEY_JUGADOR_ACTUAL, jugadorActual)
        }
        db.update(
            TABLE_JUGADOR_EN_PARTIDA,
            values,
            "$KEY_ID_J = ? AND $KEY_ID_P = ?",
            arrayOf(jugadorId.toString(), partidaId.toString())
        )

        db.close()
    }
}