package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida

class Tablero (var gridTablero: GridLayout, var contexto: Context) {

    private val tableroVersionUno = arrayOf(
        arrayOf("4","1","2","3","4","1","2","3","4"),
        arrayOf("3","0","0","0","3","0","0","0","1"),
        arrayOf("2","0","0","0","2","0","0","0","2"),
        arrayOf("1","0","0","0","1","0","0","0","3"),
        arrayOf("4","3","2","1","5","1","2","3","4"),
        arrayOf("3","0","0","0","1","0","0","0","1"),
        arrayOf("2","0","0","0","2","0","0","0","2"),
        arrayOf("1","0","0","0","3","0","0","0","3"),
        arrayOf("4","3","2","1","4","3","2","1","4"),
    )
    private val colores = arrayOf(
       "verde",
        "azul",
        "amarillo",
        "rojo",
        "magenta"
    )

    fun  crearTablero(jugadores: List<JugadorEnPartida>){
        gridTablero.rowCount=9
        gridTablero.columnCount=9
        ////
        // amarillo dorado #FFD700
        // azul claro HEX: #1E90FF
        // rojo crimson HEX: #DC143C
        // verde HEX: #008000
        // magenta oscuro HEX: #8B008B
        for(i in 0 until gridTablero.rowCount){

            for (j in 0 until gridTablero.columnCount){

                val casilla = Casilla (contexto,i,j)
                casilla.rowCount=3
                casilla.columnCount=3
                val dificultad = tableroVersionUno[i][j].toInt()
                casilla.layoutParams= GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
                    casilla.dificultad = dificultad
                    if (tableroVersionUno[i][j] == "1"){
                        casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.verde))
                    }else if (tableroVersionUno[i][j] == "2"){
                        casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.azul))
                    }else if (tableroVersionUno[i][j] == "3"){
                        casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.amarillo))
                    }else if (tableroVersionUno[i][j] == "4"){
                        casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.rojo))
                    }else if (tableroVersionUno[i][j] == "5"){
                        casilla.jugadores = jugadores
                        casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.magenta))
                        for (jugador in jugadores){
                            val nuevoJugador = ImageView(contexto)
                            val resourceId = contexto.resources.getIdentifier(jugador.jugador.avatar, "drawable", contexto.packageName)
                            nuevoJugador.setImageResource(resourceId)
                            nuevoJugador.layoutParams= GridLayout.LayoutParams().apply {
                                width = 0
                                height = 0
                                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            }
                            casilla.addView(nuevoJugador)
                        }
                    } else {
                        casilla.setBackgroundColor(ContextCompat.getColor(contexto,R.color.negro))
                    }


                gridTablero.addView(casilla)
            }

        }

        ////



    }
    fun moverJugador(jugador:JugadorEnPartida , movimientos : Int){
        var x  = jugador.casillaActual.split("_")[0].toInt()
        var y = jugador.casillaActual.split("_")[1].toInt()
        moverJugador2(x, y, movimientos)
    }
    fun moverJugador2(x:Int,y:Int , movimientos : Int){
        var movimientos = 6
        val arriba  = true
        val abajo  = true
        val izquierda  = true
        val derecha = true
        var x2 :Int
        var y2 :Int
        if (x==9){
            !derecha
        }
        if (x==0){
            !izquierda
        }
        if(y==0){
            !arriba
        }
        if(y==9){
            !abajo
        }


        var casilla : Casilla? = null
        //derecha
        if (derecha) {
             x2 = x + movimientos
             y2 = y

            if (x2 > 9) {
                y2 = y + x2 - 8

                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + 8) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
                y2 = y - x2 + 8

                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + 8) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            } else {
                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            }
        }
        if (izquierda) {
            //izquierda
            x2 = x - movimientos
            y2 = y
            if (x2 < 0) {
                y2 = y - x2
                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + 0) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
                y2 = y + x2
                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + 0) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            } else {
                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            }
        }
        if (arriba) {
            //arriba
            x2 = x
            y2 = y - movimientos
            if (y2 < 0) {
                x2 = x - y2
                casilla = gridTablero.getChildAt( x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
                x2 = x + y2
                casilla = gridTablero.getChildAt( x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            } else {
                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            }
        }
        if (abajo) {
            //abajo
            x2 = x
            y2 = y + movimientos
            if (y2 > 9) {
                x2 = x + y2 - 8
                casilla = gridTablero.getChildAt(8 * gridTablero.columnCount + x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
                x2 = x + 8
                casilla = gridTablero.getChildAt(8 * gridTablero.columnCount + x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            } else {
                casilla = gridTablero.getChildAt(y2 * gridTablero.columnCount + x2) as Casilla
                casilla.setBackgroundColor(ContextCompat.getColor(contexto, R.color.negro))
            }
        }



    }

}