package com.example.abclearngame.ballon

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.eisuchi.extention.setToFullScreen
import com.example.abclearngame.R
import com.example.abclearngame.activity.BaseActivity
import com.example.abclearngame.ballon.utils.HighScoreHelper
import com.example.abclearngame.ballon.utils.SimpleAlertDialog
import com.example.abclearngame.ballon.utils.SoundHelper
import com.example.abclearngame.databinding.ActivityGameplayBinding
import com.example.abclearngame.extension.executeAsyncTask
import com.example.abclearngame.extension.hide
import com.example.abclearngame.extension.invisible
import com.example.abclearngame.extension.visible
import com.example.abclearngame.utils.AppUtils
import com.example.abclearngame.utils.Constant
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.delay
import java.text.MessageFormat
import java.util.Date
import java.util.Random


/**
 * <h1>GamePlayActivity</h1>
 *
 *
 * **GamePlayActivity** represent gameplay screen and handle all game logic.
 */
class BalloonPopActivity : BaseActivity(), Balloon.BalloonListener {
    private val mRandom = Random()

    private lateinit var binding: ActivityGameplayBinding
    private var mBalloonsPerLevel = 26
    private var mBalloonsPopped = 0
    private var mBalloonsGenrate = 0
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mLevel = 0
    private var mScore = 0
    private var mHeartsUsed = 0
    private var mPlaying = false
    private var mGame = false
    private var mGameStopped = true
    private var mScoreDisplay: TextView? = null
    private var mLevelDisplay: TextView? = null
    private var mGoButton: Button? = null
    private lateinit var mContentView: ViewGroup
    private lateinit var mSoundHelper: SoundHelper
    private val mHeartImages: MutableList<ImageView> = ArrayList()
    private val mBalloons: MutableList<Balloon?> = ArrayList()
    private var currentAlphabetLetter = "A"
    private val handler = Handler(Looper.getMainLooper())
    private var mInterstitialAd: InterstitialAd? = null

