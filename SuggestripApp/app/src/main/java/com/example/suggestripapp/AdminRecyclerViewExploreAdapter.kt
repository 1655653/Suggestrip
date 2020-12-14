package com.example.suggestripapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.suggestripapp.fav.FavDB
import jp.wasabeef.glide.transformations.*
import kotlinx.android.synthetic.main.row_layout.view.*


//qui creo l'adapter ma sopratutto la classe ViewHolder che mi serve per sistemare tutti gli item della cardview
class AdminRecyclerViewExploreAdapter(var months_array: Array<String>, var img_months_array: Array<String>, var city_list: MutableList<City>) : RecyclerView.Adapter<AdminRecyclerViewExploreAdapter.ViewHolder>()
 {
     inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
     {
         fun bind(months_array: Array<String>, img_months_array: Array<String>, position: Int){
             itemView.tv_city_name_rv.text = months_array[position]

             var options = RequestOptions()
                     .placeholder(R.drawable.logo)
                     .centerCrop()


             Glide.with(itemView.context).load(img_months_array[position]!!).apply(options).into(itemView.ib_city)
             Log.d("pocamadonna", img_months_array[position])

             var db = FavDB(itemView.context)
             managePreferred(db, city_list[position],itemView)

//             if(!itemView.intent.getBooleanExtra("admin",false))
//                 managePreferred(db, city_list[position],itemView)
//             else{
//                 itemView.favBtn.setImageResource(android.R.drawable.ic_menu_edit)
//             }

//
             //Picasso.get().load(img_months_array[position]).into(itemView.ib_city);
         }
         //quando clicco sulla citta starta un altra activity
         init
         {
             itemView.setOnClickListener { v ->
                 val context: Context = v.context
                 val intent = Intent(context, AdminCityDetailsActivity::class.java)
                 Log.d("porcoddio", city_list[adapterPosition].toString())

                 intent.putExtra("city", city_list[adapterPosition])
                 intent.putExtra("from_shake", false)
                 intent.putExtra("from_rv", true)
                 intent.putExtra("id", 1);
                 intent.putExtra("description", "send description to 2nd activity");
                 (context as Activity).startActivityForResult(intent, 5)
             }
         }
     }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(parent.context)
         val view = inflater.inflate(R.layout.row_layout, parent, false)
         return ViewHolder(view)
     }

     override fun onBindViewHolder(holder: AdminRecyclerViewExploreAdapter.ViewHolder, position: Int) {
         holder.bind(months_array, img_months_array, position)

     }

     override fun getItemCount(): Int {
         return months_array.size
     }
     private fun managePreferred(db: FavDB, ranked_city: City, view: View) {
         val query = "SELECT * FROM " + FavDB.TABLE_NAME + " WHERE " + FavDB.ID + " = ${ranked_city.ID.toString()} "
         var cursor = db.readableDatabase.rawQuery(query, null, null)
         var heart: ImageView = view.findViewById(R.id.favBtn)
         if (cursor.moveToFirst()) {
             heart.setImageResource(R.drawable.ic_favorite_red_24dp)
         }
         else{
             heart.setImageResource(R.drawable.ic_favorite_shadow_24dp)
         }

         heart.setOnClickListener {
             cursor = db.readableDatabase.rawQuery(query, null, null)
             if (cursor.moveToFirst()) {
                 db.remove_fav(ranked_city.ID.toString())
                 heart.setImageResource(R.drawable.ic_favorite_shadow_24dp)
                 Toast.makeText(view.context, "${ranked_city.name} removed from the favourites", Toast.LENGTH_SHORT).show()
             }
             else{
                 heart.setImageResource(R.drawable.ic_favorite_red_24dp)
                 db.insertIntoTheDatabase(ranked_city.name, ranked_city.img_url, ranked_city.ID.toString(), "1")
                 Toast.makeText(view.context, "${ranked_city.name} added to the favourites!", Toast.LENGTH_SHORT).show()
             }
         }
     }

     fun onActivityResult(requestCode: Int, resultCode: Int, intExtra: Int) {
         Log.d("CDA", "onActivityResult, now i have to update the heart")
         city_list.forEachIndexed { index, city ->
             if(city.ID == intExtra){

                 notifyItemChanged(index)

             }
         }

     }

 }
