package com.example.trivialnavidad.core.feature.juego.viewModel

import android.util.Log
import com.example.t8_ej03_persistenciaapi.api.RetrofitClient
import com.example.t8_ej03_persistenciaapi.api.RetrofitClient.obtenerTodasLasPreguntas
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.t8_ej03_persistenciaapi.model.PreguntasResponse
import kotlinx.coroutines.runBlocking

class preguntas {
    private var preguntas:List<Pregunta>? = null
    fun cogerpreguntas(){
        obtenerTodasLasPreguntas { pregntasOptenidas , respuesta ->
            preguntas = pregntasOptenidas
            Log.d("Preguntas", respuesta.toString())
        }
    }
    fun preguntasDificultad(dificultad:Int):List<Pregunta>?{
        var preguntasDificultad:List<Pregunta>? = null
        if(preguntas != null){
            preguntasDificultad = preguntas!!.filter { it.dificultad == dificultad }
        }
        return preguntasDificultad
    }

}