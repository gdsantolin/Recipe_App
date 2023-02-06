package com.example.recipeapp.entities.converter

import androidx.room.TypeConverter
import com.example.recipeapp.entities.ItemsCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryListConverter {

    @TypeConverter
    fun fromCategoryList(category: List<ItemsCategory>):String?{
        if (category == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object : TypeToken<ItemsCategory>(){

            }.type
            return gson.toJson(category,type)
        }
    }

    @TypeConverter
    fun toCategoryList ( categoryString: String):List<ItemsCategory>?{
        if (categoryString == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object :TypeToken<ItemsCategory>(){

            }.type
            return  gson.fromJson(categoryString,type)
        }
    }
}