package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.t8_ej03_persistenciaapi.api.RetrofitClient.obtenerTodasLasPreguntas
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R

class Preguntas(private val contexto: Context) {
    private var preguntas: List<Pregunta>? = null

    fun cargarPreguntas() {
        obtenerTodasLasPreguntas { preguntasObtenidas, respuesta ->
            preguntas = preguntasObtenidas
            Log.d("Preguntas", respuesta.toString())
        }
    }

    fun preguntasDificultad(dificultad: Int): List<Pregunta>? {
        return preguntas?.filter { it.dificultad == dificultad }
    }

    private fun mostrarAlertaSinConexion() {
        val alert = AlertDialog.Builder(contexto)
        alert.setTitle(contexto.getString(R.string.juego))
        alert.setCancelable(false)
        alert.setMessage(contexto.getString(R.string.internet_mensaje))
        alert.setPositiveButton(contexto.getString(R.string.aceptar)) { dialog, _ ->
            mostrarConfiguracionWifi()
            dialog.dismiss()
        }
        alert.show()
    }



    private fun mostrarConfiguracionWifi() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        contexto.startActivity(intent)
    }

    fun verificarConexionYRecargarPreguntas() {
        if (!hayConexionInternet(contexto)) {
            mostrarAlertaSinConexion()
        } else {
            cargarPreguntas()
        }
    }

    private fun hayConexionInternet(contexto: Context): Boolean {
        val connectivityManager = contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}