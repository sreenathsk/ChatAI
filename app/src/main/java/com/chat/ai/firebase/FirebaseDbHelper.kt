package com.chat.ai.firebase

import com.chat.ai.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDbHelper {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersReference: DatabaseReference = database.getReference("users")

    // Save user to the database
    fun saveUser(user: User) {
        val userId = user.uid
        usersReference.child(userId).setValue(user)
            .addOnCompleteListener { task ->
            }
    }

    // Retrieve user from the database
    fun getUser(userId: String, callback: (User?) -> Unit) {
        usersReference.child(userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.getValue(User::class.java)
                callback(user)
            } else {
                callback(null)
            }
        }
    }

    // Update user in the database
    fun updateUser(user: User) {
        val userId = user.uid
        usersReference.child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {
                }
            }
    }

    // Delete user from the database
    fun deleteUser(userId: String) {
        usersReference.child(userId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {
                }
            }
    }
}
