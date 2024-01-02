package com.example.t8_ej03_persistenciaapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base de la API.
    private const val BASE_URL = "http://192.168.0.202:5000/"

    // Inicialización perezosa del ApiService. Se crea una instancia de Retrofit con la URL base
    // y el convertidor Gson para la serialización y deserialización automática de los datos.
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Creación de la implementación de ApiService usando Retrofit.
        retrofit.create(ApiService::class.java)
    }

}
