package com.example.trivialnavidad.core.feature.minijuegos.adivina.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.ComunicadorAdivina
import com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel.MetodosAdivina
import java.text.Normalizer

/**
 * Fragmento que representa el mini-juego "Adivina".
 *
 * @property pregunta La pregunta para el mini-juego.
 * @property jugador El jugador asociado con este mini-juego.
 */
class Adivina(var pregunta: Pregunta, var jugador: JugadorEnPartida) : Fragment() {
    private var comunicador: ComunicadorAdivina? = MetodosAdivina()
    private var contexto: Context? = null
    private var fallos = 0
    private var mostradoFinal = false

    /**
     * Llamado para crear y devolver la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater El objeto [LayoutInflater] que se puede utilizar para inflar vistas en el fragmento.
     * @param container Si no es nulo, esta es la vista principal a la que se debe adjuntar la interfaz de usuario del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento se está reconstruyendo a partir de un estado guardado anteriormente.
     *
     * @return Devuelve la [View].
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.minijuego_adivina, container, false)
        contexto = container?.context
        val imagenes = arrayOf(
            R.drawable.ahorcado_1, R.drawable.ahorcado_2, R.drawable.ahorcado_3,
            R.drawable.ahorcado_4, R.drawable.ahorcado_5, R.drawable.ahorcado_6
        )

        val imagen = view.findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.imagenAhorcado)
        val txPregunta = view.findViewById<TextView>(R.id.t_pregunta)
        val palabraAdivinar = Normalizer.normalize(pregunta.correcta[0], Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")
            .lowercase()
        val palabraDividida = view.findViewById<TextView>(R.id.t_palabraAdivina)
        txPregunta.text = pregunta.pregunta
        val grAbecedario = view.findViewById<GridLayout>(R.id.gr_botonesLetras)

        val abecedario = CharArray(27) { ('a' + it) }
        abecedario[26] = 'ñ'
        var palabraHuecos = desgranarPalabra(palabraAdivinar)
        palabraDividida.text = palabraHuecos
        grAbecedario.rowCount = 9
        grAbecedario.columnCount = 4
        for (letra in abecedario) {
            val botonLetra = Button(contexto)
            botonLetra.text = letra.toString()
            botonLetra.background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra)

            val params = GridLayout.LayoutParams()
            params.width = GridLayout.LayoutParams.WRAP_CONTENT
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.setMargins(5, 5, 5, 5)

            // Agrega el botón al GridLayout con los parámetros de diseño
            grAbecedario.addView(botonLetra, params)

            botonLetra.setOnClickListener {
                if (mostradoFinal)
                    return@setOnClickListener
                botonLetra.isEnabled = false
                if (comprobarLetra(botonLetra, palabraAdivinar)) {
                    botonLetra.background =
                        ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra_acertada)
                    palabraHuecos =
                        rellenarPalabra(
                            palabraHuecos.replace(" ", ""),
                            palabraAdivinar,
                            botonLetra.text.toString()
                        )
                    palabraDividida.text = palabraHuecos
                } else {
                    botonLetra.background =
                        ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra_fallada)
                    fallos++
                    if (fallos > 6)
                        fallos = 6
                    imagen.setImageResource(imagenes[fallos - 1])
                }
                if (fallos >= 6) {
                    mostradoFinal = true
                    terminarJuego(false)
                }
                if (comprobarPlabra(palabraHuecos.replace(" ", ""))) {
                    mostradoFinal = true
                    terminarJuego(true)
                }
            }
        }

        return view
    }

    /**
     * Método que descompone la palabra en espacios en blanco y subraya cada letra.
     *
     * @param palabra La palabra a desgranar.
     * @return Devuelve la palabra dividida.
     */
    private fun desgranarPalabra(palabra: String): String {
        var palabraDividida = ""
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for (hueco in arrayDeLetras) {
            palabraDividida += "_ "
        }
        return palabraDividida
    }

    /**
     * Método que comprueba si la letra pulsada está presente en la palabra.
     *
     * @param botonPulsado El botón que representa la letra pulsada.
     * @param palabra La palabra a adivinar.
     * @return Devuelve true si la letra está presente, false en caso contrario.
     */
    private fun comprobarLetra(botonPulsado: Button, palabra: String): Boolean {
        var correcto = false
        val arrayDeLetras = palabra.toCharArray().map { it.toString() }.toTypedArray()
        for (hueco in arrayDeLetras) {
            if (botonPulsado.text == hueco) {
                correcto = true
            }
        }
        return correcto
    }

    /**
     * Método que comprueba si todas las letras de la palabra han sido adivinadas.
     *
     * @param palabraDividida La palabra dividida con letras adivinadas y espacios en blanco.
     * @return Devuelve true si todas las letras han sido adivinadas, false en caso contrario.
     */
    private fun comprobarPlabra(palabraDividida: String): Boolean {
        val arrayDeLetras = palabraDividida.toCharArray().map { it.toString() }.toTypedArray()
        for (hueco in arrayDeLetras) {
            if (hueco == "_") {
                return false
            }
        }
        return true
    }

    /**
     * Método que actualiza la palabra con las letras adivinadas.
     *
     * @param palabraHuecos La palabra con espacios en blanco y letras adivinadas.
     * @param palabraAdivinar La palabra completa a adivinar.
     * @param letra La letra que se ha adivinado.
     * @return Devuelve la palabra actualizada con las letras adivinadas.
     */
    private fun rellenarPalabra(palabraHuecos: String, palabraAdivinar: String, letra: String): String {
        var palabraHuecosActualizados = ""
        val letrasAdivinar = palabraAdivinar.toCharArray().map { it.toString() }.toTypedArray()
        val letrasDividida = palabraHuecos.toCharArray().map { it.toString() }.toTypedArray()

        for ((posicion, hueco) in letrasDividida.withIndex()) {
            palabraHuecosActualizados += if (letrasAdivinar[posicion] == letra) {
                "$letra "
            } else {
                "$hueco "
            }
        }
        return palabraHuecosActualizados
    }

    /**
     * Método que finaliza el juego y muestra un cuadro de diálogo con el resultado.
     *
     * @param resultado true si el juego se ganó, false si se perdió.
     */
    private fun terminarJuego(resultado: Boolean) {
        var ganado = false
        val acercaDe: Int
        if (resultado) {
            acercaDe = R.string.acierto
            jugador.juegos[1] = true
            ganado = true
        } else {
            acercaDe = R.string.fallo
        }
        val msnEmergente = AlertDialog.Builder(contexto as AppCompatActivity)
        msnEmergente.setCancelable(false)
        msnEmergente.setMessage(getString(acercaDe))
        msnEmergente.setPositiveButton(
            (contexto as AppCompatActivity).getString(R.string.aceptar)
        ) { _, _ ->
            comunicador?.volver(contexto!!, ganado)
        }
        msnEmergente.show()
    }
}