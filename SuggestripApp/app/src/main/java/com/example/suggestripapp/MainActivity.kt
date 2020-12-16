package com.example.suggestripapp


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.suggestripapp.fav.FavActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
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
    private val admin_list_id = mutableListOf<String>("P3ggNn7i8tMcJ1Vq4nkKoRq8VnO2", "T4OkA2DQcwMNB4ElISmFVW3xi7w1", "4pp3oLbrjLMLf7NLcQEsKvctAu42")
    var is_admin = false
    //firebase authUI
    lateinit var providers: List<AuthUI.IdpConfig>

    var options = RequestOptions()
        .placeholder(R.drawable.user_small)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_user = findViewById<ImageView>(R.id.btn_user)


        val user = FirebaseAuth.getInstance().currentUser
        if(user != null && intent.getBooleanExtra("is_user_modified", false)){
            if(intent.getBooleanExtra("is_uri_propic_modified", false))
                Glide.with(applicationContext).load(user.photoUrl).apply(options).into(btn_user)
        }

        else if(getIntent().getBooleanExtra("back_from_ranked", false))  {
            for(a in admin_list_id){
                if(user?.uid == a) {
                    //btn_admin.visibility = VISIBLE
                    is_admin = true
                }

            }
            Glide.with(applicationContext).load(user?.photoUrl).apply(options).into(btn_user)
        }


        else {
            if(! getIntent().getBooleanExtra("back_from_shake", false) && !getIntent().getBooleanExtra("back_from_ranked", false)) { //se torno dallo shake non devo fare il sign in
                //setto i provider
                providers = Arrays.asList<AuthUI.IdpConfig>(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build(),
                        AuthUI.IdpConfig.AnonymousBuilder().build()
                )
                //custom fun that triggers startActivityForResult


                showSignInOPtions()
                Log.d("porcamadonna", "sto qua")
            }
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

            if(is_admin){
                intent.putExtra("is_admin", true)
            }
            Log.d("starnazzo", is_admin.toString())

            startActivity(intent)
        }
        btn_heart.setOnClickListener {
            val intent = Intent(this, FavActivity::class.java).apply {}
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
                intent.putExtra("from_shake", true)
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
                    // USER SUCCESFULLY SIGNED IN
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d("dyocanale", "user id:      " + user?.uid)


                    var welcome = "Welcome, "

                    for(admins in admin_list_id){
                        if(user?.uid == admins) {
                            is_admin = true
                            welcome+= "ADMIN " +user?.displayName
                        }
                    }
                    if(!is_admin) welcome+= user?.displayName

                    if (user != null) {
                        if(user.isAnonymous) welcome = "Welcome, Guest"
                        Toast.makeText(this, welcome, Toast.LENGTH_LONG).show()

                        val btn_user = findViewById<ImageView>(R.id.btn_user)
                        Glide.with(applicationContext).load(user.photoUrl).apply(options).into(btn_user)
                    }


                    // ...
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                    val intent = intent
                    finish()
                    startActivity(intent)
//                    var welcome = "sign-in Error, Logged as Guest"
//                    Toast.makeText(this, welcome, Toast.LENGTH_LONG).show()
                    

                }
            }


    }
}
