package com.example.suggestrip

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*


const val EXTRA_MESSAGE = "com.example.suggestrip.MESSAGE"
class MainActivity : AppCompatActivity(){
    var popup: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        popup = Dialog(this)

        btn_profiling.setOnClickListener {
            val intent = Intent(this, ProfilingActivity::class.java).apply {}
            startActivity(intent)
        }
    }
    fun ShowPopup(v: View?) {
        popup?.setContentView(R.layout.user_popup)
        val txtclose = popup!!.findViewById<View>(R.id.txtclose) as TextView
        txtclose.setOnClickListener { popup!!.dismiss() }
        popup!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup!!.show()
    }

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