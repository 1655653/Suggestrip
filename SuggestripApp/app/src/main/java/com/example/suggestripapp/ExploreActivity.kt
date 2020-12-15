package com.example.suggestripapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Region
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_explore.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.concurrent.TimeUnit

class ExploreActivity : AppCompatActivity() {
    /*var city_name_array = arrayOf("MIAMI","NEW YORK", "PARIS","ROME","TOKYO")
    var img_cities_array = arrayOf(R.drawable.miami,R.drawable.new_york,R.drawable.paris,R.drawable.rome, R.drawable.tokyo)*/
    var city_list = mutableListOf<City>()
    var city_name_array = mutableListOf<String>()
    var img_cities_array = mutableListOf<String>()
    var popup: Dialog? = null
    var adapter:Any? = null
    var is_admin = false
//    lateinit var adapter :RecyclerViewExploreAdapter

    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)
        popup = Dialog(this)
        ShowPopup()
        is_admin = intent.getBooleanExtra("is_admin", false)
        if(!is_admin)
            addButton.visibility = GONE

         //AWS CALL, GET ALL CITIES, UI RENDERING INSIDE
        run()
        addButton.setOnClickListener{
            //TODO add new city page
            val intent = Intent(applicationContext, AdminEditCityDetailsActivity::class.java).apply {}
            intent.putExtra("city",City("null","null","Insert description",102,"null","Insert city name",null, Tags(1.0,1.0,1.0,1.0,1.0,1.0,1.0),null,null))
            intent.putExtra("is_creating",true)
            startActivity(intent)

            //awsCall("CREATE", City("null","null","this is a test description",102,"null","Ortucchio",null, Tags(1.0,1.0,1.0,1.0,2.0,3.0,4.0),null,null))
        }
    }
    fun ShowPopup() {
        popup?.setContentView(R.layout.user_popup)
        popup?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup?.show()
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
                        Log.d("DIOPORCACCIO", response_city.name)

                        response_city.img_url = "https://" + response_city.img_url
                        response_city.name = response_city.name.replace("_"," ")
                        city_list.add(response_city)
                        Log.d("porcamadonna", city_list.size.toString())
                    }
                    ///************************************************AFTER THE RESPONSE I POPULATE THE RECYCLER VIEW
                    runOnUiThread {
                        popup?.dismiss()
                        populateRV(city_list)
                    }

                }
            }
        })
    }

    private fun populateRV(cityList: MutableList<City>) {

        //save data locally
//        val outStream = FileOutputStream("citylist.dat")
//        val objectOutStream = ObjectOutputStream(outStream)
//        objectOutStream.writeInt(cityList.size) // Save size first

        for(city in cityList){
            city_name_array.add(city.name)
            img_cities_array.add(city.img_url)
            //objectOutStream.writeObject(city)
        }
        //objectOutStream.close() //close file
        //RENDERING IMAGES





        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        rv.layoutManager = layoutManager

        if(intent.getBooleanExtra("is_admin", false)){
            adapter = AdminRecyclerViewExploreAdapter(
                    city_name_array.toTypedArray(),
                    img_cities_array.toTypedArray(),
                    city_list
            )
            rv.adapter = adapter as AdminRecyclerViewExploreAdapter
            is_admin = true
        }
        else{
            adapter = RecyclerViewExploreAdapter(
                    city_name_array.toTypedArray(),
                    img_cities_array.toTypedArray(),
                    city_list
            )
            rv.adapter = adapter as RecyclerViewExploreAdapter
        }
        //Log.d("starnazzo", is_admin.toString())


        //sets divider in the list
//        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
//        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

        //Attaches adapter with RecyclerView.

    }
    override fun onActivityResult(request: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(request, resultCode, data)
        Log.d("CDA", request.toString()+"     "+resultCode.toString())

        if(request == 5){
            if(is_admin){
                (adapter as AdminRecyclerViewExploreAdapter)?.onActivityResult(request,5,data!!.getIntExtra("id_removed", 0));
            }
            else{
                (adapter as RecyclerViewExploreAdapter)?.onActivityResult(request,5,data!!.getIntExtra("id_removed", 0));
            }
        }

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