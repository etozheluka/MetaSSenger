package com.example.lukametaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.Menu
import android.view.View
import com.example.lukametaplayer.InsideApp.SecondActivity
import com.example.lukametaplayer.WelcomeActivity.LoginActivity
import com.example.lukametaplayer.WelcomeActivity.RegisterActivity
import com.google.firebase.auth.FirebaseAuth


private lateinit var loginBtn :Button
private lateinit var registerBtn : Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(FirebaseAuth.getInstance().currentUser !=null){
            gotoprofile()
        }


        loginBtn = findViewById(R.id.loginBtn)
        registerBtn = findViewById(R.id.registerBtn)


        loginBtn.setOnClickListener {
                val login = Intent(baseContext, LoginActivity::class.java)
                startActivity(login)
        }

        registerBtn.setOnClickListener {
            val login = Intent(baseContext, RegisterActivity::class.java)
            startActivity(login)
        }
    }
    private fun gotoprofile(){
        startActivity(Intent(this, SecondActivity::class.java))
        finish()
    }


}