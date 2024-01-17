package com.example.trivialnavidad.core.feature.principal.viewModel

import Partidas
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.trivialnavidad.R
import com.example.trivialnavidad.core.feature.juego.view.Juego
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialnavidad.app.MainActivity
import com.example.trivialnavidad.app.MainActivity.Companion.socket
import com.example.trivialnavidad.core.feature.seleccionJugadores.view.SeleccionJugador
import com.example.trivialnavidad.core.feature.unirseOnline.view.UnirseOnline
import io.socket.client.IO


class MetodosPrincipal: ComunicadorPrincipal {

    override fun abrir_juego(datos: String, context: Context) {
        if (context is AppCompatActivity) {
            if (datos == "cargar") {
                val partidas = Partidas("offline")
                val fragmentManager = context.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contenedor, partidas)
                    .commit()
            } else if (datos == "nueva") {
                // Código para el caso "online", si es necesario
                val seleccion = SeleccionJugador()
                val fragmentManager = context.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contenedor, seleccion)
                    .commit()
            }else if (datos =="online-crear"){
                val popup = AlertDialog.Builder(context)
                popup.setTitle("Crear partida")
                val view = context.layoutInflater.inflate(R.layout.partida_online, null)
                popup.setView(view)

                popup.setPositiveButton("Crear") { dialog, which ->
                    val nombre = view.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.et_nombre_partida).text.toString()
                    val socket = MainActivity.socket
                    socket?.emit("crearPartida", nombre)
                    socket?.on("partidaCreada") { args ->

                        val id = args[0].toString()
                        if (id == "null") {
                            context.runOnUiThread {
                                val errorPopup = AlertDialog.Builder(context)
                                errorPopup.setTitle("Error")
                                errorPopup.setMessage("Ya existe una partida con ese nombre")
                                errorPopup.setPositiveButton("Aceptar") { dialog, which ->
                                    dialog.dismiss()
                                }
                                errorPopup.show()
                            }
                        } else {
                            val unirse = UnirseOnline(id.toInt())
                            unirse.host = true
                            val fragmentManager = context.supportFragmentManager
                            fragmentManager.beginTransaction()
                                .replace(R.id.contenedor, unirse)
                                .commit()
                        }
                    }
                }
                context.runOnUiThread {
                    popup.show()
                }


            }else if (datos =="online-cargar"){


                val partidas = Partidas("online")
                partidas.cogerPartidas()
                val fragmentManager = context.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contenedor, partidas)
                    .commit()

            }
        }
    }

}