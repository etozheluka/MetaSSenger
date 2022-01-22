package com.example.lukametaplayer.welcomeActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lukametaplayer.insideApp.EditActivity
import com.example.lukametaplayer.R
import com.example.lukametaplayer.models.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


private lateinit var email_input : TextInputLayout
private lateinit var password_input : TextInputLayout
private lateinit var password_repeat : TextInputLayout
private lateinit var email_input1 : EditText
private lateinit var password_input1 : EditText
private lateinit var password_repeat1 : EditText
private lateinit var signupBtn : Button
private lateinit var txtbtn : TextView

private val db = FirebaseDatabase.getInstance().getReference("User")
private val auth = FirebaseAuth.getInstance()

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        email_input = findViewById(R.id.email_input)
        email_input1 = findViewById(R.id.email_input1)
        password_input = findViewById(R.id.password_input)
        password_input1 = findViewById(R.id.password_input1)
        password_repeat = findViewById(R.id.password_repeat)
        password_repeat1 = findViewById(R.id.password_repeat1)
        signupBtn = findViewById(R.id.signupBtn)
        txtbtn = findViewById(R.id.txtbtn)


        txtbtn.setOnClickListener {
            login()

        }
        signupBtn.setOnClickListener {
            signupBtn()
        }
    }


    private fun signupBtn(){


        val password = password_input1.text.toString()
        val passwordRepeat = password_repeat1.text.toString()
        val email = email_input1.text.toString()




        if(email_input1.length()==0){
            email_input.error = "Please enter your email"

        }else{
            email_input.error = null
        }

        if(password_input1.length()==0){
            password_input.error = "Please enter your password"

        }else{
            password_input.error = null
        }
        if(password_repeat1.length()==0){
            password_repeat.error = "Password doesn't match"
            return
        }else{
            password_repeat.error = null
        }
        if(password != passwordRepeat){
            password_repeat.error = "Password doesn't match"

        }else{
            password_repeat.error = null

        }


        if(email_input1.length()==0){
            return

        }else{
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        login()
                        getid()
                    }else{
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    private fun getid() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val userInfo = User(uid,"","","")
        db.child(auth.currentUser?.uid!!)
            .setValue(userInfo)
    }

    private fun login() {
        startActivity(Intent(this, EditActivity::class.java))
        finish()
    }

}