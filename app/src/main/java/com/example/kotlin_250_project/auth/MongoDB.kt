package com.example.kotlin_250_project.auth

import org.bson.Document
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase

class MongoDB {
    private val mongoClient: MongoClient = MongoClient("your-mongodb-uri")
    private val database: MongoDatabase = mongoClient.getDatabase("your-database-name")
    private val collection: MongoCollection<Document> = database.getCollection("users")

    fun insertUser(email: String, userData: Document) {
        collection.insertOne(userData)
    }
}