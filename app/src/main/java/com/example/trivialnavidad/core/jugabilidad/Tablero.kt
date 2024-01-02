package com.example.trivialnavidad.core.jugabilidad

import android.content.Context
import android.view.Gravity
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import com.example.trivialnavidad.R

class Tablero {
     val gridTablero = GridLayout(this)// me pide un contexto pero no se pasarselo
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


    private fun  crearTablero(tableroGrid: GridLayout, contexto: Context): GridLayout{
        tableroGrid.rowCount=9
        tableroGrid.columnCount=9
  ////
        // amarillo dorado #FFD700
        // azul claro HEX: #1E90FF
        // rojo crimson HEX: #DC143C
        // verde HEX: #008000
        // magenta oscuro HEX: #8B008B
        for(i in 0 until tableroGrid.rowCount){

            for (j in 0 until tableroGrid.columnCount){

                var botonTablero = Button(contexto)
                botonTablero.text="$i y $j"
                botonTablero.layoutParams= GridLayout.LayoutParams().apply {
                    rowSpec= GridLayout.spec(i)
                    columnSpec= GridLayout.spec(j)

                    if (tableroVersionUno[i,j].equals("1")){
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto, R.color.verde))
                    }else if (tableroVersionUno[i,j].equals("2")){
                        botonTablero.setBackgroundColor(androidx.core.content.ContextCompat.getColor(contexto, com.example.trivialnavidad.R.color.azul))
                    }else if (tableroVersionUno[i,j].equals("3")){
                        botonTablero.setBackgroundColor(androidx.core.content.ContextCompat.getColor(contexto, com.example.trivialnavidad.R.color.amarillo))
                    }else if (tableroVersionUno[i,j].equals("5")){
                        botonTablero.setBackgroundColor(androidx.core.content.ContextCompat.getColor(contexto, com.example.trivialnavidad.R.color.magenta))
                    } else {
                        botonTablero.setBackgroundColor(ContextCompat.getColor(contexto,R.color.negro))
                    }
                    //gravity= Gravity.FILL
                }
                gridTablero.addView(botonTablero)
            }
        }

        ////

        return  tableroGrid

    }
}