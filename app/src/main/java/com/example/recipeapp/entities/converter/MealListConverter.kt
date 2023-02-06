package com.example.recipeapp.entities.converter

import androidx.room.TypeConverter
import com.example.recipeapp.entities.ItemsMeal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MealListConverter {
    @TypeConverter
    fun fromMealList(category: List<ItemsMeal>):String?{
        if (category == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object : TypeToken<ItemsMeal>(){

            }.type
            return gson.toJson(category,type)
        }
    }

    @TypeConverter
    fun toMealList ( categoryString: String):List<ItemsMeal>?{
        if (categoryString == null){
            return (null)
        }else{
            val gson = Gson()
            val type = object :TypeToken<ItemsMeal>(){

            }.type
            return  gson.fromJson(categoryString,type)
        }
    }
}