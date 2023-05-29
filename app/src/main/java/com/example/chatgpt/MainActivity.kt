package com.example.chatgpt

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val Datalist = ArrayList<DataList>()
private lateinit var adapter: MyAdapter
private lateinit var promp: TextView
private lateinit var progBar: ProgressBar

class MainActivity : AppCompatActivity() {

    //var progBar = findViewById<ProgressBar>(R.id.progressBar)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        promp = findViewById(R.id.prompt)
        Log.e("prompt:", promp.toString())

        progBar = findViewById(R.id.progressBar)
        progBar.visibility = View.GONE

        var listView = findViewById<ListView>(R.id.list_item_layout)
        adapter = MyAdapter(Datalist, this)
        listView.adapter = adapter

        var btn = findViewById<Button>(R.id.ctnue)
        btn.setOnClickListener(View.OnClickListener {
            Datalist.clear()
            getdata()
            val view: View? = this.currentFocus
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            // on below line hiding our keyboard.
            inputMethodManager.hideSoftInputFromWindow(view?.getWindowToken(), 0)
        })



        adapter.notifyDataSetChanged()
    }

    private fun getdata() {

        progBar.visibility = View.VISIBLE
        val jsonObject = JsonObject()
        jsonObject.addProperty("prompt", promp.text.toString())
        Log.e("prompt:", promp.toString())
        jsonObject.addProperty("max_tokens", 400)
        jsonObject.addProperty("model", "text-davinci-003")



        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer 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")
                    .build()

                chain.proceed(request)
            }
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val ApiClient = retrofit.create(Api::class.java)


        //val apiCli = ApiClient.getData()

        ApiClient.getData(jsonObject).enqueue(object : retrofit2.Callback<ModelClass> {
            override fun onResponse(call: Call<ModelClass>, response: Response<ModelClass>) {
//                var completion = response.body()
//                Log.d("API_RESPONSE", completion?.choices?.get(0)?.text.toString())
//                var Text =  completion?.choices?.get(0)?.text.toString()
//
//                var newList = DataList(Text)
//
//                Datalist.add(newList)
//                Log.d("::::::::",Text)
//                Log.d("API_RESPONSE::::::::",Datalist.size.toString())
//                Log.d("API_RESPONSE::::::::",newList.toString())

                val completion = response.body()
                val choices = completion?.choices
                if (choices != null) {
                    for (choice in choices) {
                        Log.d("API_RESPONSE", choice.text)
                        val newText = choice.text
                        val newList = DataList(newText)
                        Datalist.add(newList)
                    }
                }

                Log.d("API_RESPONSE", Datalist.size.toString())
                //Log.d("API_RESPONSE", newList.toString())

                progBar.visibility = View.GONE
                adapter.notifyDataSetChanged()

               // adapter.notifyDataSetChanged()

            }
            override fun onFailure(call: Call<ModelClass>, t: Throwable) {
                // Hide the progress bar when the data fetch fails
                progBar.visibility = View.GONE

                // Handle the failure
            }
        })



    }
}