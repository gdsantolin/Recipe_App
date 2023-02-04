package com.example.recipe_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipe_app.R
import com.example.recipe_app.databinding.ItemRvMainCategoryBinding
import com.example.recipe_app.entities.CategoryItems
import com.example.recipe_app.entities.Recipes

class MainCategoryAdapter: RecyclerView.Adapter<MainCategoryAdapter.RecipeViewHolder>() {

    var ctx: Context? = null
    var arrMainCategory = ArrayList<CategoryItems>()

    class RecipeViewHolder(val binding: ItemRvMainCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(arrData : List<CategoryItems>){
        arrMainCategory = arrData as ArrayList<CategoryItems>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        val binding = ItemRvMainCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrMainCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.binding.tvDishName.text = arrMainCategory[position].strcategory
        Glide.with(ctx!!).load(arrMainCategory[position].strcategorythumb).into(holder.binding.imgDish)
    }
}
