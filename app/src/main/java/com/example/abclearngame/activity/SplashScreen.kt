package com.example.abclearngame.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashScreen :AppCompatActivity() {

     private lateinit var binding: ActivitySplashBinding
    private var SPLASH_TIME = 2000L //This is 3 seconds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)
        Handler(mainLooper).postDelayed(Runnable { //Do any action here. Now we are moving to next page
            goToActivityAndClearTask<LoadingScreen>()
        }, SPLASH_TIME)

    }
}