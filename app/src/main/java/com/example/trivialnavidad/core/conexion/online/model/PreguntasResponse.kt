package com.example.t8_ej03_persistenciaapi.model

data class Pregunta(
    val dificultad: Int,
    val pregunta: String,
    val respuestas: List<String>,
    val correcta: List<String>
)
