package com.example.abclearngame.matching


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.MyApp
import com.example.abclearngame.R
import com.example.abclearngame.activity.BaseActivity
import com.example.abclearngame.databinding.ActivityMatchingBinding
import com.example.abclearngame.extension.hide
import com.example.abclearngame.extension.visible
import com.example.abclearngame.utils.AppUtils
import com.example.abclearngame.utils.AppUtils.letterToSpeak
import com.example.abclearngame.utils.Constant
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.Locale


class MatchingActivity : BaseActivity() {
    private lateinit var lineView: LineView
    lateinit var binding: ActivityMatchingBinding
    val chunkSize = 3
    val chunked = Constant.LettersList.chunked(chunkSize)
    var letterList = listOf<String>()
    var picList = listOf<String>()
    private var mInterstitialAd: InterstitialAd? = null
    var tts: TextToSpeech? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToFullScreen(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        interstitialAd()
        lineView = binding.lineview
        var currentListChunkPos = intent.getIntExtra("startPos", 0)
        letterList = chunked[currentListChunkPos]
        if (letterList.size == 2) binding.imgNext.hide()
        picList = letterList.shuffled()

        for (i in letterList.indices) {
            getLetterImage(i, letterList[i])
        }
        for (i in picList.indices) {
            getPicImage(i, picList[i])
        }

        binding.imgClose.setOnClickListener {
            finish()
            Animatoo.animateFade(this)
        }
        binding.imgNext.setOnClickListener {
            val size = chunked.size
            Log.d("chunkSize", "$size")
            if (chunked.size >= currentListChunkPos + 2) {
                currentListChunkPos++
                val i = Intent(this, MatchingActivity::class.java)
                i.putExtra("startPos", currentListChunkPos)
                startActivity(i)
                Animatoo.animateShrink(this)
                finish()
            } else {
                binding.imgNext.hide()
                Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show()
            }


        }

        image1Touch()
        image2Touch()
        image3Touch()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun image1Touch() {
        binding.img1.setOnTouchListener { _, event ->
            val imageCoordinates = getCenterCoordinates(binding.img1)
            val first = imageCoordinates.first
            val second = imageCoordinates.second
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lineView.startCoordinates = imageCoordinates
                    lineView.endCoordinates = null
                    lineView.invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    lineView.endCoordinates = Pair(event.x + first, event.y + second)
                    Log.d("Move", "${first + event.x}, ${second + event.y}")
                    lineView.invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    val x = lineView.endCoordinates?.first!!
                    val y = lineView.endCoordinates?.second!!
                    Log.d("UP", "$x, $y")
                    endLineDraw(binding.img1, x, y)

                    lineView.invalidate()


                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun image2Touch() {
        binding.img2.setOnTouchListener { _, event ->
            val imageCordinates = getCenterCoordinates(binding.img2)
            val first = imageCordinates.first
            val second = imageCordinates.second
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lineView.startCoordinates = imageCordinates
                    lineView.endCoordinates = null
                    lineView.invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    lineView.endCoordinates = Pair(event.x + first, event.y + second)
                    lineView.invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    val x = lineView.endCoordinates?.first!!
                    val y = lineView.endCoordinates?.second!!
                    endLineDraw(binding.img2, x, y)
                    lineView.invalidate()
                    // validateConnection()

                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun image3Touch() {
        binding.img3.setOnTouchListener { _, event ->
            val image1 = binding.img3.contentDescription
            val imageCordinates = getCenterCoordinates(binding.img3)
            val first = imageCordinates.first
            val second = imageCordinates.second
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lineView.startCoordinates = getCenterCoordinates(binding.img3)
                    lineView.endCoordinates = null
                    lineView.invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    lineView.endCoordinates = Pair(event.x + first, event.y + second)
                    lineView.invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    val x = lineView.endCoordinates?.first!!
                    val y = lineView.endCoordinates?.second!!
                    endLineDraw(binding.img3, x, y)
                    lineView.invalidate()
                    // validateConnection()

                }
            }
            true
        }
    }

    private fun showingInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
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

    private fun endLineDraw(letterImage: ImageView, x: Float, y: Float) {
        val hitRect3 = Rect()
        shakeAnimation(letterImage, x, y)
        when (letterImage.contentDescription) {

            binding.pic1.contentDescription -> connectionValidation(
                letterImage,
                hitRect3,
                x,
                y,
                binding.pic1
            )

            binding.pic2.contentDescription -> connectionValidation(
                letterImage,
                hitRect3,
                x,
                y,
                binding.pic2
            )

            else -> connectionValidation(letterImage, hitRect3, x, y, binding.pic3)

        }


    }

    private fun connectionValidation(
        letterImage: ImageView,
        hitRect3: Rect,
        x: Float,
        y: Float,
        picImageView: ImageView
    ) {
        picImageView.getHitRect(hitRect3)
        Log.d("pictureCoordinate", "$hitRect3")
        val isTouched = hitRect3.contains(x.toInt(), y.toInt())

        if (isTouched) {
            letterImage.isEnabled = false
            lineView.endCoordinates = getCenterCoordinates(picImageView)
            val line = LineView.LineList(lineView.startCoordinates, lineView.endCoordinates)
            lineView.lineList.add(line)
            if (lineView.lineList.size == 3) {
                Toast.makeText(
                    this,
                    "Showing Ad..",
                    Toast.LENGTH_SHORT
                ).show()
                showingInterstitialAd()
                binding.imgNext.visible()
            }
            if (session.isSoundOnOff) initializeTTS(letterToSpeak(letterImage.contentDescription.toString()))
            // Toast.makeText(this, "Connection successful!", Toast.LENGTH_SHORT).show()
        } else {
            lineView.endCoordinates = null

            // Toast.makeText(this, "Connection unsuccessful!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shakeAnimation(image: ImageView, x: Float, y: Float) {
        val hitRect1 = Rect()
        val hitRect2 = Rect()
        val hitRect3 = Rect()

        binding.pic1.getHitRect(hitRect1)
        binding.pic2.getHitRect(hitRect2)
        binding.pic3.getHitRect(hitRect3)

        val isTouchedPic1 = hitRect1.contains(x.toInt(), y.toInt())
        val isTouchedPic2 = hitRect2.contains(x.toInt(), y.toInt())
        val isTouchedPic3 = hitRect3.contains(x.toInt(), y.toInt())

        if (isTouchedPic1 && image.contentDescription != binding.pic1.contentDescription)
            binding.pic1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))

        if (isTouchedPic2 && image.contentDescription != binding.pic2.contentDescription)
            binding.pic2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))

        if (isTouchedPic3 && image.contentDescription != binding.pic3.contentDescription)
            binding.pic3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))

    }


    private fun getCenterCoordinates(view: View): Pair<Float, Float> {
        val x = view.x + view.width / 2
        val y = view.y + view.height / 2
        return Pair(x, y)
    }

    private fun getLetterImage(position: Int, letter: String) {
        val path = AppUtils.getAlphabetConnectPath(letter)
        val bm = AppUtils.getBitmapByAssetName(path, context = this)

        when (position) {
            0 -> {
                binding.img1.contentDescription = letter
                Glide.with(this).load(bm).into(binding.img1)
            }

            1 -> {
                binding.img2.contentDescription = letter
                Glide.with(this).load(bm).into(binding.img2)
            }

            else -> {
                binding.img3.contentDescription = letter
                Glide.with(this).load(bm).into(binding.img3)
            }
        }

    }

    private fun getPicImage(position: Int, letter: String) {
        val path = AppUtils.getAlphabetObjectPath(letter)
        val bm = AppUtils.getBitmapByAssetName(path, context = this)

        when (position) {
            0 -> {
                binding.pic1.contentDescription = letter
                Glide.with(this).load(bm).into(binding.pic1)
            }

            1 -> {
                binding.pic2.contentDescription = letter
                Glide.with(this).load(bm).into(binding.pic2)
            }

            else -> {
                binding.pic3.contentDescription = letter
                Glide.with(this).load(bm).into(binding.pic3)
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

                    }
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

                } else
                    Log.e("error", "Initialization Failed!")

            }.start()
        }


    }

    override fun onPause() {
        super.onPause()
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()

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


