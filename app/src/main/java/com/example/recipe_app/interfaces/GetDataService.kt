package com.example.recipe_app.interfaces

import retrofit2.Call
import com.example.recipe_app.entities.Category
import retrofit2.http.GET

interface GetDataService {
    @GET("categories.php")
    fun getCategoryList(): Call<Category>
}