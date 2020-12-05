package com.example.suggestripapp.fav

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suggestripapp.City
import com.example.suggestripapp.R
import kotlinx.android.synthetic.main.activity_explore.*

class FavActivity : AppCompatActivity() {
    var city_name_array = mutableListOf<String>("MIAMI", "NEW YORK", "PARIS", "ROME", "TOKYO")
    var img_cities_array = mutableListOf(R.drawable.miami, R.drawable.new_york, R.drawable.paris, R.drawable.rome, R.drawable.tokyo)
    var city_list = mutableListOf<City>()
    private var favDB: FavDB? = null
    //var city_name_array = mutableListOf<String>()
    //var img_cities_array = mutableListOf<String>()
    val favCityList = mutableListOf<City>()

    private var favAdapter: RecyclerViewFavAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        favDB = FavDB(this)
        rv.layoutManager = LinearLayoutManager(this)

        rv.setHasFixedSize(true)
        // add item touch helper

        // add item touch helper
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rv) // set swipe to recyclerview


        loadData()


    }

    private fun loadData() {
        if (city_list != null) {
            city_list.clear()
        }
        val db = favDB!!.readableDatabase
        /*city_name_array.forEachIndexed{ i, s ->
             //var c = City("", "", "", i, "", s, null, null, "null")
             favDB!!.insertIntoTheDatabase(s,img_cities_array[i],i.toString(),"1")
        }*/



        val cursor = favDB!!.select_all_favorite_list()
        try {
            while (cursor!!.moveToNext()) {
                val title = cursor!!.getString(cursor!!.getColumnIndex(FavDB.NAME))
                val id = cursor!!.getString(cursor!!.getColumnIndex(FavDB.ID)).toInt()
                val image = cursor!!.getString(cursor!!.getColumnIndex(FavDB.IMG_URL))
                var c = City("", image, "", id, "", title, null, null, null, weather = null)
                //val favItem = FavItem(title, id, image)
                favCityList.add(c)
            }
        } finally {
            if (cursor != null && cursor.isClosed) cursor.close()
            db.close()
        }


        favAdapter = RecyclerViewFavAdapter(city_name_array, img_cities_array, favCityList)
        rv.adapter = favAdapter
    }

    // remove item after swipe
    private val simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition // get position which is swipe
            val c = favCityList[position]
            if (direction == ItemTouchHelper.LEFT) { //if swipe left
                favAdapter!!.notifyItemRemoved(position) // item removed from recyclerview
                city_list.removeAt(position) //then remove item
                favDB!!.remove_fav(c.ID.toString()) // remove item from database

            }
        }
    }

}