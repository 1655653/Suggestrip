package com.example.suggestripapp

import android.graphics.Point
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.dev.sacot41.scviewpager.*
import kotlinx.android.synthetic.main.activity_profiling.*
import java.util.*


class ProfilingActivity : AppCompatActivity() {
    private var NUM_PAGES = 5

    private var mViewPager: SCViewPager? = null
    private var mPageAdapter: SCViewPagerAdapter? = null
    private var mDotsView: DotsView? = null
    var lock_dollar = false
    var lock_greek = false
    var dollar_array = BooleanArray(3)

    val size: Point? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_profiling)

        mDotsView = findViewById<DotsView>(R.id.dotsview_main)
        mDotsView!!.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView!!.setNumberOfPage(NUM_PAGES);

        mPageAdapter = SCViewPagerAdapter(supportFragmentManager);
        mPageAdapter!!.setNumberOfPage(NUM_PAGES);
        mPageAdapter!!.setFragmentBackgroundColor(R.color.green_bg);
        mViewPager =  findViewById<SCViewPager>(R.id.viewpager_main_activity);
        mViewPager!!.adapter = mPageAdapter;

        val size: Point = SCViewAnimationUtil.getDisplaySize(this)
        val greek_array = listOf(btn_greek1,btn_greek2,btn_greek3,btn_greek4,btn_greek5)
        var greek_array_boolean = BooleanArray(greek_array.size)
        //cambia dot
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                lock_dollar = true
                lock_greek = true
                when (position) {
                    0 -> tv_question.text = "How many people travel?"
                    1 -> {
                        tv_question.text = "How much money do you want to spend?"
                        lock_dollar = false
                        btn_1dollar.visibility = VISIBLE
                        btn_2dollar.visibility = VISIBLE
                        btn_2dollar2.visibility = VISIBLE
                        btn_3dollar.visibility = VISIBLE
                        btn_3dollar2.visibility = VISIBLE
                        btn_3dollar3.visibility = VISIBLE
                    }
                    2 -> {
                        tv_question.text = "How much do you enjoy visiting historic places, museum ecc.."
                        lock_greek = false
                        if (dollar_array[0]) {
                            btn_2dollar.animation?.fillAfter = false
                            btn_2dollar.visibility = GONE
                            btn_2dollar2.animation?.fillAfter = false
                            btn_2dollar2.visibility = GONE
                            btn_3dollar.animation?.fillAfter = false
                            btn_3dollar.visibility = GONE
                            btn_3dollar2.animation?.fillAfter = false
                            btn_3dollar2.visibility = GONE
                            btn_3dollar3.animation?.fillAfter = false
                            btn_3dollar3.visibility = GONE

                        }

                        else if (dollar_array[1]) {
                            btn_1dollar.animation?.fillAfter = false
                            btn_1dollar.visibility = GONE
                            btn_3dollar.animation?.fillAfter = false
                            btn_3dollar.visibility = GONE
                            btn_3dollar2.animation?.fillAfter = false
                            btn_3dollar2.visibility = GONE
                            btn_3dollar3.animation?.fillAfter = false
                            btn_3dollar3.visibility = GONE
                        }
                        else if (dollar_array[2]) {
                            btn_1dollar.animation?.fillAfter = false
                            btn_1dollar.visibility = GONE
                            btn_2dollar.animation?.fillAfter = false
                            btn_2dollar.visibility = GONE
                            btn_2dollar2.animation?.fillAfter = false
                            btn_2dollar2.visibility = GONE
                        }
                        else{
                            btn_1dollar.animation?.fillAfter = false
                            btn_1dollar.visibility = GONE
                            btn_2dollar.animation?.fillAfter = false
                            btn_2dollar.visibility = GONE
                            btn_2dollar2.animation?.fillAfter = false
                            btn_2dollar2.visibility = GONE
                            btn_3dollar.animation?.fillAfter = false
                            btn_3dollar.visibility = GONE
                            btn_3dollar2.animation?.fillAfter = false
                            btn_3dollar2.visibility = GONE
                            btn_3dollar3.animation?.fillAfter = false
                            btn_3dollar3.visibility = GONE
                        }
                        for(g in  greek_array){
                            g.visibility = VISIBLE
                        }
                    }
                    3 ->{
                        tv_culture_numb.visibility = VISIBLE
                        tv_culture_numb.text = greek_array_boolean.count{it}.toString()
                        for(g in  greek_array){
                            if(g != greek_array[0]) {
                                g.animation?.fillAfter = false
                                g.visibility = GONE
                            }
                        }
                    }
                }
                val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                tv_question.animation = animFadeIn
                tv_question.startAnimation(animFadeIn)
                mDotsView!!.selectDot(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("porcamadonna", btn_1dollar.getLocationOnScreen().toString())
            }
        })

        val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        tv_question.animation = animFadeIn
        tv_question.startAnimation(animFadeIn)



        ///*******************************************FIRST QUESTION-OMINI
        val one: View = findViewById(R.id.iv_one)
        val two: View = findViewById(R.id.iv_two)
        val three: View = findViewById(R.id.iv_three)
        btn_1_ppl.setOnClickListener {
            two.animation?.fillAfter = false
            two.visibility = View.GONE
            three.animation?.fillAfter = false
            three.visibility = View.GONE
            iv_plus_3.animation?.fillAfter = false
            iv_plus_3.visibility = View.GONE
            drop(one, size)
        }
        btn_2_ppl.setOnClickListener {
            three.animation?.fillAfter = false
            three.visibility = View.GONE
            iv_plus_3.animation?.fillAfter = false
            iv_plus_3.visibility = View.GONE
            drop(one, size)
            drop(two, size)
        }

        btn_3_ppl.setOnClickListener {
            iv_plus_3.animation?.fillAfter = false
            iv_plus_3.visibility = View.GONE
            drop(one, size)
            drop(two, size)
            drop(three, size)
        }
        btn_more3_ppl.setOnClickListener {
            drop(one, size)
            drop(two, size)
            drop(three, size)
            drop(iv_plus_3, size)

        }

        val animation_first_question = SCPositionAnimation(this, 0, -size.x, 0)
        val animation_first_question_omino = SCPositionAnimation(this, 0, 0, (size.y / 1.8).toInt())
        //ANIM OMINO 1
        val one_gone_anim = SCViewAnimation(one)
        one_gone_anim.addPageAnimation(animation_first_question_omino)
        val go_little_right= SCPositionAnimation(this, 0, 80, (size.y / 1.8).toInt())
        one_gone_anim.addPageAnimation(go_little_right)
        mViewPager!!.addAnimation(one_gone_anim)

        //ANIM OMINO 2
        val two_gone_anim = SCViewAnimation(two)
        two_gone_anim.addPageAnimation(animation_first_question_omino)
        mViewPager!!.addAnimation(two_gone_anim)


        //ANIM OMINO 3
        val three_gone_anim = SCViewAnimation(three)
        three_gone_anim.addPageAnimation(animation_first_question_omino)
        val go_little_left= SCPositionAnimation(this, 0, -80, (size.y / 1.8).toInt())
        three_gone_anim.addPageAnimation(go_little_left)

        mViewPager!!.addAnimation(three_gone_anim)
        //ANIM OMINO 3+
        val threeplus_gone_anim = SCViewAnimation(iv_plus_3)

        val go_plus_little_left= SCPositionAnimation(this, 0, -300, (size.y / 1.65).toInt())
        threeplus_gone_anim.addPageAnimation(go_plus_little_left)
        mViewPager!!.addAnimation(threeplus_gone_anim)

        //ANIM BTN1
        val btn1_anim = SCViewAnimation(btn_1_ppl)
        btn1_anim.addPageAnimation(animation_first_question)
        mViewPager!!.addAnimation(btn1_anim)

        //ANIM BTN2
        val btn2_anim = SCViewAnimation(btn_2_ppl)
        btn2_anim.addPageAnimation(animation_first_question)
        mViewPager!!.addAnimation(btn2_anim)

        //ANIM BTN3
        val btn3_anim = SCViewAnimation(btn_3_ppl)
        btn3_anim.addPageAnimation(animation_first_question)
        mViewPager!!.addAnimation(btn3_anim)

        //ANIM BTNMORE
        val btn_more_3_anim = SCViewAnimation(btn_more3_ppl)
        btn_more_3_anim.addPageAnimation(animation_first_question)
        mViewPager!!.addAnimation(btn_more_3_anim)

        ///*******************************************SECOND QUESTION-DOLLAR

        //ANIM DOLLAR1
        val dollar1_animation = SCViewAnimation(btn_1dollar)
        val store_dollar_y = (size.y / 2.5).toInt()
        dollar1_animation.startToPosition(null, -size.y)
        dollar1_animation.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar1_animation.addPageAnimation(SCPositionAnimation(this, 1, -size.x / 7, store_dollar_y))
        mViewPager!!.addAnimation(dollar1_animation)


        btn_1dollar.setOnClickListener {
            if(!lock_dollar ){
                btn_1dollar.setImageResource(R.drawable.filled_cost)
                btn_2dollar.setImageResource(R.drawable.empty_cost)
                btn_2dollar2.setImageResource(R.drawable.empty_cost)
                btn_3dollar.setImageResource(R.drawable.empty_cost)
                btn_3dollar2.setImageResource(R.drawable.empty_cost)
                btn_3dollar3.setImageResource(R.drawable.empty_cost)
                dollar_array[0] = true
                dollar_array[1] = false
                dollar_array[2] = false
            }
        }


        //ANIM DOLLAR2
        val dollar2_animation = SCViewAnimation(btn_2dollar)
        dollar2_animation.startToPosition(null, -size.y)
        dollar2_animation.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar2_animation.addPageAnimation(SCPositionAnimation(this, 1, -size.x / 3, store_dollar_y))
        mViewPager!!.addAnimation(dollar2_animation)

        val dollar2_animation2 = SCViewAnimation(btn_2dollar2)
        dollar2_animation2.startToPosition(null, -size.y)
        dollar2_animation2.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar2_animation2.addPageAnimation(SCPositionAnimation(this, 1, -size.x / 3, store_dollar_y))
        mViewPager!!.addAnimation(dollar2_animation2)

        btn_2dollar2.setOnClickListener {
            if(!lock_dollar ){
                btn_1dollar.setImageResource(R.drawable.empty_cost)
                btn_2dollar.setImageResource(R.drawable.filled_cost)
                btn_2dollar2.setImageResource(R.drawable.filled_cost)
                btn_3dollar.setImageResource(R.drawable.empty_cost)
                btn_3dollar2.setImageResource(R.drawable.empty_cost)
                btn_3dollar3.setImageResource(R.drawable.empty_cost)
                dollar_array[0] = false
                dollar_array[1] = true
                dollar_array[2] = false
            }
        }

        //ANIM DOLLAR3
        val left_padding = (-size.x/1.7).toInt()
        val dollar3_animation = SCViewAnimation(btn_3dollar)
        dollar3_animation.startToPosition(null, -size.y)
        dollar3_animation.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar3_animation.addPageAnimation(SCPositionAnimation(this, 1, left_padding, store_dollar_y))
        mViewPager!!.addAnimation(dollar3_animation)

        val dollar3_animation2 = SCViewAnimation(btn_3dollar2)
        dollar3_animation2.startToPosition(null, -size.y)
        dollar3_animation2.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar3_animation2.addPageAnimation(SCPositionAnimation(this, 1, left_padding, store_dollar_y))
        mViewPager!!.addAnimation(dollar3_animation2)

        val dollar3_animation3 = SCViewAnimation(btn_3dollar3)
        dollar3_animation3.startToPosition(null, -size.y)
        dollar3_animation3.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar3_animation3.addPageAnimation(SCPositionAnimation(this, 1, left_padding, store_dollar_y))
        mViewPager!!.addAnimation(dollar3_animation3)
        btn_3dollar3.setOnClickListener {
            if(!lock_dollar ) {
                btn_1dollar.setImageResource(R.drawable.empty_cost)
                btn_2dollar.setImageResource(R.drawable.empty_cost)
                btn_2dollar2.setImageResource(R.drawable.empty_cost)
                btn_3dollar.setImageResource(R.drawable.filled_cost)
                btn_3dollar2.setImageResource(R.drawable.filled_cost)
                btn_3dollar3.setImageResource(R.drawable.filled_cost)
                dollar_array[0] = false
                dollar_array[1] = false
                dollar_array[2] = true
            }
        }

        ///*******************************************THIRD QUESTION-GREEK
        //transition, poi l'ho tolta'
