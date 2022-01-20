package com.example.lukametaplayer.InsideApp

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.text.Layout
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.lukametaplayer.R
import com.example.lukametaplayer.adapter.snackbar
import com.example.lukametaplayer.chat.ChatActivity
import com.example.lukametaplayer.models.User
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.card_layout.view.*
import kotlinx.android.synthetic.main.fragment_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



private lateinit var upload:Button
private lateinit var upload1:Button
private lateinit var layout_root:View
private var selectedFile: Uri? = null
private lateinit var file: Uri




class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUsers()

    }

    private fun showUsers() {
        val reference = FirebaseDatabase.getInstance().getReference("/User")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach{
                    val name = it.getValue(User::class.java)

                    if (name != null){
                        adapter.add(UserItem(name, name))
                    }

                }
                adapter.setOnItemClickListener{ item,view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatActivity::class.java)
                    intent.putExtra(SecondActivity.NAME_KEY, userItem.name)
                    startActivity(intent)

                }
                recyclerView.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    class UserItem(val name: User, val status: User) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.title.text = name.name
            viewHolder.itemView.status.text = status.status
            Glide.with(viewHolder.itemView.context).load(name.url).into(viewHolder.itemView.imageuser)
        }

        override fun getLayout(): Int {
            return R.layout.card_layout
        }
    }


}