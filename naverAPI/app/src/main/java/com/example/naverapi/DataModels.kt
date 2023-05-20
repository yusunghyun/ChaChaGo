package com.example.myapplication

data class MyPost(val model: String, val messages: List<Message>)
data class Message(val role: String, val content: String)
data class MyResponse(
    val id: String,
    val object2: String,
    val created: Int,
    val model: String,
    val choices: List<Choice>
)
data class Choice(
    val message: Message,
    val finish_reason: String,
    val index: Int
)
