package com.example.suggestrip

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.sqrt


const val EXTRA_MESSAGE = "com.example.suggestrip.MESSAGE"

class MainActivity : AppCompatActivity(){
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    var popup: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        popup = Dialog(this)

        btn_profiling.setOnClickListener {
            val intent = Intent(this, ProfilingActivity::class.java).apply {}
            //to pass arguments to next activity
            //Intent.putExtra("key", value); //Optional parameters
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

    fun porcoddiopadre(){

    }
    /*fun ShowPopup(v: View?) {  //ADD TO THE XML THE ONLCIK = showPopup
        popup?.setContentView(R.layout.user_popup)
        val txtclose = popup!!.findViewById<View>(R.id.txtclose) as TextView
        txtclose.setOnClickListener { popup!!.dismiss() }
        popup!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup!!.show()
    }*/

}

/*var pb = findViewById<ProgressBar>(R.id.progressBar)

        when(p0?.id){
            R.id.btn_profiling -> {
                if (pb.isVisible) pb.visibility = View.INVISIBLE; //CLICCO SECONDA VOLTA
                else {
                    pb.visibility = View.VISIBLE //CLICCO PRIMA VOLTA
                    tv_hello.visibility = View.INVISIBLE
                    tv_hello.visibility = View.INVISIBLE
                };

            };
        }*/