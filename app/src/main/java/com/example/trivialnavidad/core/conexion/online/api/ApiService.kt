package com.example.trivialnavidad.core.conexion.online.api

import com.example.t8_ej03_persistenciaapi.model.Pregunta
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // Definición del método para obtener publicaciones. Utiliza la anotación @GET para indicar
    // que es una solicitud GET HTTP. La función devuelve un objeto Call que encapsula una lista de Posts.
    @GET("preguntas.json")
    fun getPreguntas(): Call<List<Pregunta>>
}
