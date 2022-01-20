package com.example.lukametaplayer.models

data class ChatMessage(
    val id : String = "",
    val text:String  = "",
    val fromid: String = "",
    val toid:String = "",
    val timeStamp:Long = -1
)