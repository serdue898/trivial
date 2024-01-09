package com.example.trivialnavidad.core.feature.minijuegos.repaso.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.onffline.modelo.JugadorEnPartida
import com.example.trivialnavidad.core.feature.minijuegos.repaso.viewModel.ComunicadorRepaso
import com.example.trivialnavidad.core.feature.minijuegos.repaso.viewModel.MetodosRepaso

class Repaso(var pregunta : Pregunta, var jugador : JugadorEnPartida) : Fragment() {

    private var comunicador: ComunicadorRepaso? = MetodosRepaso()
    private var contexto: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // enunciado comodin para generar los huecos
        var enunciadoComodin = pregunta.pregunta

        // asignar las preguntas a los elementos de la vista
        var respuestasRepaso : List<String> = listOf()
        respuestasRepaso= pregunta.respuestas
        val enunciado = view?.findViewById<TextView>(R.id.t_enunciadoRepaso)
        enunciado?.text= generarHuecosTexto(enunciadoComodin, respuestasRepaso)

        // falta coger las respuestas de la base de datos
        val view = inflater.inflate(R.layout.minijuego_repaso, container, false)
        contexto = container?.context

        // declarar cada uno de spinner que va a contener la información aunque está seá repetida
        val sp_hueco1 = view.findViewById<Spinner>(R.id.sp_hueco1)
        val sp_hueco2 = view.findViewById<Spinner>(R.id.sp_hueco2)
        val sp_hueco3 = view.findViewById<Spinner>(R.id.sp_hueco3)
        val sp_hueco4 = view.findViewById<Spinner>(R.id.sp_hueco4)

        // para suplir el error que se muestra al poner this en el contexto hay que poner requireContext()
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, respuestasRepaso)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_hueco1.adapter = adapter
        sp_hueco2.adapter = adapter
        sp_hueco3.adapter = adapter
        sp_hueco4.adapter = adapter



        val bt_corregir = view.findViewById<Button>(R.id.bt_repasoCorregir)

        bt_corregir.setOnClickListener(){
            val mensaje = AlertDialog.Builder(contexto as AppCompatActivity)
            if((sp_hueco1.isSelected.toString()== pregunta.correcta)&&
                (sp_hueco2.isSelected.toString()== pregunta.correcta)&&
                (sp_hueco3.isSelected.toString()== pregunta.correcta)&&
                (sp_hueco4.isSelected.toString()== pregunta.correcta)){
                mensaje.setMessage("Enhorabuena, has resuelto correctamente la prueba.")

            }else{
                mensaje.setMessage("Desgraciadamente te has confundido al resolver la prueba. Más suerte para la próxima.")
            }
            mensaje.show()

        }
        return view
    }

    fun generarHuecosTexto(enunciado: String, respuestas : List<String>): String{
        var i =1
        var enunciadoHuecos =""
        for (palabra in respuestas){
            enunciadoHuecos = enunciado.replace(palabra, "hueco" + i)
            i++
        }
        return  enunciadoHuecos
    }
}