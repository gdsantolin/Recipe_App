package com.example.recipe_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipe_app.dao.RecipeDao
import com.example.recipe_app.entities.Category
import com.example.recipe_app.entities.CategoryItems
import com.example.recipe_app.entities.Recipes
import com.example.recipe_app.entities.converter.CategoryListConverter

@Database(entities = [Recipes::class,CategoryItems::class,Category::class], version = 1, exportSchema = false)
@TypeConverters(CategoryListConverter::class)
abstract class RecipeDatabase: RoomDatabase() {
    companion object{
        var recipesDatabase:RecipeDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): RecipeDatabase{
            if(recipesDatabase == null){
                recipesDatabase = Room.databaseBuilder(
                    context,
                    RecipeDatabase::class.java,
                    "recipe.db"
                ).build()
            }
            return recipesDatabase!!
        }
    }

    abstract fun recipeDao(): RecipeDao
}