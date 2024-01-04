package com.example.trivialnavidad.core.conexion.onffline

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.conexion.onffline.modelo.Partida

class Conexion(Context: Context) {
    private val dbHelper: BaseDatos = BaseDatos(Context)
    companion object {
        private const val DATABASE_NAME = "trivial"
        private const val DATABASE_VERSION = 2
        private const val TABLE_JUGADORES = "jugador"
        private const val KEY_ID_J = "id_jugador"
        private const val KEY_NOMBRE_J = "nombre"

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
        private const val KEY_AVATAR_JP = "avatar_partida"
    }
    fun agregarJugador(jugador: Jugador ) {
        val db = dbHelper.writableDatabase

        // Comprobar si el nombre del jugador ya existe en la base de datos
        if (!existeJugador(jugador.nombre)) {
            val values = ContentValues().apply {
                put(KEY_NOMBRE_J, jugador.nombre)
            }

            val jugadorId = db.insert(TABLE_JUGADORES, null, values)
            jugador.id = jugadorId.toInt()
        }

    }

    private fun existeJugador(nombreJugador: String): Boolean {
        val db = dbHelper.readableDatabase

        val query = "SELECT $KEY_NOMBRE_J FROM $TABLE_JUGADORES WHERE $KEY_NOMBRE_J = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(nombreJugador))

        val existe = cursor.count > 0

        cursor.close()

        return existe
    }

    fun agregarPartida(partida: Partida):Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE_P, partida.nombre)
        }
        val partidaId = db.insert(TABLE_PARTIDA, null, values)
        partida.idPartida = partidaId.toInt()
        partida.nombre = "Partida ${partidaId.toInt()}"
        return partidaId.toInt()
    }

    fun agregarJugadorEnPartida(jugadorEnPartida: JugadorEnPartida) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(KEY_ID_J, jugadorEnPartida.jugador.id)
            put(KEY_ID_P, jugadorEnPartida.partida)
            put(KEY_CASILLA_ACTUAL, jugadorEnPartida.casillaActual)
            put(KEY_JUGADOR_ACTUAL, if (jugadorEnPartida.jugadorActual) 1 else 0)
            put(KEY_AVATAR_JP, jugadorEnPartida.avatar)
        }
       var funciona= db.insert(TABLE_JUGADOR_EN_PARTIDA, null, values)
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

        return listaPartidas
    }


    fun obtenerJugadoresEnPartida(partidaId: Int): List<JugadorEnPartida> {
        val db = dbHelper.readableDatabase
        val listaJugadoresEnPartida = mutableListOf<JugadorEnPartida>()

        val query = "SELECT * FROM $TABLE_JUGADOR_EN_PARTIDA " +
                "WHERE $KEY_ID_P = $partidaId "

        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val idJugadorIndex = cursor.getColumnIndex(KEY_ID_J)
            val idPartidaIndex = cursor.getColumnIndex(KEY_ID_P)
            val casillaActualIndex = cursor.getColumnIndex(KEY_CASILLA_ACTUAL)
            val jugadorActualIndex = cursor.getColumnIndex(KEY_JUGADOR_ACTUAL)
            val jugadorAvatar = cursor.getColumnIndex(KEY_AVATAR_JP)
            val juego1Index = cursor.getColumnIndex(KEY_juego1)
            val juego2Index = cursor.getColumnIndex(KEY_juego2)
            val juego3Index = cursor.getColumnIndex(KEY_juego3)
            val juego4Index = cursor.getColumnIndex(KEY_juego4)

            if (idJugadorIndex != -1 && idPartidaIndex != -1 && casillaActualIndex != -1 && jugadorActualIndex != -1 && jugadorAvatar != -1 && juego1Index != -1 && juego2Index != -1 && juego3Index != -1 && juego4Index != -1) {
                val idJugador = cursor.getInt(idJugadorIndex)
                val idPartida = cursor.getInt(idPartidaIndex)
                val casillaActual = cursor.getString(casillaActualIndex)
                val jugadorActual = cursor.getInt(jugadorActualIndex) == 1
                val avatar = cursor.getString(jugadorAvatar)
                val juego1 = cursor.getInt(juego1Index)==1
                val juego2 = cursor.getInt(juego2Index)==1
                val juego3 = cursor.getInt(juego3Index)==1
                val juego4 = cursor.getInt(juego4Index)==1
                val juegos = mutableListOf( juego1, juego2, juego3, juego4)
                val jugador = obtenerJugador(idJugador)
                val jugadorEnPartida = JugadorEnPartida(jugador, idPartida, casillaActual, jugadorActual,juegos,avatar)
                listaJugadoresEnPartida.add(jugadorEnPartida)
            }
        }

        cursor.close()

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

            if (idIndex != -1 && nombreIndex != -1 ) {
                val id = cursor.getInt(idIndex)
                val nombre = cursor.getString(nombreIndex)
                jugador = Jugador(id, nombre, "")
            }
        }

        cursor.close()

        return jugador

    }

    fun actualizarCasillaActual(jugadorEnPartida: JugadorEnPartida) {
        val db = dbHelper.writableDatabase
        val jugadorId = jugadorEnPartida.jugador.id
        val partidaId = jugadorEnPartida.partida
        val nuevaCasilla = jugadorEnPartida.casillaActual
        val jugadorActual = if (jugadorEnPartida.jugadorActual) 1 else 0
        val jugadorAvatar = jugadorEnPartida.avatar
        val juego1 = if (jugadorEnPartida.juegos[0]) 1 else 0
        val juego2 = if (jugadorEnPartida.juegos[1]) 1 else 0
        val juego3 = if (jugadorEnPartida.juegos[2]) 1 else 0
        val juego4 = if (jugadorEnPartida.juegos[3]) 1 else 0
        val values = ContentValues().apply {
            put(KEY_CASILLA_ACTUAL, nuevaCasilla)
            put(KEY_JUGADOR_ACTUAL, jugadorActual)
            put(KEY_AVATAR_JP, jugadorAvatar)
            put(KEY_juego1, juego1)
            put(KEY_juego2, juego2)
            put(KEY_juego3, juego3)
            put(KEY_juego4, juego4)
        }
        db.update(
            TABLE_JUGADOR_EN_PARTIDA,
            values,
            "$KEY_ID_J = ? AND $KEY_ID_P = ?",
            arrayOf(jugadorId.toString(), partidaId.toString())
        )

    }
}