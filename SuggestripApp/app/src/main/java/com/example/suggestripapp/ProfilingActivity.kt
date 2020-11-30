package com.example.suggestripapp

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.dev.sacot41.scviewpager.*
import kotlinx.android.synthetic.main.activity_profiling.*
import java.util.*


class ProfilingActivity : AppCompatActivity() {
    private var NUM_PAGES = 9

    private var mViewPager: SCViewPager? = null
    private var mPageAdapter: SCViewPagerAdapter? = null
    private var mDotsView: DotsView? = null
    //lock per non far cambiare immagine quando stai in un altra pagina
    var lock_dollar = false
    var lock_greek = false
    var lock_tree = false
    var lock_glass = false
    var lock_car = false
    var lock_ball = false
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

        val greek_array = listOf(btn_greek1, btn_greek2, btn_greek3, btn_greek4, btn_greek5)
        var greek_array_boolean = BooleanArray(greek_array.size)

        val tree_array = listOf(btn_tree1, btn_tree2, btn_tree3, btn_tree4, btn_tree5)
        var tree_array_boolean = BooleanArray(tree_array.size)

        val glass_array = listOf(btn_glass1, btn_glass2, btn_glass3, btn_glass4, btn_glass5)
        var glass_array_boolean = BooleanArray(glass_array.size)

        val car_array = listOf(btn_car1, btn_car2, btn_car3, btn_car4, btn_car5)
        var car_array_boolean = BooleanArray(car_array.size)

        val ball_array = listOf(btn_ball1, btn_ball2, btn_ball3, btn_ball4, btn_ball5)
        var ball_array_boolean = BooleanArray(ball_array.size)

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
                lock_tree = true
                lock_glass = true
                lock_car = true
                lock_ball = true
                if (position < 3) {
                    tv_culture_numb.visibility = INVISIBLE
                }
                if (position < 4) {
                    tv_sport_numb.visibility = INVISIBLE
                }

                if (position < 5) {
                    tv_infrastructure_numb.visibility = INVISIBLE
                }
                if (position < 6) {
                    tv_nightlife_numb.visibility = INVISIBLE
                }

