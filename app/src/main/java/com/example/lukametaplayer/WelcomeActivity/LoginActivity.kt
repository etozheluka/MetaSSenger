package com.example.lukametaplayer.WelcomeActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.lukametaplayer.InsideApp.SecondActivity
import com.example.lukametaplayer.MainActivity
import com.example.lukametaplayer.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


private lateinit var email_login : TextInputLayout
private lateinit var password_login : TextInputLayout
private lateinit var email_login1 : EditText
private lateinit var password_login1 : EditText
private lateinit var loginBtn : Button
private lateinit var txtbtn2 : TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        email_login = findViewById(R.id.email_login)
        email_login1 = findViewById(R.id.email_login1)
        password_login = findViewById(R.id.password_login)
        password_login1 = findViewById(R.id.password_login1)
        loginBtn = findViewById(R.id.loginBtn)
        txtbtn2 = findViewById(R.id.txtbtn2)

        loginBtn.setOnClickListener{
            loginBtn()
        }
        txtbtn2.setOnClickListener {
            register()
        }

    }


    private fun loginBtn(){

        val password = password_login1.text.toString()
        val email = email_login1.text.toString()


        if(email_login1.length()==0){
            email_login.error = "Please enter your email"

        }else{
            email_login.error = null
        }

        if(password_login1.length()==0){
            password_login.error = "Please enter your password"
            return@loginBtn

        }else{
            password_login.error = null
        }

        if(email_login1.length()==0){
            return@loginBtn

        }else{
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                        login()
                    }else{
                        email_login.error = "Email and Password do not match"
                    }
                }
        }

    }
    private fun login(){
        startActivity(Intent(this,SecondActivity::class.java))
        finish()

    }
    private fun register(){
        startActivity(Intent(this, RegisterActivity::class.java))

    }
}







