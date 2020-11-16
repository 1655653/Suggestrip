package com.example.suggestripapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_city_details.*
import kotlinx.android.synthetic.main.row_layout.view.*
import okhttp3.*
import java.io.IOException


class CityDetailsActivity : AppCompatActivity() {
    lateinit var city:City
    private val client = OkHttpClient()
    var popup: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_details)

        intent =  getIntent();
        popup = Dialog(this)

        val from_shake = intent.getBooleanExtra("from_shake",false)
        if(!from_shake){
            city = intent.extras?.get("city") as City //city came from explore
            populateLayout(city!!)

        }
        else{
            //val city came from aws call
            ShowPopup()
            var ID = (0..101).random()
            var url = "https://7ny13nqj6e.execute-api.us-east-1.amazonaws.com/default/dynamo_getter?ID="+ID.toString()
            Log.d("porcaddio",url)
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
                                City::class.java ) as City
                        city.img_url = "https://"+ city.img_url
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