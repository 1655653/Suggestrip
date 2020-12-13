package com.example.suggestripapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        //TEST per la create, vedere payload dentro cloudscripts per update e delete. aggiungere form visivo
        awsCall("CREATE", City("null","null","this is a test description",101,"null","Ortucchio",null, Tags(1.0,1.0,1.0,1.0,2.0,3.0,4.0),null,null))
    }


    private fun awsCall(crudOp : String, newCity : City) {
        var url = "https://hzqjewxyhe.execute-api.us-east-1.amazonaws.com/default/CRUD_operations"
        val payload = "{\n" +
                "    \"msg_id\": \"$crudOp\",\n" +
                "    \"city\":{\n" +
                "        \"description\": \"${newCity.description}\",\n" +
                "        \"name\": \"${newCity.name}\",\n" +
                "        \"tags\": {\n" +
                "            \"costs\": ${newCity.tags!!.costs.toString()},\n" +
                "            \"night_life\":${newCity.tags!!.night_life.toString()},\n" +
                "            \"sports\": ${newCity.tags!!.sports.toString()},\n" +
                "            \"nature\": ${newCity.tags!!.nature.toString()},\n" +
                "            \"culture\": ${newCity.tags!!.culture.toString()},\n" +
                "            \"infrastructure\": ${newCity.tags!!.infrastructure.toString()},\n" +
                "            \"food\": ${newCity.tags!!.food.toString()}\n" +
                "        }\n" +
                "    }\n" +
                "}"
        Log.d("DIOMAYALE",payload)
        val okHttpClient = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()
        val requestBody = payload.toRequestBody()
        val request = Request.Builder()
                .method("POST", requestBody)
                .url(url)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful)
                    throw IOException("Unexpected code $response")

                Log.d("DIOMAYALEE", "body: " + response.body!!.string())
                val intent = Intent(applicationContext, MainActivity::class.java).apply {}
                startActivity(intent)
            }
        })
    }
}