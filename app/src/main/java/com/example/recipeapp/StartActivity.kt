package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.entities.Category
import com.example.recipeapp.entities.Meal
import com.example.recipeapp.entities.ItemsMeal
import com.example.recipeapp.interfaces.GetDataService
import com.example.recipeapp.retrofitclient.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : BaseActivity(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {
    private var READ_STORAGE_PERM = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //cria a base de dados a partir da API e insere os dados das categorias e pratos
        readStorageTask()

        //tratamento do evento de clique
        btnGetStarted.setOnClickListener {
            var intent = Intent(this@StartActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun getCategories() {

        //enviando requisicao GET das categorias
        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getCategoryList()

        call.enqueue(object : Callback<Category> {
            override fun onFailure(call: Call<Category>, t: Throwable) {
                Toast.makeText(this@StartActivity, "Algo deu errado", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Category>,
                response: Response<Category>
            ) {
                for (arr in response.body()!!.categoryitems!!) {
                    getMeal(arr.strcategory)
                }
                insertDataIntoRoomDb(response.body())
            }

        })
    }

    fun getMeal(categoryName: String) {
        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getMealList(categoryName)
        call.enqueue(object : Callback<Meal> {
            override fun onFailure(call: Call<Meal>, t: Throwable) {
                loader.visibility = View.INVISIBLE
                Toast.makeText(this@StartActivity, "Algo deu errado", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Meal>,
                response: Response<Meal>
            ) {
                insertMealDataIntoRoomDb(categoryName, response.body())
            }

        })
    }

    fun insertDataIntoRoomDb(category: Category?) {
        launch {
            this.let {
                for (arr in category!!.categoryitems!!)
                    RecipeDatabase.getDatabase(this@StartActivity).recipeDao().insertCategory(arr)
            }
        }
    }

    fun insertMealDataIntoRoomDb(categoryName: String, meal: Meal?) {
        launch {
            this.let {
                for (arr in meal!!.mealsItem!!) {
                    var mealItemModel = ItemsMeal(
                        arr.id,
                        arr.idMeal,
                        categoryName,
                        arr.strMeal,
                        arr.strMealThumb,
                        false
                    )
                    RecipeDatabase.getDatabase(this@StartActivity).recipeDao().insertMeal(mealItemModel)
                    Log.d("mealData", arr.toString())
                }
                //torna o botao de get started visivel apos criar a base de dados
                btnGetStarted.visibility = View.VISIBLE
            }
        }


    }

    fun clearDatabase() {
        launch {
            this.let {
                RecipeDatabase.getDatabase(this@StartActivity).recipeDao().clearDb()
            }
        }
    }

    private fun hasReadStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    //obter as categorias para inseri-las na base de dados
    private fun readStorageTask() {
        if (hasReadStoragePermission()) {
            clearDatabase()
            getCategories()
        }
        //permissao de acesso negada pelo usuario
        else {
            EasyPermissions.requestPermissions(
                this,
                "Esse app precisa acessar seu armazenamento",
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
    }

    override fun onRationaleDenied(requestCode: Int) {}
    override fun onRationaleAccepted(requestCode: Int) {}
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}
}