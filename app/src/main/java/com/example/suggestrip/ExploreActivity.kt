package com.example.suggestrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_explore.*

class ExploreActivity : AppCompatActivity() {
    var months_array = arrayOf("MIAMI","NEW YORK", "PARIS","ROME","TOKYO")
    var img_months_array = arrayOf(R.drawable.miami,R.drawable.new_york,R.drawable.paris,R.drawable.rome, R.drawable.tokyo)

    lateinit var adapter :RecyclerViewExploreAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        adapter = RecyclerViewExploreAdapter(months_array,img_months_array)

/*
        val layoutManager = LinearLayoutManager(this)
*/
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        rv.layoutManager = layoutManager
        //sets divider in the list
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

        //Attaches adapter with RecyclerView.
        rv.adapter = adapter
    }
}