package com.example.recipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.entities.ItemsMeal
import kotlinx.android.synthetic.main.item_rv_categories.view.*

class MealsAdapter: RecyclerView.Adapter<MealsAdapter.RecipeViewHolder>() {

    var listener: MealsAdapter.OnItemClickListener? = null
    var ctx :Context? = null
    var arrMeals = ArrayList<ItemsMeal>()

    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){}

    fun setData(arrData : List<ItemsMeal>){
        arrMeals = arrData as ArrayList<ItemsMeal>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_meals,parent,false))
    }

    override fun getItemCount(): Int {
        return if (arrMeals.size == 0) 0 else arrMeals.size
    }

    fun setClickListener(listener1: MealsAdapter.OnItemClickListener){
        listener = listener1
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.itemView.tv_dish_name.text = arrMeals[position].strMeal
        Glide.with(ctx!!).load(arrMeals[position].strMealThumb).into(holder.itemView.img_dish)
        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(arrMeals[position].idMeal)
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}