package com.chat.ai.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.chat.ai.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spl_screen)


        val auth = Firebase.auth
        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser == null)
                startActivity(Intent(this@SplScreenActivity, LoginActivity::class.java))
            else
                startActivity(Intent(this@SplScreenActivity, ChatActivity::class.java))

        }, 2000)

    }
}