package com.example.recipeapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.adapter.CategoriesAdapter
import com.example.recipeapp.adapter.MealsAdapter
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.entities.ItemsCategory
import com.example.recipeapp.entities.ItemsMeal
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch


class HomeActivity : BaseActivity() {
    //lista de categorias e lista de pratos
    var arrCategories = ArrayList<ItemsCategory>()
    var arrMeals = ArrayList<ItemsMeal>()
    var arrFavorites = ArrayList<ItemsMeal>()

    var arrCategoriesAdapter = CategoriesAdapter()
    var arrMealsAdapter = MealsAdapter()
    var arrFavoritesAdapter = MealsAdapter()

    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //obtem as categorias a serem exibidas e por padrao mostra
        getDataFromDb()
        getMealListFavorites()

        //setando os tratadores de eventos
        arrCategoriesAdapter.setClickListener(onClicked)
        arrMealsAdapter.setClickListener(onClickedSubItem)

        searchView = findViewById(R.id.idSV)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getMealListFromSearch(query)
                if (!query.isNullOrBlank())
                    getMealListFromSearch(query)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    //exibe os pratos da categoria clicada
    private val onClicked = object : CategoriesAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }
    }

    //vai para a tela de descricao do prato clicado
    private val onClickedSubItem  = object : MealsAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@HomeActivity,DetailActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }


    //obter todas as categorias
    private fun getDataFromDb(){
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
                arrCategories = cat as ArrayList<ItemsCategory>
                arrCategories.reverse()

                //exibir uma categoria inicial por padrao
                getMealDataFromDb(arrCategories[0].strcategory)
                arrCategoriesAdapter.setData(arrCategories)

                rv_categories.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_categories.adapter = arrCategoriesAdapter
            }
        }
    }

    //obter todos os pratos de uma categoria
    private fun getMealDataFromDb(categoryName: String){
        tvCategory.text = "$categoryName Category"
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName)
                arrMeals = cat as ArrayList<ItemsMeal>
                arrMealsAdapter.setData(arrMeals)

                rv_meals.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_meals.adapter = arrMealsAdapter
            }
        }
    }

    private fun getMealListFromSearch(search: String){
        tvCategory.text = "Search for \"$search\""
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealListFromSearch(search)
                arrMeals = cat as ArrayList<ItemsMeal>
                arrMealsAdapter.setData(arrMeals)

                rv_meals.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_meals.adapter = arrMealsAdapter
            }
        }
    }

    private fun getMealListFavorites(){
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllFavorites()
                arrFavorites = cat as ArrayList<ItemsMeal>
                arrFavoritesAdapter.setData(arrFavorites)

                rv_favorites.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_favorites.adapter = arrFavoritesAdapter
            }
        }
    }
}