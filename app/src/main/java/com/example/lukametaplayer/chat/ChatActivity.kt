package com.example.lukametaplayer.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lukametaplayer.InsideApp.SecondActivity
import com.example.lukametaplayer.R
import com.example.lukametaplayer.models.ChatMessage
import com.example.lukametaplayer.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*
import kotlinx.android.synthetic.main.fragment_main.*

var toUser : User? = null


class ChatActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chat_recycler.adapter = adapter

        toUser = intent.getParcelableExtra<User>(SecondActivity.NAME_KEY)
        supportActionBar?.title = toUser?.name

        chat_recycler.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
                reverseLayout = false
            }
        }



        showMessage()


        send_chat_btn.setOnClickListener {
            sendmessage()
        }


    }

    private fun showMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {

                    if (chatMessage.fromid == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatToItem(chatMessage.text))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text,toUser!!))
                    }
                }


            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun sendmessage() {

        val text = chat_textfield.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(SecondActivity.NAME_KEY)
        val toId = user?.uid
        if(fromId == null)return



        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val reference2 = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatmessage = ChatMessage(reference.key!!,text,fromId,toId.toString(),System.currentTimeMillis() / 1000)
        reference.setValue(chatmessage)
            .addOnSuccessListener {
                chat_textfield.text?.clear()
                chat_recycler.apply {
                    layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                        stackFromEnd = true
                        reverseLayout = false
                    }
                }
            }
        reference2.setValue(chatmessage)



    }

}


class ChatFromItem(val text:String, val user:User?): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_from_message.text = text

        val uri = user?.url
        val photo = viewHolder.itemView.chat_image_from

        Glide.with(viewHolder.itemView.context).load(uri).into(photo)
    }
    override fun getLayout(): Int {
        return R.layout.chat_from
    }

}
class ChatToItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_to_message.text = text

//        val uri = user?.url
//        val photo = viewHolder.itemView.chat_image_to
//        Picasso.get().load(uri).into(photo)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to
    }

}

