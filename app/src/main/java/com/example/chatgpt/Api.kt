package com.example.chatgpt
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface Api{
        @Headers("Authorization: Bearer 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")
        @POST("completions")
        fun getData(@Body josnobj: JsonObject): Call<ModelClass>



}