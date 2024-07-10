package com.chat.ai.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chat.ai.activity.LoginActivity
import com.chat.ai.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthHelper(private val context: Context, private val listener: AuthResultListener) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Sign in with email and password
    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    listener.onAuthSuccess(user)
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Account does not exist."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                        else -> "Authentication failed."
                    }
                    listener.onAuthFailure(errorMessage)
                }
            }
    }

    // Create a new account with email and password
    fun createAccount(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    listener.onAuthSuccess(user)
                } else {
                    listener.onAuthFailure("Account creation failed: ${task.exception?.message}")
                }
            }
    }

    // Sign out
    fun signOut() {
        mAuth.signOut()
        Toast.makeText(context, "Signed out successfully.", Toast.LENGTH_SHORT).show()
        // Navigate to the login activity
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    // Get currently signed-in user
    fun getCurrentUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    interface AuthResultListener {
        fun onAuthSuccess(user: FirebaseUser?)
        fun onAuthFailure(message: String)
    }

}
