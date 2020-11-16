package com.example.suggestripapp

/*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}*/

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.*
import kotlin.math.sqrt


const val EXTRA_MESSAGE = "com.example.suggestrip.MESSAGE"

class MainActivity : AppCompatActivity(){
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    var popup: Dialog? = null
    //firebase authUI
    lateinit var providers: List<AuthUI.IdpConfig>
    var options = RequestOptions()
        .placeholder(R.drawable.user_small)
        .centerCrop()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser
        if(user != null && intent.getBooleanExtra("is_user_modified",false)){
            if(intent.getBooleanExtra("is_uri_propic_modified",false))
                Glide.with(applicationContext).load(user.photoUrl).apply(options).into(btn_user)
        }
        else {
            popup = Dialog(this)
            //setto i provider
            providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
            )
            //custom fun that triggers startActivityForResult
            showSignInOPtions()
            Log.d("porcamadonna","sto qua")
        }

        btn_profiling.setOnClickListener {
            val intent = Intent(this, ProfilingActivity::class.java).apply {}
            //to pass arguments to next activity
            //intent.putExtra("key", value); //Optional parameters
            //on the other side---> intent =  getIntent();
            //    String value = intent.getStringExtra("key")
            startActivity(intent)
        }
        btn_user.setOnClickListener {
            val intent = Intent(this, AccountMngActivity::class.java).apply {}
            startActivity(intent)
        }
        btn_explore.setOnClickListener {
            val intent = Intent(this, ExploreActivity::class.java).apply {}
            startActivity(intent)
        }

        ///shake feature
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

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
                val intent = Intent(this@MainActivity, CityDetailsActivity::class.java).apply {}
                intent.putExtra("from_shake",true)
                startActivity(intent)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    private fun showSignInOPtions() {
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.AppTheme)
            .setLogo(R.drawable.logo)
            .build(), 666)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 666) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Toast.makeText(this, "Welcome, "+user.displayName, Toast.LENGTH_LONG ).show()
                    Log.d("porcamadonna",user.photoUrl.toString())


                    Glide.with(applicationContext).load(user.photoUrl).apply(options).into(btn_user)


                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}
