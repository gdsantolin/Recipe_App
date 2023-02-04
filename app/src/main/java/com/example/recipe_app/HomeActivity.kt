package com.example.recipe_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe_app.adapter.MainCategoryAdapter
import com.example.recipe_app.adapter.SubCategoryAdapter
import com.example.recipe_app.database.RecipeDatabase
import com.example.recipe_app.databinding.ActivityHomeBinding
import com.example.recipe_app.databinding.ActivitySplashBinding
import com.example.recipe_app.entities.CategoryItems
import com.example.recipe_app.entities.Recipes
import kotlinx.coroutines.launch
import java.util.Locale.Category

class HomeActivity : BaseActivity() {
    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<Recipes>()

    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_home)

        getDataFromDb()

        arrSubCategory.add(Recipes(1, "Beef & mustard pie"))
        arrSubCategory.add(Recipes(2, "Chicken and mushroom hotpot"))
        arrSubCategory.add(Recipes(3, "Banana pancakes"))
        arrSubCategory.add(Recipes(4, "Kapsalon"))

        subCategoryAdapter.setData(arrSubCategory)


        binding.rvSubCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSubCategory.adapter = subCategoryAdapter

    }

    private fun getDataFromDb(){
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
                arrMainCategory = cat as ArrayList<CategoryItems>
                arrMainCategory.reverse()

                mainCategoryAdapter.setData(arrMainCategory)

                binding.rvMainCategory.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.rvMainCategory.adapter = mainCategoryAdapter
            }


        }
    }

}