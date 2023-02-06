package com.example.recipeapp.dao

import androidx.room.*
import com.example.recipeapp.entities.ItemsCategory
import com.example.recipeapp.entities.ItemsMeal
import com.example.recipeapp.entities.Meal

@Dao
interface RecipeDao {

    @Query("SELECT * FROM categoryitems ORDER BY id DESC")
    suspend fun getAllCategory() : List<ItemsCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(itemsCategory: ItemsCategory?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(itemsMeal: ItemsMeal?)

    @Query("DELETE FROM categoryitems")
    suspend fun clearDb()

    @Query("SELECT * FROM MealItems WHERE categoryName = :categoryName ORDER BY id DESC")
    suspend fun getSpecificMealList(categoryName:String) : List<ItemsMeal>

    @Query("SELECT DISTINCT * FROM MealItems WHERE strmeal LIKE '%' || :search || '%' ORDER BY id DESC")
    suspend fun getSpecificMealListFromSearch(search:String) : List<ItemsMeal>

    @Query("SELECT * FROM MealItems WHERE idMeal = :idMeal")
    suspend fun getSpecificMeal(idMeal: String): ItemsMeal

    @Update
    suspend fun updateMeal(meal: ItemsMeal)

    @Query("SELECT * FROM MealItems WHERE isFavorite = 1")
    suspend fun getAllFavorites(): List<ItemsMeal>

}