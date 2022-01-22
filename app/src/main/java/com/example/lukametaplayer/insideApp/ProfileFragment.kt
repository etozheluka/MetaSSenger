package com.example.lukametaplayer.insideApp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lukametaplayer.MainActivity
import com.example.lukametaplayer.R
import com.example.lukametaplayer.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



private lateinit var txtProfile : TextView
private lateinit var txtProfile2 : TextView
private val db = FirebaseDatabase.getInstance().getReference("User")
private val auth = FirebaseAuth.getInstance()


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtProfile = view.findViewById(R.id.txt_profile)
        txtProfile2 = view.findViewById(R.id.txt_profile2)

        //TODO POFIKSIT CRASH PRI SMENE FRAGMENTA

        db.child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfo = snapshot.getValue(User::class.java) ?:return
                txtProfile.text = userInfo.name
                txt_profile2.text = userInfo.status
                Glide.with(this@ProfileFragment)
                    .load(userInfo.url).placeholder(R.drawable.profile).into(imageProfile)
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })



        logOutBtn.setOnClickListener{
            MaterialAlertDialogBuilder(view.context)
                .setTitle("Log Out")
                .setMessage(resources.getString(R.string.supporting_text))

                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->

                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    logout()

                }

                .show()


        }

        edit.setOnClickListener {
            startActivity(Intent(context, EditActivity::class.java))
        }
        edit2.setOnClickListener {
            dialog()
        }

    }

    private fun changepassword() {
        FirebaseAuth
            .getInstance()
            .sendPasswordResetEmail(FirebaseAuth.getInstance().currentUser?.email.toString())
            .addOnCompleteListener {function ->
                if (function.isSuccessful){
                    Toast.makeText(context, "Check Your Email", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }

            }
    }


    private fun logout(){
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()

    }


    private fun dialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title))
            .setMessage(resources.getString(R.string.supporting_text))
            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                changepassword()
            }
            .show()

    }


}