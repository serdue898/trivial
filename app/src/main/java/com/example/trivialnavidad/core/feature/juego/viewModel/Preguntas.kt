package com.example.trivialnavidad.core.feature.juego.viewModel

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.t8_ej03_persistenciaapi.model.Pregunta
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.conexion.online.api.RetrofitClient.obtenerTodasLasPreguntas

/**
 * Clase Preguntas: Gestiona la carga y filtrado de preguntas del juego Trivial.
 *
 * @property contexto Contexto de la aplicación.
 * @property preguntas Lista de preguntas cargadas.
 */
class Preguntas(private val contexto: Context) {
    private var preguntas: List<Pregunta>? = null

    /**
     * Método cargarPreguntas: Carga todas las preguntas disponibles y muestra el resultado en el registro.
     */
    fun cargarPreguntas() {
        obtenerTodasLasPreguntas { preguntasObtenidas, respuesta ->
            preguntas = preguntasObtenidas
            Log.d("Preguntas", respuesta.toString())
        }
    }

    /**
     * Método preguntasDificultad: Filtra y devuelve las preguntas de una dificultad específica.
     *
     * @param dificultad Nivel de dificultad de las preguntas a filtrar.
     * @return Lista de preguntas filtradas por dificultad.
     */
    fun preguntasDificultad(dificultad: Int): List<Pregunta>? {
        return preguntas?.filter { it.dificultad == dificultad }
    }

    /**
     * Método mostrarAlertaSinConexion: Muestra una alerta indicando que no hay conexión a Internet
     * y proporciona opciones al usuario para configurar la conexión.
     */
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

    /**
     * Método mostrarConfiguracionWifi: Abre la configuración de Wi-Fi del dispositivo.
     */
    private fun mostrarConfiguracionWifi() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        contexto.startActivity(intent)
    }

    /**
     * Método verificarConexionYRecargarPreguntas: Verifica la conexión a Internet y recarga las preguntas
     * si hay conexión, mostrando una alerta en caso contrario.
     */
    fun verificarConexionYRecargarPreguntas() {
        if (!hayConexionInternet(contexto)) {
            mostrarAlertaSinConexion()
        } else {
            cargarPreguntas()
        }
    }

    /**
     * Método hayConexionInternet: Verifica si hay conexión a Internet en el dispositivo.
     *
     * @param contexto Contexto de la aplicación.
     * @return true si hay conexión a Internet, false de lo contrario.
     */
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
