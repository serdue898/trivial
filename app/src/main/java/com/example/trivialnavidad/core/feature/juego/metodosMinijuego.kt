package com.example.trivialnavidad.core.feature.juego

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.R

class metodosMinijuego {
     fun abrirJuego( Contexto: Context){
        if (Contexto is AppCompatActivity) {
            val MinijuegoTest_fragment = Test()
            Contexto.supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, MinijuegoTest_fragment)
                .commit()

        }


    }
}