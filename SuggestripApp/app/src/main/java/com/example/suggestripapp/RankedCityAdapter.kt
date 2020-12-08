package com.example.suggestripapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.suggestripapp.fav.FavDB
import kotlinx.android.synthetic.main.activity_city_details.*

class RankedCityAdapter(var context: Context, var arrayList: MutableList<RankedCity>) : BaseAdapter()
{
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = View.inflate(context, R.layout.ranked_city, null)
        var icons:ImageView = view.findViewById(R.id.imageView)
        var name: TextView = view.findViewById(R.id.titleTextView)
        //var score: TextView = view.findViewById(R.id.score)
        var position_text: TextView = view.findViewById(R.id.position)
        var crown: ImageView = view.findViewById(R.id.crown)

        var ranked_city = arrayList.get(position)
        var options = RequestOptions()
            .placeholder(R.drawable.logo)
            .centerCrop()
        Glide.with(context).load(ranked_city.img_url).apply(options).into(icons)
        when(position){
            0 -> crown.setImageResource(R.drawable.gold)
            1 -> crown.setImageResource(R.drawable.silver)
            2 -> crown.setImageResource(R.drawable.bronze)
            else -> crown.visibility = GONE
        }

        var db = FavDB(context)
        managePreferred(db, arrayList[position],view)

        view.setOnClickListener { v ->
            val context: Context = v.context
            val intent = Intent(context, CityDetailsActivity::class.java)
            intent.putExtra("city",arrayList[position])
            Log.d("algo", arrayList[position].toString())
            intent.putExtra("from_shake", false)
            intent.putExtra("from_algo", true)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        //score.text = "Score: " + String.format("%.2f", ranked_city.score)
        name.text = ranked_city.name
        position_text.text = "#" + (position+1).toString() + "\nScore: " + String.format("%.1f", ranked_city.score)
        return  view!!
    }
    private fun managePreferred(db: FavDB, ranked_city: RankedCity, view: View) {
        val query = "SELECT * FROM " + FavDB.TABLE_NAME + " WHERE " + FavDB.ID + " = ${ranked_city.ID.toString()} "
        var cursor = db.readableDatabase.rawQuery(query, null, null)
        var heart: ImageView = view.findViewById(R.id.favBtn)
        if (cursor.moveToFirst()) {
            heart.setImageResource(R.drawable.ic_favorite_red_24dp)
        }

        heart.setOnClickListener {
            cursor = db.readableDatabase.rawQuery(query, null, null)
            if (cursor.moveToFirst()) {
                db.remove_fav(ranked_city.ID.toString())
                heart.setImageResource(R.drawable.ic_favorite_shadow_24dp)
                Toast.makeText(context, "${ranked_city.name} removed from the favourites", Toast.LENGTH_SHORT).show()
            }
            else{
                heart.setImageResource(R.drawable.ic_favorite_red_24dp)
                db.insertIntoTheDatabase(ranked_city.name, ranked_city.img_url, ranked_city.ID.toString(), "1")
                Toast.makeText(context, "${ranked_city.name} added to the favourites!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}