package com.example.suggestripapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class RankActivity : AppCompatActivity() {

    var city_list = mutableListOf<RankedCity>()
    private var listV:ListView ?=null
    private var rankedCityAdapter: RankedCityAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        loadCities()
        listV = findViewById(R.id.listview_card)
        rankedCityAdapter = RankedCityAdapter(applicationContext, city_list)
        listV?.adapter  = rankedCityAdapter



    }

    private fun loadCities() {
        var intent = intent;
        val body = intent.getStringExtra("body_string")
        //Log.d("algo", body)
        var cities = Gson().fromJson<Object>(
            body,
            Object::class.java
        ) as List<*>


        for (c in cities) {
            var x = Gson().toJson(c)
            var response_city = Gson().fromJson<RankedCity>(x, RankedCity::class.java)
            response_city.img_url = "https://" + response_city.img_url
            response_city.name = response_city.name.replace("_"," ")
            city_list.add(response_city)


        }


    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {}
        intent.putExtra("back_from_ranked", true)
        startActivity(intent)
        super.onBackPressed()


    }

}