package com.example.abclearngame.ballon.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * <h1>HighScoreHelper</h1>
 *
 *
 * **HighScoreHelper** class is responsible for save high score of the game, return high score and determine if current score is
 * the high score.
 */
object HighScoreHelper {


    private const val PREFS_GLOBAL = "prefs_global"
    private const val PREF_TOP_SCORE = "pref_top_score"

    /**
     * This method is used to return instance to the application SharedPreferences.
     *
     * @param context This is the context of activity which call this method.
     * @return SharedPreferences This is the instance of application SharedPreferences.
     * @see SharedPreferences
     */
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_GLOBAL, Context.MODE_PRIVATE)
    }

    /**
     * This method is used to check if score of finished game is the high score.
     *
     * @param context  This is the context of activity which call this method.
     * @param newScore This is the score of finished game.
     * @return boolean This indicate is current score is the high score.
     * @see SharedPreferences
     */
   fun isTopScore(context: Context, newScore: Int): Boolean {
        val topScore = getPreferences(context).getInt(PREF_TOP_SCORE, 0)
        return newScore > topScore
    }

    /**
     * This method is used to return current high score of the player.
     *
     * @param context This is the context of activity which call this method.
     * @return int This number represent current high score.
     * @see SharedPreferences
     */
    fun getTopScore(context: Context): Int {
        return getPreferences(context).getInt(PREF_TOP_SCORE, 0)
    }

    /**
     * This method is used to set new high score for the player.
     *
     * @param context This is the context of activity which call this method.
     * @param score   This is the score that is the new high score.
     * @see SharedPreferences
     */
   fun setTopScore(context: Context, score: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(PREF_TOP_SCORE, score)
        editor.apply()
    }
}