package com.example.abclearngame.activity

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.abclearngame.R
import com.example.abclearngame.ballon.utils.SoundHelper
import com.example.abclearngame.utils.SessionManager


/**
 * Created by Hardik
 */

open class BaseActivity : AppCompatActivity() {

    lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(this)

    }




    override fun onPause() {
        super.onPause()

        hideSoftKeyboard()
    }

    override fun onDestroy() {
        hideSoftKeyboard()
        super.onDestroy()
    }

    fun showSoftKeyboard(view: EditText) {
        view.requestFocus(view.text.length)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideSoftKeyboard(): Boolean {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            return false
        }
    }

}