    /**
     * This method is responsible for configurations of gameplay screen.
     *
     * @param savedInstanceState Define potentially saved parameters due to configurations changes.
     * @see Activity.onCreate
     */
    @SuppressLint("FindViewByIdCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContentView = binding.activityMain
        setToFullScreen(this)
        val viewTreeObserver = mContentView.viewTreeObserver

        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mContentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    mScreenWidth = mContentView.width
                    mScreenHeight = mContentView.height
                }
            })
        }

        mScoreDisplay = binding.scoreDisplay
        mLevelDisplay = binding.levelDisplay
        mHeartImages.add(binding.heart1)
        mHeartImages.add(binding.heart2)
        mHeartImages.add(binding.heart3)
        mHeartImages.add(binding.heart4)
        mHeartImages.add(binding.heart5)
        mGoButton = binding.goButton
        updateDisplay()
        mSoundHelper = SoundHelper(this)
        mSoundHelper.prepareMusicPlayer(this)

        interstitialAd()
        binding.btnBackGameplay.setOnClickListener {
            finish()
            Animatoo.animateFade(this)
        }

    }

    private fun showingInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    /* private val showInterstitialRunnable = object : Runnable {
         override fun run() {
             showingInterstitialAd()
             handler.postDelayed(this, Constant.adShowIntervalMillis)
         }
     }*/
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

    /**
     * This method is responsible for calling method setToFullScreen().
     *
     * @see Activity.onResume
     * @see BalloonPopActivity.setToFullScreen
     */
    override fun onResume() {
        super.onResume()
        //   handler.postDelayed(showInterstitialRunnable, Constant.adShowIntervalMillis)
        setToFullScreen(this)
    }


    /**
     * This method is responsible to start game and set beginning game parameters.
     *
     * @see BalloonPopActivity.setToFullScreen
     * @see SoundHelper.playMusic
     * @see BalloonPopActivity.startLevel
     */
    private fun startGame() {
        setToFullScreen(this)
        mScore = 0
        mLevel = 0
        mHeartsUsed = 0
        mGameStopped = false
        mGame = true
        for (pin in mHeartImages) pin.setImageResource(R.drawable.heart)
        startLevel()
    }

    /**
     * This method is responsible to start the next level and potentially give user appropriate Google Play Games achievement.
     *
     * @see Games.getAchievementsClient
     * @see BalloonPopActivity.updateDisplay
     * @see BalloonLauncher.execute
     */
    private fun startLevel() {
        mLevel++
        updateDisplay()
        currentAlphabetLetter = generateAlphabetLetter()
        binding.imgLetter.visible()
        binding.imgLetter.setImageBitmap(generateLetterBitmap(currentAlphabetLetter))
        //BalloonLauncher().execute(mLevel)
        doBackgroundTask(mLevel)
        mPlaying = true
        mBalloonsPopped = 0
        mGoButton!!.visibility = View.INVISIBLE
        binding.imgPlay.hide()
    }

    /**
     * This method is responsible to finish current level.
     *
     * @see Toast.makeText
     */
    private fun finishLevel() {
        Toast.makeText(this, getString(R.string.finish_level) + mLevel, Toast.LENGTH_SHORT).show()
        mPlaying = false
        binding.imgLetter.hide()
        mGoButton!!.text = MessageFormat.format(
            "{0} {1}",
            getString(R.string.level_start),
            mLevel + 1
        )
        mGoButton!!.visibility = View.VISIBLE
        Toast.makeText(
            this,
            "Showing Ad..",
            Toast.LENGTH_SHORT
        ).show()
        showingInterstitialAd()
    }

    /**
     * This method is called when button start game is clicked and indicate the beginning of the game if game is in the progress,
     * else it's start new level.
     *
     * @param view represent button which user is tapped.
     * @see BalloonPopActivity.startGame
     * @see BalloonPopActivity.startLevel
     */
    fun startLevel(view: View?) {
        startLevel()
    }

    fun playGame(view: View?) {
        if (mGameStopped) startGame()
    }

    /**
     * This method is called when balloon is popped, either by tapping by user or by going away.
     *
     * @param balloon   represent balloon object which is popped.
     * @param userTouch indicate if user popped balloon or balloon is going away.
     */
    override fun popBalloon(balloon: Balloon?, userTouch: Boolean) {
        mBalloonsPopped++
        mContentView.removeView(balloon)
        mBalloons.remove(balloon)
        if (userTouch) {
            if (session.isSoundOnOff) mSoundHelper.playSound()
            if (currentAlphabetLetter == balloon?.getCurrentLetter()) {
                mScore++
                currentAlphabetLetter = generateAlphabetLetter()
                binding.imgLetter.setImageBitmap(generateLetterBitmap(currentAlphabetLetter))
                // removeExistingBalloon()
            } else {
                mHeartsUsed++
                if (mHeartsUsed <= mHeartImages.size) mHeartImages[mHeartsUsed - 1].setImageResource(
                    R.drawable.broken_heart
                )
                if (mHeartsUsed == NUMBER_OF_HEARTS) {
                    gameOver()
                    return
                }
            }
        } else if (currentAlphabetLetter == balloon?.getCurrentLetter()) {
            if (session.isSoundOnOff) mSoundHelper.playSound()
            mHeartsUsed++
            if (mHeartsUsed <= mHeartImages.size) mHeartImages[mHeartsUsed - 1].setImageResource(R.drawable.broken_heart)
            if (mHeartsUsed == NUMBER_OF_HEARTS) {
                gameOver()
                return
            }
        }
        updateDisplay()
        if (mBalloonsPopped == mBalloonsPerLevel) {
            finishLevel()
            mBalloonsPerLevel += 10
        }
    }

    private fun removeExistingBalloon() {
        val iterator = mBalloons.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item?.letter == currentAlphabetLetter) {
                iterator.remove()
                //  mContentView.removeView(item)
            }
        }
    }


    /**
     * This method is called when game is over and showing new high score if it is achieved and send that score to the
     * Google Play Games leaderboard if user is signed in.
     *
     * @see HighScoreHelper
     *
     * @see SimpleAlertDialog
     */
    private fun gameOver() {
        binding.imgLetter.hide()
        Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
        mGame = false
        for (balloon in mBalloons) {
            mContentView.removeView(balloon)
            balloon!!.setPopped(true)
        }
        mBalloons.clear()
        mPlaying = false
        mGameStopped = true
        // mGoButton!!.setText(R.string.start_game)
        binding.imgPlay.visible()
        if (HighScoreHelper.isTopScore(this, mScore)) {
            HighScoreHelper.setTopScore(this, mScore)
            val dialog: SimpleAlertDialog = SimpleAlertDialog.newInstance(
                getString(R.string.new_high_score_title), getString(
                    R.string.new_high_score_message
                ) + mScore
            )
            if (!supportFragmentManager.isDestroyed)
                dialog.show(supportFragmentManager, null)
        }
        mGoButton?.invisible()
        binding.imgPlay.visible()
    }

    /**
     * This method update score after every popped balloon and level at the beginning of new level.
     */
    private fun updateDisplay() {
        mScoreDisplay!!.text = mScore.toString()
        mLevelDisplay!!.text = mLevel.toString()

    }

    /**
     * This method add new balloon to the screen.
     *
     * @param x represent x axis of the balloon.
     * @see Balloon
     *
     * @see ViewGroup.addView
     */
    private fun launchBalloon(x: Int) {
        var balloonLetter = Constant.LettersList[mRandom.nextInt(Constant.LettersList.size)]
        mBalloonsGenrate++
        if (mBalloonsGenrate % 5 == 0) balloonLetter = currentAlphabetLetter
        val balloon = Balloon(
            this,
            balloonLetter,
            150,
        )
        mBalloons.add(balloon)

        balloon.x = x.toFloat()
        balloon.y = (mScreenHeight + balloon.height).toFloat()
        mContentView.addView(balloon)

        balloon.releaseBalloon(
            mScreenHeight,
            Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - mLevel * 500)
        )
    }


    private fun doBackgroundTask(vararg params: Int?) {

        lifecycleScope.executeAsyncTask(
            doInBackground = { publishProgress: suspend (progress: Int) -> Unit ->
                if (params.size != 1) throw AssertionError(getString(R.string.assertion_message))
                val minDelay =
                    Math.max(
                        MIN_ANIMATION_DELAY, MAX_ANIMATION_DELAY - ((params[0]!!.minus(1)).times(
                            100
                        ))
                    ) / 2
                var balloonsLaunched = 0
                while (mPlaying && balloonsLaunched < mBalloonsPerLevel) {
                    val random = Random(Date().time)
                    publishProgress(random.nextInt(mScreenWidth - 200))
                    balloonsLaunched++
                    try {

                        delay((random.nextInt(minDelay) + minDelay).toLong())
                        // Thread.sleep((random.nextInt(minDelay) + minDelay).toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }


            },
            onPostExecute = {

            },
            onProgressUpdate = {
                launchBalloon(it)
            }
        )
    }

    /**
     * This class is responsible for calculating speed of balloons and x axis position of the balloon
     *
     * @see AsyncTask
     */
    @SuppressLint("StaticFieldLeak")
    private inner class BalloonLauncher : AsyncTask<Int, Int, Void>() {
        /**
         * This method is executing in background and calculate speed and position of balloons depends on game level.
         * With increasing level, speed of balloons is increasing too.
         *
         * @param params represent current level
         * @return null
         * @see AsyncTask.doInBackground
         * @see AsyncTask.publishProgress
         * @see Thread.sleep
         */


        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Int?): Void? {
            if (params.size != 1) throw AssertionError(getString(R.string.assertion_message))
            val minDelay =
                Math.max(
                    MIN_ANIMATION_DELAY, MAX_ANIMATION_DELAY - ((params[0]!!.minus(1)).times(
                        100
                    ))
                ) / 2
            var balloonsLaunched = 0
            while (mPlaying && balloonsLaunched < mBalloonsPerLevel) {
                val random = Random(Date().time)
                publishProgress(random.nextInt(mScreenWidth - 200))
                balloonsLaunched++

                try {
                    Thread.sleep((random.nextInt(minDelay) + minDelay).toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        /**
         * This method update UI, calling launchBalloon() method.
         *
         * @param values represent calculated x axis of balloon
         * @see BalloonPopActivity.launchBalloon
         * @see AsyncTask.onProgressUpdate
         */
        @Deprecated("Deprecated in Java")
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            launchBalloon(values[0]!!)
        }


    }

    private fun generateLetterBitmap(letter: String): Bitmap? {
        val path = AppUtils.getCapitalLatterPath(letter)
        return AppUtils.getBitmapByAssetName(path, this)
    }


    private fun generateAlphabetLetter() =
        Constant.LettersList[Random().nextInt(Constant.LettersList.size)]

    companion object {
        private const val MIN_ANIMATION_DELAY = 500
        private const val MAX_ANIMATION_DELAY = 1500
        private const val MIN_ANIMATION_DURATION = 1000
        private const val MAX_ANIMATION_DURATION = 6000
        private const val NUMBER_OF_HEARTS = 5
    }


}