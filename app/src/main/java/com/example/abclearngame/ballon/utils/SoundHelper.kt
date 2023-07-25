package com.example.abclearngame.ballon.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.abclearngame.R

/**
 * <h1>SoundHelper</h1>
 *
 *
 * **SoundHelper** class is responsible for reproducing game sound and music.
 */
class SoundHelper(activity: Context) {
    private var mMusicPlayer: MediaPlayer? = null
    private var mSoundPool: SoundPool? = null
    private val mSoundID: Int
    private val mVolume: Float
    private var mLoaded = false

    init {
        val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / audioManager.getStreamMaxVolume(
            AudioManager.STREAM_MUSIC
        ).toFloat()
      //  activity.volumeControlStream = AudioManager.STREAM_MUSIC
        mSoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(6).build()
        } else {
            SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        }
        mSoundPool!!.setOnLoadCompleteListener { soundPool: SoundPool?, sampleId: Int, status: Int ->
            mLoaded = true
        }
        mSoundID = mSoundPool!!.load(activity, R.raw.balloon_pop, 1)
    }

    /**
     * This method is responsible to play game sound.
     *
     * @see SoundPool.play
     */
    fun playSound() {
        if (mLoaded) mSoundPool!!.play(mSoundID, mVolume, mVolume, 1, 0, 1f)
    }

    /**
     * This method is responsible for preparation of music player which is instance of MediaPlayer class.
     *
     * @param context Context of activity which call this method.
     * @see MediaPlayer
     */
    fun prepareMusicPlayer(context: Context) {
        mMusicPlayer = MediaPlayer.create(context.applicationContext, R.raw.bgmusic)
       // mMusicPlayer!!.setVolume(.5f, .5f)
        mMusicPlayer!!.isLooping = true
    }

    /**
     * This method is responsible for start playing game music.
     *
     * @see MediaPlayer.start
     */
    fun playMusic() {
        if (mMusicPlayer != null) mMusicPlayer!!.start()
    }

    /**
     * This method is responsible for pausing game music.
     *
     * @see MediaPlayer.isPlaying
     * @see MediaPlayer.pause
     */
    fun pauseMusic() {
        if (mMusicPlayer != null && mMusicPlayer!!.isPlaying) mMusicPlayer!!.pause()
    }
}