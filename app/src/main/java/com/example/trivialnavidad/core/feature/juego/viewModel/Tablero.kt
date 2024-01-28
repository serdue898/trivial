package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.provider.Settings
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.minijuegos.adivina.view.Adivina
import com.example.trivialnavidad.core.feature.minijuegos.parejas.view.Parejas
import com.example.trivialnavidad.core.feature.minijuegos.repaso.view.Repaso
import com.example.trivialnavidad.core.feature.minijuegos.test.view.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Clase Tablero: Gestiona la interfaz y las interacciones del tablero de juego del Trivial.
 *
 * @property gridTablero Referencia al GridLayout que representa el tablero de juego.
 * @property contexto Contexto de la aplicación.
 * @property jugadores Lista de jugadores en la partida.
 * @property tipo Tipo de partida ("offline" u "online").
 * @property jugadorActual Jugador que realiza la acción en el tablero.
 */
class Tablero(
    private var gridTablero: GridLayout,
    var contexto: Context,
    private var jugadores: List<JugadorEnPartida>,
    private val tipo: String
) {
    var jugadorActual: JugadorEnPartida? = null
    private var posiblesMovimientos = mutableListOf<Casilla>()
    val juego = MainActivity.juego as Juego
    val preguntas = Preguntas(contexto)
    private val avatarImages = contexto.resources.obtainTypedArray(R.array.avatar_images)
    private val colores = contexto.resources.obtainTypedArray(R.array.colores)
    private val tableroVersionUno = arrayOf(
        arrayOf("4", "1", "2", "3", "4", "1", "2", "3", "4"),
        arrayOf("3", "0", "0", "0", "3", "0", "0", "0", "1"),
        arrayOf("2", "0", "0", "0", "2", "0", "0", "0", "2"),
        arrayOf("1", "0", "0", "0", "1", "0", "0", "0", "3"),
        arrayOf("4", "3", "2", "1", "5", "1", "2", "3", "4"),
        arrayOf("3", "0", "0", "0", "1", "0", "0", "0", "1"),
        arrayOf("2", "0", "0", "0", "2", "0", "0", "0", "2"),
        arrayOf("1", "0", "0", "0", "3", "0", "0", "0", "3"),
        arrayOf("4", "3", "2", "1", "4", "3", "2", "1", "4"),
    )

    /**
     * Método asignarJugadores: Asigna a cada jugador en la partida a la casilla correspondiente en el tablero.
     */
    fun asignarJugadores() {
        for (jugador in jugadores) {
            val fila = jugador.casillaActual.split("_")[0].toInt()
            val columna = jugador.casillaActual.split("_")[1].toInt()
            val casilla = obtenerCasilla(fila, columna)
            casilla?.addJugador(jugador)
            rellenarCasilla(casilla!!)
        }
    }

    /**
     * Método actualizartablero: Actualiza la interfaz del tablero al cambiar la posición de los jugadores.
     *
     * @param jugadores Lista actualizada de jugadores en la partida.
     */
    fun actualizartablero(jugadores: List<JugadorEnPartida>) {
        gridTablero.removeAllViews()
        crearTablero()
        for (jugador in jugadores) {
            val fila = jugador.casillaActual.split("_")[0].toInt()
            val columna = jugador.casillaActual.split("_")[1].toInt()
            val casilla = obtenerCasilla(fila, columna)
            casilla?.addJugador(jugador)
            rellenarCasilla(casilla!!)
        }
    }

    /**
     * Método crearTablero: Inicializa el tablero de juego, asigna dificultades y eventos a las casillas.
     */
    fun crearTablero() {
        preguntas.cargarPreguntas()
        gridTablero.rowCount = 9
        gridTablero.columnCount = 9

        for (i in 0 until gridTablero.rowCount) {
            for (j in 0 until gridTablero.columnCount) {
                val casilla = Casilla(contexto, i, j)
                casilla.rowCount = 3
                casilla.columnCount = 3
                val dificultad = tableroVersionUno[i][j].toInt()
                casilla.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
                casilla.dificultad = dificultad
                casilla.color = colores.getColor(dificultad, 0)
                casilla.setBackgroundColor(casilla.color)
                casilla.setOnClickListener {
                    if (tipo == "offline") {
                        moverVista(casilla)
                    } else {
                        jugadorActual?.casillaActual = casilla.fila.toString() + "_" + casilla.columna.toString()
                        val socket = MainActivity.socket
                        socket?.emit("moverJugador", jugadorActual?.toJson())
                        reiniciarMovimientos()
                    }
                    asignarMinijuegos(casilla)
                }
                casilla.isEnabled = false
                gridTablero.addView(casilla)
            }
        }
    }

    /**
     * Método asignarMinijuegos: Asigna un minijuego a la casilla según la dificultad.
     *
     * @param casilla Casilla en la que se encuentra el jugador.
     */
    private fun asignarMinijuegos(casilla: Casilla) {
        val preguntasMinijuego = preguntas.preguntasDificultad(casilla.dificultad)

        if (preguntasMinijuego == null) {
            val alert = AlertDialog.Builder(contexto)
            alert.setTitle(contexto.getString(R.string.juego))
            alert.setCancelable(false)
            alert.setMessage(contexto.getString(R.string.internet_mensaje))
            alert.setPositiveButton(contexto.getString(R.string.aceptar)) { dialog, _ ->
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                contexto.startActivity(intent)
                dialog.dismiss()
            }
            alert.show()
        } else {
            val pregunta = preguntasMinijuego.random()
            var minijuego: Fragment? = null
            when (casilla.dificultad) {
                1 -> {
                    val listaPtreguntas: MutableList<Pregunta> = mutableListOf()
                    for (k in 0 until 5) {
                        var repetida = true
                        var preguntaNueva: Pregunta? = null
                        while (repetida) {
                            preguntaNueva = preguntasMinijuego.random()
                            if (!listaPtreguntas.contains(preguntaNueva)) {
                                repetida = false
                            }
                        }
                        listaPtreguntas.add(preguntaNueva!!)
                    }
                    minijuego = Test(listaPtreguntas, jugadorActual!!, false, tipo)
                }
                2 -> {
                    minijuego = Adivina(pregunta, jugadorActual!!)
                    // Falta terminar
                }
                3 -> {
                    minijuego = Repaso(pregunta, jugadorActual!!)
                }
                4 -> {
                    minijuego = Parejas(pregunta, jugadorActual!!)
                }
                5 -> {
                    var entrar = true
                    jugadorActual?.juegos?.forEach {
                        if (!it) {
                            entrar = false
                        }
                    }
                    if (entrar) {
                        val listaPtreguntas: MutableList<Pregunta> = mutableListOf()
                        listaPtreguntas.add(pregunta)
                        minijuego = Test(listaPtreguntas, jugadorActual!!, true, tipo)
                    } else {
                        val alert = AlertDialog.Builder(contexto)
                        alert.setTitle(contexto.getString(R.string.juego_final))
                        alert.setMessage(contexto.getString(R.string.mensaje_no_minijuego))
                        alert.setPositiveButton(contexto.getString(R.string.aceptar)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        alert.show()
                        juego.resultadoMiniJuego(true)
                    }
                }
                else -> {
                    juego.resultadoMiniJuego(true)
                }
            }
            if (minijuego != null) {
                if (contexto is AppCompatActivity) {
                    val fragmentManager =
                        (contexto as AppCompatActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.contenedor, minijuego)
                        .commit()
                }
            }
        }
    }

    /**
     * Método moverOnline: Actualiza la posición de un jugador en el tablero durante una partida en línea.
     *
     * @param jugador Jugador que se está moviendo.
     * @param jugadorAntiguo Posición antigua del jugador.
     */
    fun moverOnline(jugador: JugadorEnPartida, jugadorAntiguo: String) {
        val fila = jugador.casillaActual.split("_")[0].toInt()
        val columna = jugador.casillaActual.split("_")[1].toInt()
        val casilla = obtenerCasilla(fila, columna)
        val casillaAntigua = obtenerCasilla(jugadorAntiguo.split("_")[0].toInt(), jugadorAntiguo.split("_")[1].toInt())
        if (jugador.casillaActual == jugadorAntiguo) {
            return
        }

        val continuar = casillaAntigua?.removeJugador(jugador.id_jugador)
        if (continuar!!) {
            rellenarCasilla(casillaAntigua)
        }

        casilla?.addJugador(jugador)
        rellenarCasilla(casilla!!)
        jugadorActual = jugador
        reiniciarMovimientos()
    }

    /**
     * Método moverJugador: Inicia el movimiento de un jugador en el tablero durante una partida offline.
     *
     * @param jugador Jugador que se está moviendo.
     * @param movimientos Número de casillas que puede avanzar el jugador.
     */
    fun moverJugador(jugador: JugadorEnPartida, movimientos: Int) {
        val x = jugador.casillaActual.split("_")[0].toInt()
        val y = jugador.casillaActual.split("_")[1].toInt()
        jugadorActual = jugador
        posiblesMovimientos(x, y, movimientos, "", jugador)
    }



    /**
     * Método para calcular y mostrar los posibles movimientos de un jugador en el tablero.
     *
     * @param fila Fila actual del jugador.
     * @param columna Columna actual del jugador.
     * @param movimientos Número de movimientos disponibles.
     * @param direction Dirección del último movimiento.
     * @param jugador Jugador actual.
     */
    private fun posiblesMovimientos(fila: Int, columna: Int, movimientos: Int, direction: String, jugador: JugadorEnPartida) {
        if (movimientos == 0) {
            (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
                withContext(Dispatchers.Main) {
                    val casilla = obtenerCasilla(fila, columna)
                    posiblesMovimientos.add(casilla!!)
                    val colorFondo = (casilla.background as? ColorDrawable)?.color
                    val borderDrawable = GradientDrawable()
                    borderDrawable.setColor(colorFondo!!)
                    borderDrawable.setStroke(5, Color.BLACK)
                    val layers = arrayOf(ColorDrawable(colorFondo), borderDrawable)
                    val layerDrawable = LayerDrawable(layers)
                    casilla.background = layerDrawable
                    casilla.isEnabled = true
                }
            }
            return
        }

        if (direction != "izquierda") moverEnDireccion(fila, columna + 1, movimientos - 1, "derecha", jugador)
        if (direction != "derecha") moverEnDireccion(fila, columna - 1, movimientos - 1, "izquierda", jugador)
        if (direction != "abajo") moverEnDireccion(fila - 1, columna, movimientos - 1, "arriba", jugador)
        if (direction != "arriba") moverEnDireccion(fila + 1, columna, movimientos - 1, "abajo", jugador)
    }

    /**
     * Método auxiliar para realizar un movimiento en una dirección específica.
     *
     * @param fila Fila actual del jugador.
     * @param columna Columna actual del jugador.
     * @param movimientos Número de movimientos disponibles.
     * @param direction Dirección del último movimiento.
     * @param jugador Jugador actual.
     */
    private fun moverEnDireccion(fila: Int, columna: Int, movimientos: Int, direction: String, jugador: JugadorEnPartida) {
        if (fila in 0 until gridTablero.columnCount && columna in 0 until gridTablero.rowCount) {
            if (!comprobarCasilla(fila, columna)) {
                posiblesMovimientos(fila, columna, movimientos, direction, jugador)
            }
        }
    }

    /**
     * Método auxiliar para obtener la casilla en una posición específica del tablero.
     *
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return La casilla correspondiente a la posición dada.
     */
    private fun obtenerCasilla(fila: Int, columna: Int): Casilla? {
        return gridTablero.getChildAt(fila * gridTablero.columnCount + columna) as? Casilla
    }

    /**
     * Método auxiliar para comprobar si una casilla es válida (no es de color negro).
     *
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return `true` si la casilla es válida, `false` en caso contrario.
     */
    private fun comprobarCasilla(fila: Int, columna: Int): Boolean {
        val casilla = gridTablero.getChildAt(fila * gridTablero.columnCount + columna) as Casilla
        val colorDeFondo = (casilla.background as? ColorDrawable)?.color
        return colorDeFondo == ContextCompat.getColor(contexto, R.color.negro)
    }

    /**
     * Método auxiliar para realizar el movimiento en la interfaz gráfica al seleccionar una casilla.
     *
     * @param casillaNueva Casilla de destino del movimiento.
     */
    private fun moverVista(casillaNueva: Casilla) {
        val fila = jugadorActual?.casillaActual?.split("_")?.get(0)?.toInt()
        val columna = jugadorActual?.casillaActual?.split("_")?.get(1)?.toInt()
        val casilla = obtenerCasilla(fila!!, columna!!)

        casillaNueva.addJugador(jugadorActual!!)
        rellenarCasilla(casillaNueva)
        casilla?.removeJugador(jugadorActual!!)
        rellenarCasilla(casilla!!)

        jugadorActual!!.casillaActual = "${casillaNueva.fila}_${casillaNueva.columna}"
        reiniciarMovimientos()
    }

    /**
     * Método auxiliar para reiniciar los movimientos posibles y sus colores en el tablero.
     */
    private fun reiniciarMovimientos() {
        (contexto as? AppCompatActivity)?.lifecycleScope?.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                for (casilla in posiblesMovimientos) {
                    casilla.setBackgroundColor(casilla.color)
                    casilla.isEnabled = false
                }
                posiblesMovimientos.clear()
            }
        }
    }

    /**
     * Método auxiliar para rellenar visualmente una casilla con los avatares de los jugadores presentes en ella.
     *
     * @param casilla Casilla que se va a rellenar.
     */
    private fun rellenarCasilla(casilla: Casilla) {
        casilla.removeAllViews()
        for (jugador in casilla.jugadores) {
            val nuevoJugador = ImageView(contexto)
            nuevoJugador.setImageDrawable(avatarImages.getDrawable(jugador.avatar.toInt()))
            nuevoJugador.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            casilla.addView(nuevoJugador)
        }
    }




}