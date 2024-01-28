package com.example.trivialnavidad.core.feature.minijuegos.parejas.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.parejas.viewModel.ComunicadorParejas
import com.example.trivialnavidad.core.feature.minijuegos.parejas.viewModel.MetodosParejas
import java.text.Normalizer

/**
 * Fragmento que representa el mini-juego "Parejas".
 *
 * @property pregunta La pregunta para el mini-juego.
 * @property jugador El jugador asociado con este mini-juego.
 */
class Parejas(val pregunta: Pregunta, val jugador: JugadorEnPartida) : Fragment() {

    private var contexto: Context? = null
    private var comunicador: ComunicadorParejas = MetodosParejas()
    private var imagenesBase: MutableList<Drawable> = mutableListOf()
    private var seleccionado: FrameLayout? = null
    private var aciertos = 0

    /**
     * Llamado para crear y devolver la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater El objeto [LayoutInflater] que se puede utilizar para inflar vistas en el fragmento.
     * @param container Si no es nulo, esta es la vista principal a la que se debe adjuntar la interfaz de usuario del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento se está reconstruyendo a partir de un estado guardado anteriormente.
     *
     * @return Devuelve la [View].
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.minijuego_parejas, container, false)
        contexto = container?.context
        pregunta.respuestas.forEach { i ->
            val nombre = Normalizer.normalize(i, Normalizer.Form.NFD)
                .replace("[^\\p{ASCII}]".toRegex(), "")
                .lowercase()
            val id = obtenerResourceId(nombre)
            val drawable = ContextCompat.getDrawable(contexto!!, id)
            imagenesBase.add(drawable!!)
        }
        val fr1 = view.findViewById<FrameLayout>(R.id.fr_1)
        val fr2 = view.findViewById<FrameLayout>(R.id.fr_2)
        val fr3 = view.findViewById<FrameLayout>(R.id.fr_3)
        val fr4 = view.findViewById<FrameLayout>(R.id.fr_4)
        fr1.addView(ImageView(contexto).apply {
            setImageDrawable(imagenesBase[0])
        })
        fr1.tag = pregunta.respuestas[0]
        fr2.addView(ImageView(contexto).apply {
            setImageDrawable(imagenesBase[1])
        })
        fr2.tag = pregunta.respuestas[1]
        fr3.addView(TextView(contexto).apply {
            text = pregunta.respuestas[1]
            background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra)
            gravity = Gravity.CENTER
        })
        fr3.tag = pregunta.respuestas[1]
        fr4.addView(TextView(contexto).apply {
            text = pregunta.respuestas[0]
            background = ContextCompat.getDrawable(contexto!!, R.drawable.fondo_letra)
            gravity = Gravity.CENTER
        })
        fr4.tag = pregunta.respuestas[0]
        fr1.setOnClickListener {
            actualizarCelda(fr1)
        }
        fr2.setOnClickListener {
            actualizarCelda(fr2)
        }
        fr3.setOnClickListener {
            actualizarCelda(fr3)
        }
        fr4.setOnClickListener {
            actualizarCelda(fr4)
        }
        return view
    }

    /**
     * Método que obtiene el ID del recurso de la imagen a partir del nombre de la imagen.
     *
     * @param imageName El nombre de la imagen.
     * @return Devuelve el ID del recurso de la imagen.
     */
    private fun obtenerResourceId(imageName: String): Int {
        // Aquí deberías implementar la lógica para obtener el ID del recurso de la imagen
        // Puedes hacer esto de acuerdo a cómo tienes organizados tus recursos de imágenes
        // En este ejemplo, asumo que tienes imágenes con nombres como "dado1", "dado2", etc.
        return R.drawable::class.java.getField(imageName).get(null) as Int
    }

    /**
     * Método que actualiza la celda seleccionada y verifica si se ha completado el juego.
     *
     * @param frame El [FrameLayout] seleccionado.
     */
    private fun actualizarCelda(frame: FrameLayout) {
        if (seleccionado != null) {
            if (seleccionado!!.tag.toString() == frame.tag.toString()) {
                frame.visibility = View.INVISIBLE
                seleccionado?.visibility = View.INVISIBLE
                seleccionado = null
                aciertos++
                if (aciertos == 2) {
                    terminarJuego(true)
                }
            } else {
                terminarJuego(false)
            }
        } else {
            frame.isEnabled = false
            seleccionado = frame
        }
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
            jugador.juegos[3] = true
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
            comunicador.volver(contexto!!, ganado)
        }
        msnEmergente.show()
    }
}