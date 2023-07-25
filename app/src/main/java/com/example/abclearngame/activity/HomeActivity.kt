package com.example.abclearngame.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.eisuchi.extention.goToActivity
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.ballon.BalloonPopActivity
import com.example.abclearngame.databinding.ActivityHomeBinding
import com.example.abclearngame.matching.MatchingActivity
import com.example.abclearngame.tracing.LettersListActivity

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.imgTracing.setOnClickListener {
            goToActivity<LettersListActivity>()
        }
        binding.imgBalloon.setOnClickListener {
            goToActivity<BalloonPopActivity>()
        }
        binding.imgConnect.setOnClickListener {
            goToActivity<MatchingActivity>()
        }

        binding.conRate.setOnClickListener {
            /*try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            } */
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/games")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/games")
                    )
                )
            }
        }

        binding.conShare.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                    ${shareMessage + "https://play.google.com/store/apps/details?id=" + packageName}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }

        binding.conSetting.setOnClickListener {
            goToActivity<SettingActivity>()
        }

    }

    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


}