package com.example.trivialnavidad.app

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService

class Vibracion(context: Context) {

    private val vibrator: Vibrator? = context.getSystemService() as? Vibrator

    fun vibrar(duracion: Long) {
        // Para dispositivos con Android Oreo (API 26) o versiones posteriores
        vibrator?.vibrate(VibrationEffect.createOneShot(duracion, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}