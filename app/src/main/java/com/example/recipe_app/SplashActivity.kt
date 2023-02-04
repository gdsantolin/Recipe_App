package com.example.recipe_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.recipe_app.dao.RecipeDao
import com.example.recipe_app.database.RecipeDatabase
import com.example.recipe_app.databinding.ActivitySplashBinding
import com.example.recipe_app.entities.Category
import com.example.recipe_app.interfaces.GetDataService
import com.example.recipe_app.retrofitclient.RetrofitClientInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest
import retrofit2.Call

class SplashActivity : BaseActivity(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivitySplashBinding
    private var READ_STORAGE_PERM = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        readStorageTask()

        binding.btnGetStarted.setOnClickListener {
            var intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun getCategories(){
        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getCategoryList()
        call.enqueue(object : Callback<Category>{
            override fun onFailure(call: Call<Category>, t: Throwable) {
                //binding.loader.visibility = View.INVISIBLE
                Toast.makeText(this@SplashActivity, "Algo deu errado", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Category>,
                response: Response<Category>
            ) {
                insertDataIntoRoomDb(response.body())
            }

        })
    }

    fun insertDataIntoRoomDb(category: Category?){
        launch {
            this.let {
                //RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().clearDb()
                for(arr in category!!.categorieitems!!){
                    RecipeDatabase.getDatabase(this@SplashActivity)
                        .recipeDao().insertCategory(arr)
                }

                //binding.btnGetStarted.visibility = View.VISIBLE
            }
        }
    }

    private fun hasReadStoragePermission():Boolean{
        return EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask(){
        if(hasReadStoragePermission()){
           getCategories()
        }
        else{
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
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }


}