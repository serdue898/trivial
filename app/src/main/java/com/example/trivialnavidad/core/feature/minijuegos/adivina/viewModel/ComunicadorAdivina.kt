package com.example.trivialnavidad.core.feature.minijuegos.adivina.viewModel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.core.feature.juego.view.Juego

interface ComunicadorAdivina {
     fun volver( context: Context , ganado: Boolean)
}