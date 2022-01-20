package com.example.lukametaplayer.InsideApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.lukametaplayer.R
import com.example.lukametaplayer.models.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit.*


private val db = FirebaseDatabase.getInstance().getReference("User")
private val auth = FirebaseAuth.getInstance()

private lateinit var status1 : EditText
private lateinit var url1 : EditText
private lateinit var name1 : EditText
private lateinit var status : TextInputLayout
private lateinit var url : TextInputLayout
private lateinit var name : TextInputLayout

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        status1 = findViewById(R.id.editstatus1)
        status = findViewById(R.id.editstatus)
        url1 = findViewById(R.id.editurl1)
        url = findViewById(R.id.editurl)
        name1 = findViewById(R.id.username1)
        name = findViewById(R.id.username)

//TODO SDELAT AUTO IMIA STATUS I URL
        editsubmit.setOnClickListener {



            val name = name1.text.toString()
            val status = status1.text.toString()
            val url = url1.text.toString()
            val uid = FirebaseAuth.getInstance().uid ?: ""

            val userInfo = User(uid,name,status,url)
            db.child(auth.currentUser?.uid!!)
                .setValue(userInfo)

            finish()


        }

    }

}