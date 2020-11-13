package com.example.suggestripapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_explore.*
import okhttp3.*
import java.io.IOException

class ExploreActivity : AppCompatActivity() {
    /*var city_name_array = arrayOf("MIAMI","NEW YORK", "PARIS","ROME","TOKYO")
    var img_cities_array = arrayOf(R.drawable.miami,R.drawable.new_york,R.drawable.paris,R.drawable.rome, R.drawable.tokyo)*/
    var city_list = mutableListOf<City>()
    var city_name_array = mutableListOf<String>()
    var img_cities_array = mutableListOf<String>()

    lateinit var adapter :RecyclerViewExploreAdapter

    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)
        //AWS CALL, GET ALL CITIES, UI RENDERING INSIDE
        run()

    }

    private fun run() {
        Log.d("porcamadonna", "START")
        var url = "https://tx1hnkw84a.execute-api.us-east-1.amazonaws.com/default/dynamo_getter_all"
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("porcamadonna", "ERORE")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var city = Gson().fromJson<Object>(
                        response.body!!.string(),
                        Object::class.java
                    )
                    var lc = city as List<*>
                    //Log.d("porcamadonna", lc[0].toString())
                    for (c in lc) {
                        var x = Gson().toJson(c)
                        var response_city = Gson().fromJson<City>(x, City::class.java)
                        city_list.add(response_city)
                        Log.d("porcamadonna", city_list.size.toString())
                    }
                    ///************************************************AFTER THE RESPONSE I POPULATE THE RECYCLER VIEW
                    runOnUiThread {
                        populateRV(city_list)
                    }

                }
            }
        })
    }

    private fun populateRV(cityList: MutableList<City>) {

        for(city in cityList){
            city_name_array.add(city.name)
            img_cities_array.add(city.img_url)
        }
        //RENDERING IMAGES
        adapter = RecyclerViewExploreAdapter(
            city_name_array.toTypedArray(),
            img_cities_array.toTypedArray()
        )

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        rv.layoutManager = layoutManager
        //sets divider in the list
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

        //Attaches adapter with RecyclerView.
        rv.adapter = adapter
    }

}