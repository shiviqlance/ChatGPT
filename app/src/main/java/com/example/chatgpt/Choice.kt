package com.example.chatgpt

data class Choice(
    val finish_reason: String,
    val index: String,
    val logprobs: String,
    val text: String
)