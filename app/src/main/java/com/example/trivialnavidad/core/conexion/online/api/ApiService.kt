package com.example.t8_ej03_persistenciaapi.api

import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.t8_ej03_persistenciaapi.model.PreguntasResponse
import retrofit2.http.GET
import retrofit2.Call

interface ApiService {
    // Definición del método para obtener publicaciones. Utiliza la anotación @GET para indicar
    // que es una solicitud GET HTTP. La función devuelve un objeto Call que encapsula una lista de Posts.
    @GET("preguntas.json")
    fun getPreguntas(): Call<List<Pregunta>>
    @GET("/preguntas")
    fun getPreguntasApi(): Call<List<Pregunta>>
}
