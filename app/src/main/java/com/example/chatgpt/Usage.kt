package com.example.chatgpt

data class Usage(
    val completion_tokens: String,
    val prompt_tokens: String,
    val total_tokens: String
)