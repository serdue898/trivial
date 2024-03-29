package com.example.trivialnavidad.core.conexion.online.api

import android.util.Log
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base de la API.
    private const val BASE_URL = "https://trivial-1d526-default-rtdb.firebaseio.com/"

    // Inicialización perezosa del ApiService. Se crea una instancia de Retrofit con la URL base
    // y el convertidor Gson para la serialización y deserialización automática de los datos.
    private val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Creación de la implementación de ApiService usando Retrofit.
        retrofit.create(ApiService::class.java)
    }

    fun obtenerTodasLasPreguntas(callback: (preguntas:List<Pregunta>?, error: String?) -> Unit) {
        val apiService = instance
        val call = apiService.getPreguntas()
        call.enqueue(object : Callback<List<Pregunta>> {
            override fun onResponse(call: Call<List<Pregunta>>, response: Response<List<Pregunta>>) {
                if (response.isSuccessful) {
                    val preguntas = response.body()
                    callback(preguntas, null)
                } else {
                    // Manejar el error y llamar al callback con información del error
                    Log.d("Preguntas", ""+response.code())
                    callback(null, "Error en la respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Pregunta>>, t: Throwable) {
                // Manejar el fallo y llamar al callback con información del error
                Log.d("Preguntas", "pata"+t.message)
                callback(null, "Error de red: ${t.message}")
            }
        })
    }
}

