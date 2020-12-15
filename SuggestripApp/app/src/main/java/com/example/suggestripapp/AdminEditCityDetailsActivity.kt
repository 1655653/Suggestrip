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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_admin_edit_city_details.*
import kotlinx.android.synthetic.main.activity_city_details.tv_city_description
import kotlinx.android.synthetic.main.activity_city_details.tv_city_name
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.sqrt


class AdminEditCityDetailsActivity : AppCompatActivity() {
    lateinit var city: City
    private val client = OkHttpClient()
    var from_shake = false
    var from_rv = false
    var from_algo = false
    var is_creating = false
    var popup: Dialog? = null
    var id_removed = 0

    //shake
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    var url = "https://7ny13nqj6e.execute-api.us-east-1.amazonaws.com/default/dynamo_getter?ID="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_city_details)

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
        is_creating = intent.getBooleanExtra("is_creating", false)

        //choose id, random if shake, selected if explored
        var ID = ""

        if (from_shake) {
            ID = (0..101).random().toString()
        } else if (from_algo) {
            var r_city = intent.extras?.get("city") as RankedCity
            ID = r_city.ID.toString()
            Log.d("algo", ID + " ecccoooollloooooo")
        } else {
            city = intent.extras?.get("city") as City
            ID = city.ID.toString()
        }

        if (is_creating)
            delete_button.visibility = GONE

        //ADMIN BUTTONS

        discard_button.setOnClickListener {
            var intent = Intent(this, CityDetailsActivity::class.java).apply {}
            intent.putExtra("city", city)
            intent.putExtra("is_admin", true)
            if(is_creating)
                intent = Intent(this, ExploreActivity::class.java).apply {}

            startActivity(intent)
        }

        confirm_button.setOnClickListener {
            //TODO controllare se la descriptino della city viene agggiornata dalla textbox
            //PS NON CREDO, va parsata dalla textbox
            //PPS credo di averlo fatto, ma va fatto anche per i tag
            var crudOp = "UPDATE"

            var intent = Intent(this, CityDetailsActivity::class.java).apply {}
            intent.putExtra("city", city)
            if (is_creating) {
                crudOp = "CREATE"
                intent = Intent(this, ExploreActivity::class.java).apply {}
                intent.putExtra("is_admin", true)
            }
            city.name = tv_city_name.text.toString()
            city.description = tv_city_description.text.toString()
            // TODO per ogni tag prendere i valori attuali
            city.tags!!.infrastructure = 1.0

            adminCrudAws(crudOp, city)

            startActivity(intent)
        }

        delete_button.setOnClickListener {
            adminCrudAws("DELETE", city)
            //TODO back to explore fatto bene
            // MAIN --> EXPLORE --> CITY_DETAILS --> ADMIN_EDIT --> remove --> EXPLORE
            val intent = Intent(this, ExploreActivity::class.java).apply {}
            intent.putExtra("is_admin", true)
            startActivity(intent)
        }
        //aws call
        if (!is_creating) {
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
                        city.name = city.name.replace("_", " ")
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
        for (i in n!!..2) {
            var c = (i + 1).toString()
            Log.d("provaaa", c + " n = " + n.toString())
            findViewById<ImageView>(resources.getIdentifier("dollar" + c, "id", packageName)).setImageResource(R.drawable.empty_cost)


        }
        for (n in 1..3) {
            var iv = findViewById<ImageView>(resources.getIdentifier("dollar" + n, "id", packageName))
            iv.setOnClickListener {
                for (over in n..3) {
                    findViewById<ImageView>(resources.getIdentifier("dollar" + over, "id", packageName)).setImageResource(resources.getIdentifier("empty_" + "cost", "drawable", packageName))
                }
                for (sub in 1..n) {
                    findViewById<ImageView>(resources.getIdentifier("dollar" + sub, "id", packageName)).setImageResource(resources.getIdentifier("filled_" + "cost", "drawable", packageName))
                }
                Log.d("diomaialino", n.toString())
                
            }
        }


        populateIcons("greek", city.tags?.culture?.toInt())
        populateIcons("car", city.tags?.infrastructure?.toInt())
        populateIcons("ball", city.tags?.sports?.toInt())
        populateIcons("glass", city.tags?.night_life?.toInt())
        populateIcons("pizza", city.tags?.food?.toInt())
        populateIcons("tree", city.tags?.nature?.toInt())


    }

    fun populateIcons(icon: String, n: Int?) {
        for (i in n!!..4) {
            var c = (i + 1).toString()
            Log.d("provaaa", "$icon$c n = $n")
            //R.drawable.filled_cost
//            findViewById<ImageView>(resources.getIdentifier(icon + c, "id", packageName)).visibility = GONE
            findViewById<ImageView>(resources.getIdentifier(icon + c, "id", packageName)).setImageResource(resources.getIdentifier("empty_"+icon, "drawable", packageName))
        }
        for(n in 1..5){
            var iv = findViewById<ImageView>(resources.getIdentifier(icon + n, "id", packageName))
            iv.setOnClickListener {
                for (over in n..5){
                    findViewById<ImageView>(resources.getIdentifier(icon + over, "id", packageName)).setImageResource(resources.getIdentifier("empty_"+icon, "drawable", packageName))
                }
                for (sub in 1..n){
                    findViewById<ImageView>(resources.getIdentifier(icon + sub, "id", packageName)).setImageResource(resources.getIdentifier("filled_"+icon, "drawable", packageName))
                }
                Log.d("diomaialino", n.toString())
            }
        }

    }


    override fun onBackPressed() {
        if (from_shake) {
            Log.d("CDA", "onBackPressed Called")
            val intent = Intent(this, MainActivity::class.java).apply {}
            intent.putExtra("back_from_shake", true)
            startActivity(intent)
        } else if (from_rv) {
            Log.d("CDA", "FROM RV BACK")
            val intent = Intent()
            intent.putExtra("id", "1")
            intent.putExtra("id_removed", id_removed)
            intent.putExtra("description", "sendback description from 2nd activity")
            setResult(RESULT_OK, intent)
            super.onBackPressed()

        } else {
            super.onBackPressed()
        }

    }

    fun ShowPopup() {
        popup?.setContentView(R.layout.user_popup)
        popup?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup?.show()
    }


    private fun adminCrudAws(crudOp: String, newCity: City) {
        var url = "https://hzqjewxyhe.execute-api.us-east-1.amazonaws.com/default/CRUD_operations"
        var payload = "{\n" +
                "    \"msg_id\": \"$crudOp\",\n" +
                "    \"city_id\": \"${newCity.ID}\",\n" +
                "    \"city\":{\n" +
                "        \"description\": \"${newCity.description}\",\n" +
                "        \"name\": \"${newCity.name}\",\n" +
                "        \"brief_description\": \"${newCity.brief_description}\",\n" +
                "        \"img_url\": \"${newCity.img_url}\",\n" +
                "        \"ID\": \"${newCity.ID.toString()}\",\n" +
                "        \"country\": \"${newCity.country}\",\n" +
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
        Log.d("DIOMAYALE", payload)
        ShowPopup()
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
                runOnUiThread { popup?.dismiss() }
            }
        })
    }
}