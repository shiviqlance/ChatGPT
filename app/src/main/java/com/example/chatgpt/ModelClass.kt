package com.example.chatgpt

data class ModelClass(
    val choices: List<Choice>,
    val created: String,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)