package com.example.suggestripapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_city_details.*


class CityDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_details)

        intent =  getIntent();
        val city_nm = intent.getStringExtra("city_name")
        val city_ph = intent.getIntExtra("city_photo_url",1)
        val from_shake = intent.getStringExtra("from_shake")
        if(from_shake == "false"){
            tv_city_name.text = city_nm
            iv_city_image.setImageResource(city_ph)
        }

    }
}