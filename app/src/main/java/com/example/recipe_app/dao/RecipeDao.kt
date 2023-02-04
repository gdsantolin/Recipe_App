package com.example.recipe_app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipe_app.entities.Category
import com.example.recipe_app.entities.CategoryItems
import com.example.recipe_app.entities.Recipes

@Dao
interface RecipeDao {
    @Query("SELECT * FROM categoryitems ORDER BY id DESC")
    suspend fun getAllCategory() : List<CategoryItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryItems: CategoryItems?)

    @Query("DELETE FROM categoryitems")
    suspend fun clearDb()
}