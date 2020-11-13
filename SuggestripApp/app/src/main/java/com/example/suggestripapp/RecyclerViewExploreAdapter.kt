package com.example.suggestripapp
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.row_layout.view.*


//qui creo l'adapter ma sopratutto la classe ViewHolder che mi serve per sistemare tutti gli item della cardview
class RecyclerViewExploreAdapter(var months_array: Array<String>, var img_months_array: Array<String>) : RecyclerView.Adapter<RecyclerViewExploreAdapter.ViewHolder>()
 {
     inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
     {
         fun bind(months_array: Array<String>, img_months_array: Array<String>, position: Int){
             itemView.tv_city_name_rv.text = months_array[position]

             var options = RequestOptions()
                     .placeholder(R.drawable.logo)
                     .centerCrop()

             Glide.with(itemView.context).load("https://" + img_months_array[position]!!).apply(RequestOptions.bitmapTransform(BlurTransformation(50))).apply(options).into(itemView.ib_city)
             Log.d("pocamadonna", img_months_array[position])
             //Picasso.get().load(img_months_array[position]).into(itemView.ib_city);
         }
         //quando clicco sulla citta starta un altra activity
         init
         {
             itemView.setOnClickListener { v ->
                 val context: Context = v.context
                 val intent = Intent(context, CityDetailsActivity::class.java)
                 intent.putExtra("city_name", itemView.tv_city_name_rv.text)
                 intent.putExtra("city_photo_url", img_months_array[adapterPosition])
                 intent.putExtra("from_shake", "false")
                 context.startActivity(intent)
             }
         }
     }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(parent.context)
         val view = inflater.inflate(R.layout.row_layout, parent, false)
         return ViewHolder(view)
     }

     override fun onBindViewHolder(holder: RecyclerViewExploreAdapter.ViewHolder, position: Int) {
         holder.bind(months_array, img_months_array, position)

     }

     override fun getItemCount(): Int {
         return months_array.size
     }

 }
