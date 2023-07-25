package com.example.abclearngame.utils

import android.app.NotificationManager
import android.content.Context
import com.example.abclearngame.R


class SessionManager(
    private val context: Context
) {
    val pref =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = pref.contains(KEY_IS_LOGIN) && pref.getBoolean(KEY_IS_LOGIN, false)
        set(isLoggedIn) = storeDataByKey(KEY_IS_LOGIN, isLoggedIn)

    var isRegester: Boolean
        get() = pref.contains(KEY_IS_REGISTER) && pref.getBoolean(KEY_IS_REGISTER, false)
        set(isRegester) = storeDataByKey(KEY_IS_REGISTER, isRegester)

    var isMusicOnOff: Boolean
        get() = pref.getBoolean(KEY_IS_MUSIC_ON_OFF, true)
        set(isMusicOnOff) = storeDataByKey(KEY_IS_MUSIC_ON_OFF, isMusicOnOff)

    var isSoundOnOff: Boolean
        get() =  pref.getBoolean(KEY_IS_SOUND_ON_OFF, true)
        set(isSoundOnOff) = storeDataByKey(KEY_IS_SOUND_ON_OFF, isSoundOnOff)

    var isFirstTime: Boolean
        get() = pref.contains(KEY_IS_SOUND_ON_OFF) && pref.getBoolean(KEY_IS_FIRST_TIME, true)
        set(isFirstTime) = storeDataByKey(KEY_IS_FIRST_TIME, isFirstTime)



    fun getDataByKey(Key: String, DefaultValue: String = ""): String {
        return if (pref.contains(Key)) {
            pref.getString(Key, DefaultValue)!!
        } else {
            DefaultValue
        }
    }


    private fun getDataByKeyBoolean(Key: String, DefaultValue: Boolean): Boolean {
        return if (pref.contains(Key)) {
            pref.getBoolean(Key, DefaultValue)
        } else {
            DefaultValue
        }
    }

    @JvmOverloads
    fun getDataByKeyInt(Key: String, DefaultValue: Int = 0): Int {
        return if (pref.contains(Key)) {
            pref.getInt(Key, DefaultValue)
        } else {
            DefaultValue
        }
    }

    fun storeDataByKey(key: String, Value: String) {
        pref.edit().putString(key, Value).apply()
    }

    fun storeDataByKey(key: String, Value: Int) {
        pref.edit().putInt(key, Value).apply()
    }

    private fun storeDataByKey(key: String, Value: Boolean) {
        pref.edit().putBoolean(key, Value).apply()
    }

    operator fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun remove(key: String) {
        pref.edit().remove(key).apply()
    }

    fun clearSession() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        val editor = context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit()
        editor.clear().apply()

        pref.edit().clear().apply()
    }

    companion object {
        const val KEY_IS_LOGIN = "isLogin"
        const val KEY_IS_REGISTER = "isRegister"
        const val KEY_IS_SOUND_ON_OFF = "isSoundOnOff"
        const val KEY_IS_MUSIC_ON_OFF = "isMusicOnOff"
        const val KEY_IS_FIRST_TIME = "isFirstTime"


    }
}