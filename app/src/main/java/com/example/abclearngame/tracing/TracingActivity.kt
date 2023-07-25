package com.example.abclearngame.tracing

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.danny_jiang.tracinglibrary.view.TracingLetterView.TracingListener
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.MyApp
import com.example.abclearngame.R
import com.example.abclearngame.activity.BaseActivity
import com.example.abclearngame.databinding.ActivityTracingBinding
import com.example.abclearngame.extension.hide
import com.example.abclearngame.extension.visible
import com.example.abclearngame.utils.Constant
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.Locale


class TracingActivity : BaseActivity() {
    private val ROTATE_FROM = 30.0f
    private val ROTATE_TO = 360.0f
    var r: RotateAnimation? = null
    var letter = "A"
    var letterPosition = 0
    var bitmap: Bitmap? = null
    var tts: TextToSpeech? = null

    lateinit var binding: ActivityTracingBinding


    private val handler = Handler(Looper.getMainLooper())

    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTracingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)

        getBundleData()
        letterIntilize(letter)
        nextLetterShow()
        goBack()
        interstitialAd()
        binding.letter.setTracingListener(object : TracingListener {
            override fun onFinish() {
                /*Toast.makeText(
                    this@TracingActivity,
                    "tracing finished", Toast.LENGTH_SHORT
                ).show()*/

                binding.letter.hide()
                binding.imgLetter.visible()
                binding.lottiAnimation.visible()
                bitmap =
                    binding.letter.getBitmapByAssetName(Constant.getLetterPath(letter.toString()))
                binding.imgLetter.setImageBitmap(bitmap)
                rotateAnimation()
                if (session.isSoundOnOff) initializeTTS("$letter")


                if (letterPosition % 2 == 0) {
                    Toast.makeText(
                        this@TracingActivity,
                        "Showing Ad..",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(mainLooper).postDelayed(Runnable {
                        showingInterstitialAd()
                    }, 2000)

                }

                if (letter != "Z") {
                    binding.nextButton.visible()
                } else {
                    Toast.makeText(
                        this@TracingActivity,
                        "ABC Tracing Completed..",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onTracing(x: Float, y: Float) {
                Log.e("ABC", "tracing x : $x y : $y")
            }


        })
        loadBannerAd()

    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun showingInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this@TracingActivity)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun interstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, getString(R.string.interstitial_id), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    //Log.i(TAG, "onAdLoaded")
                }


                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    //  Log.d(TAG, loadAdError.toString())
                    mInterstitialAd = null
                }
            })


    }

    private fun goBack() {
        binding.imgClose.setOnClickListener {
            finish()
            Animatoo.animateFade(this)
        }
    }

    private fun letterIntilize(letter: String) {
        binding.letter.setLetterChar(letter)
        binding.letter.setPointColor(Color.BLUE)
        binding.letter.setInstructMode(true)
    }

    private fun nextLetterShow() {
        binding.nextButton.setOnClickListener {
            letterPosition++
            Log.d("position", "" + letterPosition)
            Log.d("LetterListSize", "" + Constant.LettersList.size)

            if (Constant.LettersList.size > letterPosition) {
                val newLetter = Constant.LettersList[letterPosition]
                letterIntilize(newLetter)
                val i = Intent(this, TracingActivity::class.java)
                val bundle = Bundle()
                bundle.putString(Constant.LETTERS, newLetter)
                bundle.putInt(Constant.LETTER_POSITION, letterPosition)
                i.putExtra(Constant.DATA, bundle)
                startActivity(i)
                Animatoo.animateZoom(this)
                finish()
            } else {
                Toast.makeText(this, "End....", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initializeTTS(text: String) {
        tts = TextToSpeech(this) { status ->
            Thread {
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.US)
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED
                    ) {
                        Log.e("error", "This Language is not supported")
                    } else {
                        tts?.speak(
                            text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
                        )

                        tts?.setOnUtteranceProgressListener(object :
                            UtteranceProgressListener() {
                            override fun onStart(utteranceId: String) {
                                stopMusic()

                                Log.i("TextToSpeech", "On Start")
                            }

                            override fun onDone(utteranceId: String) {
                                Log.i("TextToSpeech", "On Done")
                              startMusic()


                            }

                            override fun onError(utteranceId: String) {
                                Log.i("TextToSpeech", "On Error")
                            }
                        })
                    }
                } else
                    Log.e("error", "Initialization Failed!")

            }.start()
        }
    }

    private fun getBundleData() {
        val bundle = intent.extras?.getBundle(Constant.DATA)
        letter = bundle?.getString(Constant.LETTERS).toString()
        letterPosition = bundle?.getInt(Constant.LETTER_POSITION, 0)!!
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bitmap != null && !bitmap!!.isRecycled) {
            bitmap!!.recycle()
            bitmap = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()

        }
        if (bitmap != null && !bitmap!!.isRecycled) {
            bitmap!!.recycle()
            bitmap = null
        }
    }




    private fun rotateAnimation() {
        r = RotateAnimation(
            ROTATE_FROM,
            ROTATE_TO,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        r!!.duration = 4L * 500
        r!!.repeatCount = 1
        binding.imgLetter.startAnimation(r)
    }

    private fun startMusic(){
        MyApp.mp?.seekTo(MyApp.length)
        MyApp.mp?.start()
    }
    private fun  stopMusic(){
        MyApp.mp?.pause()
    }
}