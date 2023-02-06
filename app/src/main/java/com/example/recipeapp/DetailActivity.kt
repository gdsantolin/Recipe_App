package com.example.recipeapp


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.entities.MealResponse
import com.example.recipeapp.interfaces.GetDataService
import com.example.recipeapp.retrofitclient.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : BaseActivity() {

    var urlYt = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var id = intent.getStringExtra("id")
        getSpecificItem(id!!)

        //voltar à tela anterior
        imgToolbarBtnBack.setOnClickListener {
            var intent = Intent(this@DetailActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        //ir ao link do youtube
        btnYoutube.setOnClickListener {
            val uri = Uri.parse(urlYt)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


        imgToolbarBtnFav.setOnClickListener {
            launch {
                this.let {
                    val meal = RecipeDatabase.getDatabase(this@DetailActivity).recipeDao().getSpecificMeal(id)
                    if (meal != null) {
                        if(meal.isFavorite){
                            meal.isFavorite = false
                            Toast.makeText(this@DetailActivity, "Prato removido dos favoritos", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            meal.isFavorite = true
                            Toast.makeText(this@DetailActivity, "Prato adicionado aos favoritos", Toast.LENGTH_SHORT).show()
                        }
                        RecipeDatabase.getDatabase(this@DetailActivity).recipeDao().updateMeal(meal)
                    }
                }
            }
        }
    }

    fun getSpecificItem(id:String) {
        //enviando requisicao GET do prato
        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getSpecificItem(id)

        //executa a chamada de forma assincrona
        call.enqueue(object : Callback<MealResponse> {
            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Algo deu errado", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<MealResponse>,
                response: Response<MealResponse>
            ) {
                //carregando imagem e categoria do prato
                Glide.with(this@DetailActivity).load(response.body()!!.mealsEntity[0].strmealthumb).into(imgItem)
                tvCategory.text = response.body()!!.mealsEntity[0].strmeal

                //organizando lista de ingredientes e medidas
                var ingredients = listOf(
                    response.body()!!.mealsEntity[0].stringredient1,
                    response.body()!!.mealsEntity[0].stringredient2,
                    response.body()!!.mealsEntity[0].stringredient3,
                    response.body()!!.mealsEntity[0].stringredient4,
                    response.body()!!.mealsEntity[0].stringredient5,
                    response.body()!!.mealsEntity[0].stringredient6,
                    response.body()!!.mealsEntity[0].stringredient7,
                    response.body()!!.mealsEntity[0].stringredient8,
                    response.body()!!.mealsEntity[0].stringredient9,
                    response.body()!!.mealsEntity[0].stringredient10,
                    response.body()!!.mealsEntity[0].stringredient11,
                    response.body()!!.mealsEntity[0].stringredient12,
                    response.body()!!.mealsEntity[0].stringredient13,
                    response.body()!!.mealsEntity[0].stringredient14,
                    response.body()!!.mealsEntity[0].stringredient15,
                    response.body()!!.mealsEntity[0].stringredient16,
                    response.body()!!.mealsEntity[0].stringredient17,
                    response.body()!!.mealsEntity[0].stringredient18,
                    response.body()!!.mealsEntity[0].stringredient19,
                    response.body()!!.mealsEntity[0].stringredient20
                )
                var measures = listOf(
                    response.body()!!.mealsEntity[0].strmeasure1,
                    response.body()!!.mealsEntity[0].strmeasure2,
                    response.body()!!.mealsEntity[0].strmeasure3,
                    response.body()!!.mealsEntity[0].strmeasure4,
                    response.body()!!.mealsEntity[0].strmeasure5,
                    response.body()!!.mealsEntity[0].strmeasure6,
                    response.body()!!.mealsEntity[0].strmeasure7,
                    response.body()!!.mealsEntity[0].strmeasure8,
                    response.body()!!.mealsEntity[0].strmeasure9,
                    response.body()!!.mealsEntity[0].strmeasure10,
                    response.body()!!.mealsEntity[0].strmeasure11,
                    response.body()!!.mealsEntity[0].strmeasure12,
                    response.body()!!.mealsEntity[0].strmeasure13,
                    response.body()!!.mealsEntity[0].strmeasure14,
                    response.body()!!.mealsEntity[0].strmeasure15,
                    response.body()!!.mealsEntity[0].strmeasure16,
                    response.body()!!.mealsEntity[0].strmeasure17,
                    response.body()!!.mealsEntity[0].strmeasure18,
                    response.body()!!.mealsEntity[0].strmeasure19,
                    response.body()!!.mealsEntity[0].strmeasure20
                )

                //tratando variaveis vazias de ingredientes e medidas
                var tvIngredientList = ""
                for (i in ingredients.indices) {
                    if (ingredients[i].trim().isNotEmpty())
                        tvIngredientList += "${ingredients[i]}"
                    if (measures[i].trim().isNotEmpty())
                        tvIngredientList += " - ${measures[i]}"
                    if (ingredients[i].trim().isNotEmpty() && measures[i].trim().isNotEmpty())
                        tvIngredientList += "\n"
                }

                //carregando lista de ingredientes e instrucoes
                tvIngredients.text = tvIngredientList
                tvInstructions.text = response.body()!!.mealsEntity[0].strinstructions

                //exibir botao para o youtube caso haja link
                if (!response.body()!!.mealsEntity[0].strsource.trim().isEmpty())
                    urlYt = response.body()!!.mealsEntity[0].strsource
                else
                    btnYoutube.visibility = View.GONE

            }
        })
    }
}