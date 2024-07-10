package com.chat.ai.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chat.ai.R
import com.chat.ai.firebase.FirebaseAuthHelper
import com.chat.ai.utils.AlertDialogHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity(), FirebaseAuthHelper.AuthResultListener {

    private lateinit var email:TextInputLayout
    private lateinit var password:TextInputLayout

    private lateinit var signInBtn:MaterialButton

    private lateinit var createTv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        initViews()



    }

    private fun initViews() {
        email = findViewById(R.id.emailEtLogin)
        password = findViewById(R.id.passwordEtLogin)
        signInBtn = findViewById(R.id.signinBtn)
        createTv = findViewById(R.id.createAccountBtn)


        signInBtn.setOnClickListener {
            if (email.editText?.text.toString().isNotEmpty() &&
                password.editText?.text.toString().isNotEmpty()){
                validateUser()
            }else{
                showToast("Please enter all fields")
            }
        }

        createTv.setOnClickListener {
            startActivity(Intent(this,CreateAccountActivity::class.java))
        }
    }

    private fun validateUser() {
        val helper = FirebaseAuthHelper(this, this)
        helper.signIn(email.editText?.text.toString(),password.editText?.text.toString())
    }

    private fun showToast(s:String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }

    override fun onAuthSuccess(user: FirebaseUser?) {
        Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ChatActivity::class.java))
    }

    override fun onAuthFailure(message: String) {
        AlertDialogHelper(this).showAlertDialog("Error",message)
    }
}