//        val mImages = intArrayOf(R.drawable.empty_greek, R.drawable.filled_greek)
//        val td = TransitionDrawable(arrayOf(
//                resources.getDrawable(mImages[0]),
//                resources.getDrawable(mImages[1])
//        ))

        //GREEK PORCAMADONNA QUESTA è MAGIA
        greek_array.forEachIndexed{i, guys ->
                fall(guys)
                guys.setOnClickListener {
                    if(!lock_greek){
                        if (!greek_array_boolean[i]){
                            greek_array_boolean[i] = true
                            for (sub_greek in 0..i){
                                greek_array[sub_greek].setImageResource(R.drawable.filled_greek)
                                greek_array_boolean[sub_greek] = true
                            }
                        }
                    else{
                        for (over_greek in i+1 until greek_array.size){
                            greek_array[over_greek].setImageResource(R.drawable.empty_greek)
                            greek_array_boolean[over_greek] = false
                        }
                    }
                    Log.d("dioputtana", Arrays.toString(greek_array_boolean))
                    }
                }
        }
        //FIRST GREEK ANIM GO DOWN
        val greek_animation = SCViewAnimation(btn_greek1)
        greek_animation.startToPosition(null, -size.y)
        greek_animation.addPageAnimation(SCPositionAnimation(this, 2, (size.x/1.4).toInt(), store_dollar_y))
        mViewPager!!.addAnimation(greek_animation)




    }

    private fun fall(btnGreek: ImageButton?) {
        btnGreek!!.visibility = VISIBLE
        val size = SCViewAnimationUtil.getDisplaySize(this)
        val greek_animation = SCViewAnimation(btnGreek)
        greek_animation.startToPosition(null, -size.y)
        greek_animation.addPageAnimation(SCPositionAnimation(this, 1, 0, size.y))
        greek_animation.addPageAnimation(SCPositionAnimation(this, 2, size.x, 0))
        mViewPager!!.addAnimation(greek_animation)

    }

    private fun drop(one: View, size: Point) {
        one.visibility = View.VISIBLE
        val anim = TranslateAnimation(0F, 0F, -(size.y / 2).toFloat(), (size.y / 10).toFloat())
        anim.duration = 1000
        anim.fillAfter = true
        one.startAnimation(anim)
    }

    fun View.getLocationOnScreen(): Point{
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return Point(location[0], location[1])
    }

}