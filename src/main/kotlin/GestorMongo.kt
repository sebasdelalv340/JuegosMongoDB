package org.example

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import java.time.LocalDate

class GestorMongo() {

    fun menu() {
        while (true) {
            println("\nMenú")
            println("1. Buscar por género.")
            println("2. Insertar juego nuevo.")
            println("3. Borrar por género.")
            println("4. Modificar juego.")
            println("5. Salir.")
            println("Introduce una opción: ")

            val opcion = readln().toInt()

            when (opcion) {
                1 -> {
                    val listaJuegos = searchByGenero(MongoConection.getCollection())
                    listaJuegos.forEach { println(it) }
                }
                2 -> insertNewGame(MongoConection.getCollection())
                3 -> deleteByGenero(MongoConection.getCollection())
                4 -> updateGame(MongoConection.getCollection())
                5 -> {
                    println("Hasta luego")
                    break
                }
            }
        }
    }

    private fun searchByGenero(collection: MongoCollection<Juego>): List<Juego> {
        var genero: String = ""

        try {
            println("Introduce el género:")
            genero = readln().lowercase()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val filtroPorGenero = Filters.eq("genero", genero)
        return collection.find(filtroPorGenero).sortedBy { it.titulo }
    }

    private fun insertNewGame(collection: MongoCollection<Juego>) {
        var titulo = ""
        var genero = ""
        var precio = 0.0
        var fecha_lanzamiento: LocalDate = LocalDate.now()
        var checkTittle = true

        try {
            do {
                println("Título:")
                titulo = readln().lowercase()
                if(checkTittleGame(collection, titulo)) {
                    checkTittle = false
                } else {
                    println("Ya existe un juego con el mismo título")
                }
            } while(checkTittle)

            println("Género:")
            genero = readln().lowercase()
            println("Precio:")
            precio = readln().toDouble()
            println("Fecha lanzamiento:")
            fecha_lanzamiento = LocalDate.parse(readln())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val juego = Juego(titulo,genero, precio, fecha_lanzamiento)

        try {
            collection.insertOne(juego)
            println("Introducido correctamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkTittleGame(collection: MongoCollection<Juego>, name: String): Boolean {
        val filtroSearchTittle = Filters.eq("titulo", name)
        val busqueda: FindIterable<Juego> = collection.find(filtroSearchTittle)

        return busqueda.count() == 0
    }

    private fun deleteByGenero(collection: MongoCollection<Juego>) {
        var genero: String = ""

        try {
            println("Introduce el género:")
            genero = readln().lowercase()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val filtroDelete = Filters.eq("genero", genero)
            collection.deleteOne(filtroDelete)
            println("Borrado correctamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateGame(collection: MongoCollection<Juego>) {
        var tittle: String = ""
        var correcto = true

        var titulo = ""
        var genero = ""
        var precio = 0.0
        var fecha_lanzamiento: LocalDate = LocalDate.now()

        try {
            do {
                println("Introduce el título:")
                tittle = readln().lowercase()

                val filtroPorTitulo = Filters.eq("titulo", tittle)
                val busqueda = collection.find(filtroPorTitulo)
                println(busqueda.first())

                println("¿Es el juego que desea modificar? (S/N):")
                val opcion = readln().lowercase()

                if (opcion == "s") {
                    correcto = false
                }
            } while (correcto)

            println("Título:")
            titulo = readln().lowercase()
            println("Género:")
            genero = readln().lowercase()
            println("Precio:")
            precio = readln().toDouble()
            println("Fecha lanzamiento:")
            fecha_lanzamiento = LocalDate.parse(readln())

            val juegoActualizado = Juego(titulo, genero, precio, fecha_lanzamiento)

            val juegoDoc = Document()
                .append("titulo", juegoActualizado.titulo)
                .append("genero", juegoActualizado.genero)
                .append("precio", juegoActualizado.precio)
                .append("fecha_lanzamiento", juegoActualizado.fecha_lanzamiento.toString())

            val filtroPorTitulo = Filters.eq("titulo", tittle)
            val updateGame = collection.updateOne(filtroPorTitulo, Document("\$set", juegoDoc))
            println("Juego actualizado: $updateGame.")
        } catch (e: Exception) {
            e.printStackTrace()
        }



    }
}