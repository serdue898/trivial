package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.widget.Button
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import com.example.trivialnavidad.R

class Tablero (var gridTablero: GridLayout) {
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
    private val filas =9
    private val columnas =9
    private val tableroVersionDos = Array(filas){ fila->
        Array(columnas){columna->
            "$fila $columna"}
    }


    fun  crearTablero( contexto: Context){
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

                var botonTablero = Button(contexto)
                botonTablero.layoutParams= GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1f)
                    rowSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1f)

                    if (tableroVersionUno[i][j].equals("1")){
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto, R.color.verde))
                    }else if (tableroVersionUno[i][j].equals("2")){
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto, R.color.azul))
                    }else if (tableroVersionUno[i][j].equals("3")){
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto, R.color.amarillo))
                    }else if (tableroVersionUno[i][j].equals("5")){
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto, R.color.magenta))
                    } else {
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto,R.color.negro))
                    }
                    //gravity= Gravity.FILL
                }
                gridTablero.addView(botonTablero)
            }
        }

        ////


    }
}