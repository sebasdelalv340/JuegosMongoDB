package org.example

import java.time.LocalDate

data class Juego(
    val titulo: String,
    val genero: String,
    val precio: Double,
    val fecha_lanzamiento: LocalDate
) {
    init { require(titulo.isNotEmpty()) { "El título no puede estar vacío" } }
}