package com.example.abclearngame


import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.abclearngame.utils.SessionManager


class MyApp :Application() {

    var isAppInForground : Boolean = true
    lateinit var session :SessionManager



    companion object{
        var mp : MediaPlayer?=null
        var length =0
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)
        mp = MediaPlayer.create(this, R.raw.bgmusic)
        mp?.isLooping = true
        session = SessionManager(this)

    }
    private var lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                length = mp!!.currentPosition
                isAppInForground = false
                if (mp != null && mp!!.isPlaying)
                { mp!!.pause()
                }

                println("AppStop")
                //your code here
            }
            Lifecycle.Event.ON_START -> {
                isAppInForground = true
                println("AppStart")
                if (session.isMusicOnOff){
                    mp!!.seekTo(length)
                    mp!!.start()
                }

                //your code here
            }

            else -> {}
        }
    }

}