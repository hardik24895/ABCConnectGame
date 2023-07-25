package com.example.abclearngame.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.databinding.ActivityLoadingBinding


class LoadingScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    private var SPLASH_TIME = 4000L //This is 4 seconds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)
        binding.progressBar1.animateProgress(4000, 0, 100)

        Handler(mainLooper).postDelayed(Runnable { //Do any action here. Now we are moving to next page
            goToActivityAndClearTask<HomeActivity>()
        }, SPLASH_TIME)

    }
}