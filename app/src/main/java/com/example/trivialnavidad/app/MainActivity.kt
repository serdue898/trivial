package com.example.trivialnavidad.app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.juego.view.Juego
import com.example.trivialnavidad.core.feature.principal.view.Principal

class MainActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var juego=null as Juego?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            var principal = Principal()
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedor, principal)
                .commit()
        }

    }



}