package com.example.suggestripapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.suggestripapp.fav.FavDB
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_city_details.*
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import kotlin.math.sqrt


class CityDetailsActivity : AppCompatActivity() {
    lateinit var city:City
    private val client = OkHttpClient()
    var from_shake = false
    var from_rv = false
    var from_algo = false
    var popup: Dialog? = null
    var id_removed = 0
///TODO WEATHER AND COVID VISUALIZATION
    //shake
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    var url = "https://7ny13nqj6e.execute-api.us-east-1.amazonaws.com/default/dynamo_getter?ID="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_details)

        intent = getIntent();
        popup = Dialog(this)
        ///shake feature
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        from_shake = intent.getBooleanExtra("from_shake", false)
        from_rv = intent.getBooleanExtra("from_rv", false)
        from_algo = intent.getBooleanExtra("from_algo", false)

        //choose id, random if shake, selected if explored
        var ID = ""

        if(from_shake){
            ID = (0..101).random().toString()
        }
        else if (from_algo){
           var r_city = intent.extras?.get("city") as RankedCity
            ID = r_city.ID.toString()
            Log.d("algo", ID + " ecccoooollloooooo")
        }
        else{
            city = intent.extras?.get("city") as City
            ID = city.ID.toString()
        }

        //aws call
        url += ID
        ShowPopup()
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
                    val gson = Gson()
                    val type: Type = object : TypeToken<City?>() {}.type
                    city = gson.fromJson(response.body!!.string(), type)

