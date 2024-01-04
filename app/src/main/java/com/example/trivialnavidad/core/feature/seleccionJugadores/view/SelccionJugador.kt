package com.example.trivialnavidad.core.feature.seleccionJugadores.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.Jugador
import com.example.trivialnavidad.core.feature.seleccionJugadores.viewModel.MetodosSeleccion

class SelccionJugador : Fragment() {

    private var comunicador: MetodosSeleccion = MetodosSeleccion()
    private var contexto: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.seleccion_jugadores, container, false)
        contexto = container?.context

        /*
        // codigo de clasificacion y no se si es necesario o no

      val b_guardar = view.findViewById<Button>(R.id.bt_volver)
        bt_volver.setOnClickListener {
            comunicador?.volver(contexto!!)
          }
    */
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jugadoresEnPartida: List<Jugador> = listOf()

        val b_guardarJugador = view?.findViewById<Button>(R.id.b_guardarJugador)
        // el botton editar me sobra ya que si se quiere editar algo sera mas sencillo pinchar en el elemento y que se ponga en edittext y el spinner
        //val b_guardarModificar = view?.findViewById<Button>(R.id.b_editarJugador)
        val b_eliminarJugador = view?.findViewById<Button>(R.id.b_eliminarJugador)
        b_eliminarJugador?.isEnabled = false
        val textoNombreJugador = view?.findViewById<EditText>(R.id.eT_nombreJugador)

        val listajugadores = view?.findViewById<RecyclerView>(R.id.ryV_listaJugadores)

        listajugadores?.setOnClickListener() {
            var nombreJugador = view?.findViewById<EditText>(R.id.eT_nombreJugador)
            var avatarJugador = view?.findViewById<Spinner>(R.id.sp_avatares)
            // al seleccionar un elemento de la lista se muestra en el edittext y en el spinenr con el la informacion que corresponda
            // nombreJugador?.text= listajugadores.toString()
        }

        if (textoNombreJugador.toString() == null) {
            b_guardarJugador?.text = "Nuevo jugador"
            b_guardarJugador?.setOnClickListener() {

                agregarJugadorLista(jugadoresEnPartida, null)
            }
        } else {
            agregarJugadorLista(jugadoresEnPartida, null)
            b_eliminarJugador?.isEnabled = true
            b_eliminarJugador?.setOnClickListener {

            }

        }

    }

    fun agregarJugadorLista(jugadoresEnPartida: List<Jugador>, nombre: String?) {
        var nombreJugador = view?.findViewById<EditText>(R.id.eT_nombreJugador)
        var avatarJugador = view?.findViewById<Spinner>(R.id.sp_avatares)
        var jugadorNuevo: Jugador = Jugador(0, nombreJugador.toString(), avatarJugador.toString())
        jugadoresEnPartida.plus(jugadorNuevo)

    }

    fun eliminarJugador(jugadoresEnPartida: List<Jugador>) {
        jugadoresEnPartida.forEach { (nombre, avatar) ->

        }
    }

}
