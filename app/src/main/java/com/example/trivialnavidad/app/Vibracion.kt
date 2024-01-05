package com.example.trivialnavidad.app

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService

class Vibracion(private val context: Context) {

    private val vibrator: Vibrator? = context.getSystemService() as? Vibrator

    fun vibrar(duracion: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Para dispositivos con Android Oreo (API 26) o versiones posteriores
            vibrator?.vibrate(VibrationEffect.createOneShot(duracion, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Para dispositivos con versiones anteriores a Android Oreo
            @Suppress("DEPRECATION")
            vibrator?.vibrate(duracion)
        }
    }
}