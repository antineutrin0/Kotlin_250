package com.example.kotlin_250_project.auth

class UserRepo(private val firebaseAuth: FireBaseAuth, private val mongoDBManager: MongoDBManager) {
    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        firebaseAuth.registerUser(email, password) { success, message ->
            if (success) {
                val userDocument = org.bson.Document("email", email)
                mongoDBManager.insertUser(email, userDocument)
            }
            onComplete(success, message)
        }
    }
}