package com.example.recipe_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


open class BaseActivity : AppCompatActivity(),CoroutineScope{

    //private lateinit var binding: ActivityBaseBinding

    private lateinit var job: Job
    override val coroutineContext:CoroutineContext
    get() = job +Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        //binding = ActivitySplashBinding.inflate(layoutInflater)
        //val view = binding.root
        //setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}