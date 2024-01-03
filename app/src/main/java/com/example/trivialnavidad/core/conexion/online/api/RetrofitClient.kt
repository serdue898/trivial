package com.example.t8_ej03_persistenciaapi.api

import android.util.Log
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.t8_ej03_persistenciaapi.model.PreguntasResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume

object RetrofitClient {
    // URL base de la API.
    private const val BASE_URL = "https://trivial-1d526-default-rtdb.firebaseio.com/"

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

    fun obtenerTodasLasPreguntas(callback: (PreguntasResponse:PreguntasResponse?, error: String?) -> Unit) {
        val apiService = RetrofitClient.instance
        val call = apiService.getPreguntas()
        call.enqueue(object : Callback<PreguntasResponse> {
            override fun onResponse(call: Call<PreguntasResponse>, response: Response<PreguntasResponse>) {
                if (response.isSuccessful) {
                    val preguntas = response.body()
                    callback(preguntas, null)
                } else {
                    // Manejar el error y llamar al callback con información del error
                    Log.d("Preguntas", ""+response.code())
                    callback(null, "Error en la respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PreguntasResponse>, t: Throwable) {
                // Manejar el fallo y llamar al callback con información del error
                Log.d("Preguntas", "pata"+t.message)
                callback(null, "Error de red: ${t.message}")
            }
        })
    }
}

