package com.example.lukametaplayer.insideApp

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lukametaplayer.R
import com.example.lukametaplayer.chat.ChatActivity
import com.example.lukametaplayer.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.card_layout.view.*
import kotlinx.android.synthetic.main.fragment_main.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"








class MainFragment : Fragment() {

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