package com.example.suggestripapp.fav
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.suggestripapp.City
import com.example.suggestripapp.CityDetailsActivity
import com.example.suggestripapp.R


//qui creo l'adapter ma sopratutto la classe ViewHolder che mi serve per sistemare tutti gli item della cardview
class RecyclerViewFavAdapter(var months_array: MutableList<String>, var img_months_array: MutableList<String>, var city_list: MutableList<City>) : RecyclerView.Adapter<RecyclerViewFavAdapter.ViewHolder>()
 {


     private val favCityList = city_list
     private var favDB: FavDB? = null

     inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
     {

         var favTextView: TextView
         var favBtn: Button
         var favImageView: ImageView
         //quando clicco sulla citta starta un altra activity
         init
         {
             favTextView = itemView.findViewById(R.id.favTextView)
             favBtn = itemView.findViewById(R.id.favBtn)
             favImageView = itemView.findViewById(R.id.favImageView)
             itemView.setOnClickListener { v ->
                 val context: Context = v.context
                 val intent = Intent(context, CityDetailsActivity::class.java)

                 intent.putExtra("city", city_list[adapterPosition])

                 intent.putExtra("from_shake", false)

                 intent.putExtra("from_rv", true)


                 intent.putExtra("id", 1);
                 intent.putExtra("description", "send description to 2nd activity");
                 (context as Activity).startActivityForResult(intent, 1)
//
//                 intent.putExtra("id", "1");
//                 intent.putExtra("description", "send description to 2nd activity");
//                 (context as Activity).startActivityForResult(intent, 1)

                 //context.startActivity(intent)
             }


             //remove from fav after click
             favBtn.setOnClickListener {
                 val position = adapterPosition
                 val favCity: City = favCityList[position]
                 favDB!!.remove_fav(favCity.ID.toString())
                 removeItem(position)
             }

         }
     }

     private fun removeItem(position: Int) {
         favCityList.remove(favCityList[position])
         //months_array.remove(months_array[position])
         //img_months_array.remove(img_months_array[position])
         notifyItemRemoved(position)
         notifyItemRangeChanged(position, favCityList.size)
     }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(parent.context)
         val view = inflater.inflate(R.layout.fav_item, parent, false)
         favDB = FavDB(parent.context)
         return ViewHolder(view)
     }

     override fun onBindViewHolder(holder: RecyclerViewFavAdapter.ViewHolder, position: Int) {
         //holder.bind(months_array, img_months_array, position)

         if (favCityList.size>0) {


             holder.favTextView.text = favCityList[position].name
             var options = RequestOptions()
                     .placeholder(R.drawable.logo)
                     .centerCrop()
             //Glide.with(holder.favImageView.context).load(img_months_array[position]!!).apply(RequestOptions.bitmapTransform(BlurTransformation(50))).apply(options).into(holder.favImageView)
             Glide.with(holder.favImageView.context).load(favCityList[position].img_url).apply(options).into(holder.favImageView)
             //holder.favImageView.setImageResource(favCityList[position].img_url)
         }
     }

     override fun getItemCount(): Int {
         return favCityList.size
     }

//     override fun onActivityResult(requestCode: Int, resultCode: Int) {
//         notifyDataSetChanged()
//     }
    fun onActivityResult(requestCode: Int, resultCode: Int) {
        notifyDataSetChanged()
    }


 }
