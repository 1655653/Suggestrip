package com.example.suggestripapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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

        var ranked_city = arrayList.get(position)
        var options = RequestOptions()
            .placeholder(R.drawable.logo)
            .centerCrop()
        Glide.with(context).load(ranked_city.img_url).apply(options).into(icons)
        view.setOnClickListener { v ->
            val context: Context = v.context
            val intent = Intent(context, CityDetailsActivity::class.java)
            intent.putExtra("city",arrayList[position])
            intent.putExtra("from_shake", "false")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        name.text = ranked_city.name
        return  view!!
    }
}