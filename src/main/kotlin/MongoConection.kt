package org.example

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object MongoConection {

    private val dotenv: Dotenv = dotenv()
    private val urlConnectionMongo = dotenv["URL_MONGODB"] ?: throw Exception()
    private val cluster: MongoClient = MongoClients.create(urlConnectionMongo)

    private fun getConectionDB(): MongoDatabase {
        return cluster.getDatabase("Sebastian")
    }

    fun getCollection(): MongoCollection<Juego> {
        val bd = getConectionDB()
        return bd.getCollection("juegos", Juego::class.java)
    }
}