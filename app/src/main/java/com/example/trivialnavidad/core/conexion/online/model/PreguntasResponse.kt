package com.example.t8_ej03_persistenciaapi.model

// Definición de la clase Post para modelar los datos que se reciben de la API.
// Esta clase tiene cuatro propiedades que corresponden a los datos de una publicación: userId, id, title y body.
data class PreguntasResponse(
    val preguntas: List<Pregunta>
)

data class Pregunta(
    val dificultad: Int,
    val pregunta: String,
    val respuestas: List<String>,
    val correcta: String
)
