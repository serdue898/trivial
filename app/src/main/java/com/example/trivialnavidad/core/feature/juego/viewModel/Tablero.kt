package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.conexion.onffline.Conexion
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.clasificacion.view.Clasifiaccion
import com.example.trivialnavidad.core.feature.juego.view.Adivina
import com.example.trivialnavidad.core.feature.juego.view.Juego
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Tablero (var gridTablero: GridLayout, var contexto: Context ,var jugadores: List<JugadorEnPartida>) {
    private var JugadorActual : JugadorEnPartida? = null
    private var posiblesMovimientos = mutableListOf<Casilla>()
    private val conexion = Conexion(contexto)
    val juego = MainActivity.juego as Juego
    val preguntas = preguntas()
    val avatarImages = contexto.resources.obtainTypedArray(R.array.avatar_images)

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
    fun asignarJugadores(){
        for (jugador in jugadores){
            val fila  = jugador.casillaActual.split("_")[0].toInt()
            val columna = jugador.casillaActual.split("_")[1].toInt()
            val casilla = obtenerCasilla(fila,columna)
            casilla?.addJugador(jugador)
            rellenarCasilla(casilla!!)
        }
    }

    fun  crearTablero(){
        preguntas.cogerpreguntas()
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
                    casilla.dificultad = dificultad.toInt()
                    if (tableroVersionUno[i][j] == "1"){
                        casilla.color = ContextCompat.getColor(contexto, R.color.verde)
                    }else if (tableroVersionUno[i][j] == "2"){
                        casilla.color = ContextCompat.getColor(contexto, R.color.azul)
                    }else if (tableroVersionUno[i][j] == "3"){
                        casilla.color = ContextCompat.getColor(contexto, R.color.amarillo)
                    }else if (tableroVersionUno[i][j] == "4"){
                        casilla.color = ContextCompat.getColor(contexto, R.color.rojo)
                    }else if (tableroVersionUno[i][j] == "5"){
                        casilla.color = ContextCompat.getColor(contexto, R.color.magenta)
                    } else {
                        casilla.color = ContextCompat.getColor(contexto, R.color.negro)
                    }
                casilla.setBackgroundColor(casilla.color)
                casilla.setOnClickListener {
                    moverVista( casilla)
                    conexion.actualizarCasillaActual(JugadorActual!!)
                    val preguntasMinijuego = preguntas.preguntasDificultad(casilla.dificultad)
                    val pregunta = preguntasMinijuego?.random()
                    var minijuego : Fragment? = null
                    if (casilla.dificultad==1){
                        minijuego = Adivina(pregunta!!, JugadorActual!!)
                    }else if (casilla.dificultad==2){
                        val listaPtreguntas :MutableList<Pregunta> = mutableListOf()
                        for  (i in 0 until 5){
                            listaPtreguntas.add(preguntasMinijuego?.random()!!)
                        }
                    }else{
                        juego.resultadoMiniJuego(true)
                    }
                    if(minijuego!=null) {
                        if (contexto is AppCompatActivity) {
                            val fragmentManager =
                                (contexto as AppCompatActivity).supportFragmentManager
                            fragmentManager.beginTransaction()
                                .replace(R.id.contenedor, minijuego!!)
                                .commit()

                        }
                    }

                }
                casilla.isEnabled = false


                gridTablero.addView(casilla)
            }

        }

        ////



    }
    fun moverJugador(jugador:JugadorEnPartida , movimientos : Int){
        val x  = jugador.casillaActual.split("_")[0].toInt()
        val y = jugador.casillaActual.split("_")[1].toInt()
        JugadorActual = jugador
        PosiblesMovimientos(x, y, movimientos,"",jugador)
    }


    fun PosiblesMovimientos(fila: Int, columna: Int, movimientos: Int, direction: String, jugador: JugadorEnPartida) {
        if (movimientos == 0) {
            GlobalScope.launch(Dispatchers.Default) {
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
        if (fila==4 && columna==4 ){
            Log.d("da","entro")
        }
        if (direction!="izquierda")moverEnDireccion(fila, columna+1, movimientos - 1,"derecha",jugador)
        if (direction!="derecha")moverEnDireccion(fila, columna-1, movimientos - 1,"izquierda",jugador)
        if (direction!="abajo")moverEnDireccion(fila-1, columna, movimientos - 1,"arriba",jugador)
        if (direction!="arriba")moverEnDireccion(fila+1, columna, movimientos - 1,"abajo",jugador)
    }

    private fun moverEnDireccion(fila: Int, columna: Int, movimientos: Int,direction:String,jugador: JugadorEnPartida) {
        if (fila in 0 until gridTablero.columnCount && columna in 0 until gridTablero.rowCount) {
            if (!comprobarCasilla(fila,columna)) {
                PosiblesMovimientos( fila, columna, movimientos,direction,jugador)
            }
        }
    }

    private fun obtenerCasilla(fila: Int, columna: Int): Casilla? {
        return gridTablero.getChildAt(fila  * gridTablero.columnCount + columna) as? Casilla
    }

    fun comprobarCasilla(fila :Int, columna:Int):Boolean{
        val casilla = gridTablero.getChildAt(fila * gridTablero.columnCount + columna) as Casilla
        val colorDeFondo = (casilla.background as? ColorDrawable)?.color
        return colorDeFondo == ContextCompat.getColor(contexto, R.color.negro)
    }

    fun moverVista(casillaNueva: Casilla){
        val fila  = JugadorActual?.casillaActual?.split("_")?.get(0)?.toInt()
        val columna = JugadorActual?.casillaActual?.split("_")?.get(1)?.toInt()
        val casilla = obtenerCasilla(fila!!,columna!!)

        casillaNueva.addJugador(JugadorActual!!)
        rellenarCasilla(casillaNueva)
        casilla?.removeJugador(JugadorActual!!)
        rellenarCasilla(casilla!!)

        JugadorActual!!.casillaActual= casillaNueva.fila.toString() +"_"+ casillaNueva.columna.toString()
        reiniciarMovimientos()

    }
    @OptIn(DelicateCoroutinesApi::class)
    fun reiniciarMovimientos(){
        GlobalScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                for ( casilla in posiblesMovimientos){
                    casilla.setBackgroundColor(casilla.color)
                    casilla.isEnabled = false
                }
                posiblesMovimientos.clear()
            }
        }

    }

    fun rellenarCasilla(casilla: Casilla){
        casilla.removeAllViews()
        for (jugador in casilla.jugadores){
            val nuevoJugador = ImageView(contexto)
            nuevoJugador.setImageDrawable(avatarImages.getDrawable(jugador.avatar.toInt()))
            nuevoJugador.layoutParams= GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            casilla.addView(nuevoJugador)
        }

    }




}