//                    val obj = JSONObject(response.body!!.string())
//                    city = Gson().fromJson<City>(
//                            obj.toString(),
//                            City::class.java) as City
                    city.img_url = "https://" + city.img_url
                    Log.d("porcaddio", city.toString())

                    ///************************************************AFTER THE RESPONSE I POPULATE THE RECYCLER VIEW
                    runOnUiThread {
                        popup?.dismiss()
                        populateLayout(city!!)

                        //favourite detection
                        iv_preferred.setOnClickListener {

                            var db = FavDB(applicationContext)
                            managePreferred(db, city)
                        }
                    }
                }
            }
        })
    }

    private fun managePreferred(db: FavDB, city: City) {
        val query = "SELECT * FROM " + FavDB.TABLE_NAME + " WHERE " + FavDB.ID + " = ${city.ID.toString()} "
        var cursor = db.readableDatabase.rawQuery(query, null, null)
        //se esiste lo cancello
        if (cursor.moveToFirst()){
            iv_preferred.setImageResource(R.drawable.ic_favorite_shadow_24dp)
            db.remove_fav(city.ID.toString())
            Toast.makeText(applicationContext, "${city.name} removed from the favourites", Toast.LENGTH_SHORT).show()

            id_removed = city.ID
        }
        //se non esiste lo aaggiungo
        else{
            iv_preferred.setImageResource(R.drawable.ic_favorite_red_24dp)
            db.insertIntoTheDatabase(city.name, city.img_url, city.ID.toString(), "1")
            Toast.makeText(applicationContext, "${city.name} added to the favourites!", Toast.LENGTH_SHORT).show()
            id_removed = 0
        }


    }

    override fun onDestroy() {
        popup?.dismiss()
        super.onDestroy()
    }
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                val intent = getIntent()
                intent.putExtra("from_shake", true)
                finish()
                startActivity(intent)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }
    @SuppressLint("SetTextI18n")
    private fun populateLayout(city: City) {


        //FILL THE ACTIVITY
        tv_city_name.text = city.name
        tv_city_description.text = city.description

        //tv_costs.text = "Costs: " + city.tags?.costs.toString()
        var n = city.tags?.costs?.toInt()
        for (i in n!!..2){
            var c = (i+1).toString()
            Log.d("provaaa",c +" n = "+ n.toString())
            findViewById<ImageView>(resources.getIdentifier("dollar$c", "id", packageName)).visibility = GONE

        }
        populateIcons("greek", city.tags?.culture?.toInt())
        populateIcons("car", city.tags?.infrastructure?.toInt())
        populateIcons("ball", city.tags?.sports?.toInt())
        populateIcons("glass", city.tags?.night_life?.toInt())
        populateIcons("pizza", city.tags?.food?.toInt())
        populateIcons("tree", city.tags?.nature?.toInt())


        if (city.covid_info?.case_per_million!! >0)
            tv_case_per_million.text = "Cases x 1mil pop: " + city.covid_info?.case_per_million!!.toInt().toString()
        else
            tv_case_per_million.visibility = GONE
        if (city.covid_info?.avg_confirmed!! >0)
            tv_avg_confirmed.text = "Avg confirmed: " + city.covid_info?.avg_confirmed!!.toInt().toString()
        else
            tv_avg_confirmed.visibility = GONE
        if (city.covid_info?.avg_deaths!! >0)
            tv_avg_deaths.text = "Avg deaths: " + city.covid_info?.avg_deaths!!.toInt().toString()
        else
            tv_avg_deaths.visibility = GONE
        if (city.covid_info?.avg_recovered!! >0)
            tv_avg_recovered.text = "Avg recovered: " + city.covid_info?.avg_recovered!!.toInt().toString()
        else
            tv_avg_recovered.visibility = GONE




        var xxx = "https://openweathermap.org/img/wn/"+ city.weather?.icon.toString()+ "@2x.png"
        var tv_weather_id = city.weather?.id.toString()
//        tv_weather_main.text = city.weather?.main.toString()
        tv_weather_description.text = city.weather?.description.toString()
        //tv_weather_icon.text = city.weather?.icon.toString()
        tv_weather_temperature.text = "Temperature: "+ city.weather?.temperature?.toInt().toString()+"°"
        Log.d("xxx", xxx)
        var options = RequestOptions()
                .placeholder(R.drawable.logo)
                .centerCrop()
        Glide.with(applicationContext).load(xxx).apply(options).into(tv_weather_icon)

        Glide.with(applicationContext).load(city.img_url).apply(options).into(iv_city_image)

        ///SET HEART IMAGE COLOR
        var sql = FavDB(applicationContext)
        val query = "SELECT * FROM " + FavDB.TABLE_NAME + " WHERE " + FavDB.ID + " = ${city.ID.toString()} "
        Log.d("dblog", query)
        var cursor = sql.readableDatabase.rawQuery(query, null, null)
        if (cursor.moveToFirst()) {
            val favourite = cursor.getString(3)
            Log.d("dblog", favourite)
            if(favourite == "1"){
                iv_preferred.setImageResource(R.drawable.ic_favorite_red_24dp)
            }
            else{
                iv_preferred.setImageResource(R.drawable.ic_favorite_shadow_24dp)
            }
        }
    }

    fun populateIcons(icon:String, n: Int?) {
        for (i in n!!..4){
            var c = (i+1).toString()
            Log.d("provaaa", "$icon$c n = $n")
            findViewById<ImageView>(resources.getIdentifier(icon+c, "id", packageName)).visibility = GONE
        }

    }


    override fun onBackPressed() {
        if(from_shake){
            Log.d("CDA", "onBackPressed Called")
            val intent = Intent(this, MainActivity::class.java).apply {}
            intent.putExtra("back_from_shake", true)
            startActivity(intent)
        }
        else if (from_rv){
            Log.d("CDA", "FROM RV BACK")
            val intent = Intent()
            intent.putExtra("id", "1")
            intent.putExtra("id_removed", id_removed)
            intent.putExtra("description", "sendback description from 2nd activity")
            setResult(RESULT_OK, intent)
            super.onBackPressed()

        }
        else{
            super.onBackPressed()
        }

    }
    fun ShowPopup() {
        popup?.setContentView(R.layout.user_popup)
        popup?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup?.show()
    }
}