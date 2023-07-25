package com.example.abclearngame.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.MyApp
import com.example.abclearngame.databinding.ActivitySettingsBinding


class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)

        binding.switchMusic.isOn = session.isMusicOnOff
        binding.switchAudio.isOn = session.isSoundOnOff

        binding.switchMusic.setOnToggledListener { _, isOn ->
            Log.d("isOn", "$isOn")
            if (isOn) {
               startMusic()
            } else {
              stopMusic()
            }
            session.isMusicOnOff = isOn
        }
        binding.switchAudio.setOnToggledListener { _, isOn ->
            Log.d("isOn", "$isOn")
            session.isSoundOnOff = isOn
        }

        binding.imgClose.setOnClickListener {
            finish()
        }

    }

   private fun startMusic(){
       MyApp.mp?.seekTo(MyApp.length)
       MyApp.mp?.start()
   }
  private fun  stopMusic(){
      MyApp.mp?.pause()
    }
}