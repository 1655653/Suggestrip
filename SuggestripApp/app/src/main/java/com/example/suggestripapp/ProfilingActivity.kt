package com.example.suggestripapp

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
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
import androidx.viewpager.widget.ViewPager
import com.dev.sacot41.scviewpager.*
import kotlinx.android.synthetic.main.activity_profiling.*

class ProfilingActivity : AppCompatActivity() {
    private var NUM_PAGES = 5

    private var mViewPager: SCViewPager? = null
    private var mPageAdapter: SCViewPagerAdapter? = null
    private var mDotsView: DotsView? = null
    var lock_dollar = false
    var dollar_array = BooleanArray(3)
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
                when (position){
                    0 -> tv_question.text = "How many people travel?"
                    1 -> {
                        tv_question.text = "How much money do you want to spend?"
                        lock_dollar = false
                        btn_2dollar.visibility = VISIBLE
                        btn_3dollar.visibility = VISIBLE
                    }
                    2 -> {
                        tv_question.text = "How much do you like nature?"
                        if(!dollar_array[1]) {
                            btn_2dollar.animation?.fillAfter = false
                            btn_2dollar.visibility = GONE
                            }
                        if(!dollar_array[2]) {
                            btn_3dollar.animation?.fillAfter = false
                            btn_3dollar.visibility = GONE
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

        val size: Point = SCViewAnimationUtil.getDisplaySize(this)

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
        mViewPager!!.addAnimation(one_gone_anim)

        //ANIM OMINO 2
        val two_gone_anim = SCViewAnimation(two)
        two_gone_anim.addPageAnimation(animation_first_question_omino)
        mViewPager!!.addAnimation(two_gone_anim)

        //ANIM OMINO 3
        val three_gone_anim = SCViewAnimation(three)
        three_gone_anim.addPageAnimation(animation_first_question_omino)
        mViewPager!!.addAnimation(three_gone_anim)
        //ANIM OMINO 3+
        val threeplus_gone_anim = SCViewAnimation(iv_plus_3)
        threeplus_gone_anim.addPageAnimation(animation_first_question_omino)
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

        //ANIM DOLLAR1
        val dollar1_animation = SCViewAnimation(btn_1dollar)
        val store_dollar_y = (size.y / 2.5).toInt()
        dollar1_animation.startToPosition(null, -size.y)
        dollar1_animation.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar1_animation.addPageAnimation(SCPositionAnimation(this, 1, -size.x/7,store_dollar_y ))
        mViewPager!!.addAnimation(dollar1_animation)


        btn_1dollar.setOnClickListener {
            if(!lock_dollar ){
                btn_1dollar.setImageResource(R.drawable.filled_cost)
                btn_2dollar.setImageResource(R.drawable.empty_cost)
                btn_3dollar.setImageResource(R.drawable.empty_cost)
                dollar_array[0] = true
                dollar_array[1] = false
                dollar_array[2] = false
            }
        }


        //ANIM DOLLAR2
        val dollar2_animation = SCViewAnimation(btn_2dollar)
        dollar2_animation.startToPosition(null, -size.y)
        dollar2_animation.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar2_animation.addPageAnimation(SCPositionAnimation(this, 1, -size.x/3, store_dollar_y))
        mViewPager!!.addAnimation(dollar2_animation)
        btn_2dollar.setOnClickListener {
            if(!lock_dollar ){
                btn_1dollar.setImageResource(R.drawable.filled_cost)
                btn_2dollar.setImageResource(R.drawable.filled_cost)
                btn_3dollar.setImageResource(R.drawable.empty_cost)
                dollar_array[0] = true
                dollar_array[1] = true
                dollar_array[2] = false
            }
        }

        //ANIM DOLLAR3
        val dollar3_animation = SCViewAnimation(btn_3dollar)
        dollar3_animation.startToPosition(null, -size.y)
        dollar3_animation.addPageAnimation(SCPositionAnimation(this, 0, 0, size.y))
        dollar3_animation.addPageAnimation(SCPositionAnimation(this, 1, (-size.x/1.9).toInt(), store_dollar_y))
        mViewPager!!.addAnimation(dollar3_animation)
        btn_3dollar.setOnClickListener {
            if(!lock_dollar ) {
                btn_1dollar.setImageResource(R.drawable.filled_cost)
                btn_2dollar.setImageResource(R.drawable.filled_cost)
                btn_3dollar.setImageResource(R.drawable.filled_cost)
                dollar_array[0] = true
                dollar_array[1] = true
                dollar_array[2] = true
            }
        }



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