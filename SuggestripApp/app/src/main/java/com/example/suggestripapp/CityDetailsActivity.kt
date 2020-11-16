package com.example.suggestripapp

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
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_city_details.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.math.sqrt


class CityDetailsActivity : AppCompatActivity() {
    lateinit var city:City
    private val client = OkHttpClient()
    var popup: Dialog? = null

    //shake
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_details)

        intent =  getIntent();
        popup = Dialog(this)
        ///shake feature
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        val from_shake = intent.getBooleanExtra("from_shake", false)
        if(!from_shake){
            city = intent.extras?.get("city") as City //city came from explore
            populateLayout(city!!)

        }
        else{
            //val city came from aws call
            ShowPopup()
            var ID = (0..101).random()
            var url = "https://7ny13nqj6e.execute-api.us-east-1.amazonaws.com/default/dynamo_getter?ID="+ID.toString()
            Log.d("porcaddio", url)
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
                        city = Gson().fromJson<City>(
                                response.body!!.string(),
                                City::class.java) as City
                        city.img_url = "https://" + city.img_url
                        Log.d("porcaddio", city.toString())

                        ///************************************************AFTER THE RESPONSE I POPULATE THE RECYCLER VIEW
                        runOnUiThread {
                            popup?.dismiss()
                            populateLayout(city!!)
                        }

                    }
                }
            })

        }
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
            if (acceleration > 12) { //poi devi passargli i valori o le reference per fare uno shake randomico. E ricorda che devi fare la stessa cpsa anche dentro city activity
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
    private fun populateLayout(city: City) {

        //FILL THE ACTIVITY
        tv_city_name.text = city.name
        tv_city_description.text = city.description
        tv_costsR.text = city.tags.costs.toString()
        tv_cultureR.text = city.tags.culture.toString()
        tv_infrastructureR.text = city.tags.infrastructure.toString()
        tv_natureR.text = city.tags.nature.toString()
        tv_sportsR.text = city.tags.sports.toString()
        tv_night_lifeR.text = city.tags.night_life.toString()
        var options = RequestOptions()
                .placeholder(R.drawable.logo)
                .centerCrop()

        Glide.with(this).load(city.img_url).apply(options).into(iv_city_image)
    }

    fun ShowPopup() {
        popup?.setContentView(R.layout.user_popup)
        popup?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup?.show()
    }
}