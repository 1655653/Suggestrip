package com.example.suggestrip
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_layout.*
import kotlinx.android.synthetic.main.row_layout.view.*
import com.bumptech.glide.Glide

//qui creo l'adapter ma sopratutto la classe ViewHolder che mi serve per sistemare tutti gli item della cardview
class RecyclerViewExploreAdapter (var months_array: Array<String>,var img_months_array: Array<Int>) : RecyclerView.Adapter<RecyclerViewExploreAdapter.ViewHolder>()
 {
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(parent.context)
         val view = inflater.inflate(R.layout.row_layout,parent,false)
         return ViewHolder(view)
     }

     override fun onBindViewHolder(holder: RecyclerViewExploreAdapter.ViewHolder, position: Int) {
         holder.bind(months_array,img_months_array,position)
     }

     override fun getItemCount(): Int {
         return months_array.size
     }
     inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
     {
         fun bind( months_array: Array<String>, img_months_array: Array<Int>,position: Int){
             itemView.tv.text = months_array[position]
             Glide.with(itemView.context).load(img_months_array[position]!!).into(itemView.imageButton)
         }
         //quando clicco sul mese toast
         init
         {
             itemView.setOnClickListener {
                 Toast.makeText(itemView.context, months_array[adapterPosition], Toast.LENGTH_SHORT).show()
             }
         }


     }
 }
