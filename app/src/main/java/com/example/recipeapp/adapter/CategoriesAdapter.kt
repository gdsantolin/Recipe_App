package com.example.recipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.entities.ItemsCategory
import kotlinx.android.synthetic.main.item_rv_categories.view.*

class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.RecipeViewHolder>() {

    var listener: OnItemClickListener? = null
    var ctx: Context? = null
    var arrCategories = ArrayList<ItemsCategory>()

    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){}

    fun setData(arrData : List<ItemsCategory>){
        arrCategories = arrData as ArrayList<ItemsCategory>
    }

    fun setClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_categories,parent,false))
    }

    override fun getItemCount(): Int {
        return arrCategories.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.itemView.tv_dish_name.text = arrCategories[position].strcategory
        Glide.with(ctx!!).load(arrCategories[position].strcategorythumb).into(holder.itemView.img_dish)
        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(arrCategories[position].strcategory)
        }
    }

    interface OnItemClickListener{
        fun onClicked(categoryName:String)
    }
}