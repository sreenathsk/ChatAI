package com.chat.ai.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chat.ai.R
import com.chat.ai.firebase.FirebaseAuthHelper
import com.chat.ai.firebase.FirebaseDbHelper
import com.chat.ai.model.User
import com.chat.ai.utils.AlertDialogHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser

class CreateAccountActivity : AppCompatActivity(), FirebaseAuthHelper.AuthResultListener {

    private lateinit var name: TextInputLayout
    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout

    private lateinit var createAccountBtn: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        initViews()

    }

    private fun initViews() {
        name = findViewById(R.id.nameEtCreate)
        email = findViewById(R.id.emailEtCreate)
        password = findViewById(R.id.passwordEtCreate)
        createAccountBtn = findViewById(R.id.createAccountBtn)

        createAccountBtn.setOnClickListener {
            if (name.editText?.text.toString().isNotEmpty() &&
                email.editText?.text.toString().isNotEmpty() &&
                password.editText?.text.toString().isNotEmpty()) {
                createNewAccount()
            } else {
                showToast("Please enter all fields")
            }
        }
    }

    private fun createNewAccount() {
        val helper = FirebaseAuthHelper(this, this)
        helper.createAccount(email.editText?.text.toString(), password.editText?.text.toString())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onAuthSuccess(user: FirebaseUser?) {
        val userData = User(user?.uid.toString(),
            name.editText?.text.toString(),
            email.editText?.text.toString(),
            )
        val db = FirebaseDbHelper()
        db.saveUser(userData)
        showToast("Account created successfully. Please login now")
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onAuthFailure(message: String) {
        AlertDialogHelper(this).showAlertDialog("Error",message)

    }
}