                if (position < 7) {
                    tv_nature_numb.visibility = INVISIBLE
                }
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

                        } else if (dollar_array[1]) {
                            btn_1dollar.animation?.fillAfter = false
                            btn_1dollar.visibility = GONE
                            btn_3dollar.animation?.fillAfter = false
                            btn_3dollar.visibility = GONE
                            btn_3dollar2.animation?.fillAfter = false
                            btn_3dollar2.visibility = GONE
                            btn_3dollar3.animation?.fillAfter = false
                            btn_3dollar3.visibility = GONE
                        } else if (dollar_array[2]) {
                            btn_1dollar.animation?.fillAfter = false
                            btn_1dollar.visibility = GONE
                            btn_2dollar.animation?.fillAfter = false
                            btn_2dollar.visibility = GONE
                            btn_2dollar2.animation?.fillAfter = false
                            btn_2dollar2.visibility = GONE
                        } else {
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
                        for (g in greek_array) {
                            g.visibility = VISIBLE
                        }
                    }
                    3 -> {
                        tv_question.text = "How much are you into sports?"
                        lock_ball = false
                        tv_culture_numb.visibility = VISIBLE
                        tv_culture_numb.text = greek_array_boolean.count { it }.toString()
                        for (g in greek_array) {
                            if (g != greek_array[0]) {
                                g.animation?.fillAfter = false
                                g.visibility = GONE
                            }
                        }
                    }
                    4 -> {
                        lock_car = false
                        tv_question.text = "How much do you want to use public transportations and more general infrastructures?"
                        tv_sport_numb.visibility = VISIBLE
                        tv_sport_numb.text = ball_array_boolean.count { it }.toString()

                    }
                    5 -> {
                        lock_glass = false
                        tv_question.text = "How much do you enjoy night life?"
                        tv_infrastructure_numb.visibility = VISIBLE
                        tv_infrastructure_numb.text = car_array_boolean.count { it }.toString()

                    }
                    6 -> {
                        lock_tree = false
                        tv_question.text = "How much do you care about nature?"
                        tv_nightlife_numb.visibility = VISIBLE
                        tv_nightlife_numb.text = glass_array_boolean.count { it }.toString()

                    }
                    7 -> {
                        tv_nature_numb.visibility = VISIBLE
                        tv_nature_numb.text = tree_array_boolean.count { it }.toString()
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
            iv_plus_3.animation?.fillAfter = false
            iv_plus_3.visibility = View.GONE
            drop(one, size)
        }
        btn_2_ppl.setOnClickListener {
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
        val animation_first_question_omino = SCPositionAnimation(this, 0, 0, (size.y / 1.85).toInt())
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

        val go_plus_little_left= SCPositionAnimation(this, 0, -300, (size.y / 1.70).toInt())
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

        //GREEK PORCAMADONNA QUESTA è MAGIA
        greek_array.forEachIndexed{ i, guys ->
                fall(guys, 1, 2)
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

//        //FIRST GREEK ANIM GO DOWN
        val greek_animation = SCViewAnimation(btn_greek1)
        greek_animation.startToPosition(null, -size.y)
        greek_animation.addPageAnimation(SCPositionAnimation(this, 2, (size.x / 1.4).toInt(), store_dollar_y))
        mViewPager!!.addAnimation(greek_animation)


        ///*******************************************fourth  QUESTION-ball
        ball_array.forEachIndexed{ i, guys ->
            fall(guys, 2, 3)
            guys.setOnClickListener {
                if(!lock_ball){
                    if (!ball_array_boolean[i]){
                        ball_array_boolean[i] = true
                        for (sub_ball in 0..i){
                            ball_array[sub_ball].setImageResource(R.drawable.filled_ball)
                            ball_array_boolean[sub_ball] = true
                        }
                    }
                    else{
                        for (over_ball in i+1 until ball_array.size){
                            ball_array[over_ball].setImageResource(R.drawable.empty_ball)
                            ball_array_boolean[over_ball] = false
                        }
                    }
                }
            }
        }
        val pad = 30
        val ball_animation = SCViewAnimation(btn_ball1)
        ball_animation.startToPosition(null, -size.y)
        ball_animation.addPageAnimation(SCPositionAnimation(this, 3, 0, -store_dollar_y + pad)) //6
        mViewPager!!.addAnimation(ball_animation)


        ///*******************************************fifth QUESTION-CAR
        car_array.forEachIndexed{ i, guys ->
            fall(guys, 3, 4) //4,5
            guys.setOnClickListener {
                if(!lock_car){
                    if (!car_array_boolean[i]){
                        car_array_boolean[i] = true
                        for (sub_car in 0..i){
                            car_array[sub_car].setImageResource(R.drawable.filled_car)
                            car_array_boolean[sub_car] = true
                        }
                    }
                    else{
                        for (over_car in i+1 until car_array.size){
                            car_array[over_car].setImageResource(R.drawable.empty_car)
                            car_array_boolean[over_car] = false
                        }
                    }
                    Log.d("dioputtana", Arrays.toString(car_array_boolean))
                }
            }
        }
        val car_animation = SCViewAnimation(btn_car1)
        car_animation.startToPosition(null, -size.y)
        car_animation.addPageAnimation(SCPositionAnimation(this, 4, (size.x / 3.5).toInt(), -store_dollar_y + pad)) //5
        mViewPager!!.addAnimation(car_animation)

        ///*******************************************SIXTH QUESTION-GLASS
        glass_array.forEachIndexed{ i, guys ->
            fall(guys, 4, 5)
            guys.setOnClickListener {
                if(!lock_glass){
                    if (!glass_array_boolean[i]){
                        glass_array_boolean[i] = true
                        for (sub_glass in 0..i){
                            glass_array[sub_glass].setImageResource(R.drawable.filled_glass)
                            glass_array_boolean[sub_glass] = true
                        }
                    }
                    else{
                        for (over_glass in i+1 until glass_array.size){
                            glass_array[over_glass].setImageResource(R.drawable.empty_glass)
                            glass_array_boolean[over_glass] = false
                        }
                    }
                    Log.d("dioputtana", Arrays.toString(glass_array_boolean))
                }
            }
        }

        val glass_animation = SCViewAnimation(btn_glass1)
        glass_animation.startToPosition(null, -size.y)
        glass_animation.addPageAnimation(SCPositionAnimation(this, 5, (size.x / 2).toInt(), -store_dollar_y + pad))
        mViewPager!!.addAnimation(glass_animation)

        ///*******************************************LAST QUESTION-TREE
        tree_array.forEachIndexed{ i, guys ->
            fall(guys, 5, 6) //2,3
            guys.setOnClickListener {
                if(!lock_tree){
                    if (!tree_array_boolean[i]){
                        tree_array_boolean[i] = true
                        for (sub_tree in 0..i){
                            tree_array[sub_tree].setImageResource(R.drawable.filled_tree)
                            tree_array_boolean[sub_tree] = true
                        }
                    }
                    else{
                        for (over_tree in i+1 until tree_array.size){
                            tree_array[over_tree].setImageResource(R.drawable.empty_tree)
                            tree_array_boolean[over_tree] = false
                        }
                    }
                    Log.d("dioputtana", Arrays.toString(tree_array_boolean))
                }
            }
        }
        val tree_animation = SCViewAnimation(btn_tree1)
        tree_animation.startToPosition(null, -size.y)
        tree_animation.addPageAnimation(SCPositionAnimation(this, 6, (size.x / 1.4).toInt(), -store_dollar_y))
        mViewPager!!.addAnimation(tree_animation)



    }

    private fun fall(btnGreek: ImageButton?, p1: Int, p2: Int) {
        btnGreek!!.visibility = VISIBLE
        val size = SCViewAnimationUtil.getDisplaySize(this)
        val greek_animation = SCViewAnimation(btnGreek)
        greek_animation.startToPosition(null, -size.y)
        greek_animation.addPageAnimation(SCPositionAnimation(this, p1, 0, size.y))
        greek_animation.addPageAnimation(SCPositionAnimation(this, p2, size.x, 0))
        mViewPager!!.addAnimation(greek_animation)

    }

    private fun drop(one: View, size: Point) {
        one.visibility = View.VISIBLE
        val anim = TranslateAnimation(0F, 0F, -(size.y / 2).toFloat(), (size.y / 9).toFloat())
        anim.duration = 1000
        anim.fillAfter = true
        one.startAnimation(anim)
    }

    fun View.getLocationOnScreen(): Point{
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return Point(location[0], location[1])
    }

    override fun onBackPressed() {
        val ci = mViewPager?.currentItem
        if (ci != null && ci>0) {
            mViewPager?.setCurrentItem(ci-1,true)
        }
        else{
            super.onBackPressed()
        }
    }

}