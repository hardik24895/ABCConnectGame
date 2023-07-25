package com.eisuchi.extention

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.abclearngame.utils.Constant
import java.util.*


inline fun <reified T : Activity> AppCompatActivity.goToActivityAndClearTask() {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    Animatoo.animateShrink(this)
    finish()
}

inline fun <reified T : Activity> Fragment.goToActivityAndClearTask() {
    val intent = Intent(context, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    activity?.finish()
}

inline fun <reified T : Activity> AppCompatActivity.goToActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    Animatoo.animateFade(this)
}

inline fun <reified T : Activity> Fragment.goToActivity() {
    startActivity(Intent(activity, T::class.java))
    activity?.let { Animatoo.animateCard(it) }
}

inline fun <reified T : Activity> AppCompatActivity.goToActivityBundle(bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(Constant.DATA, bundle)
    startActivity(intent)
    Animatoo.animateZoom(this)
}


inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun AppCompatActivity.addFragments(fragments: List<Fragment>, containerId: Int) {
    fragments.forEach {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerId, it)
        ft.commitAllowingStateLoss()
    }
}

inline fun <reified T : Activity> AppCompatActivity.onBackPress() {
    Animatoo.animateSlideLeft(this)
    finish()
}

fun AppCompatActivity.replaceFragments(fragments: List<Fragment>, containerId: Int) {
    fragments.forEach {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(containerId, it)
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(containerId, fragment)
    ft.commitAllowingStateLoss()
}

fun Fragment.replaceFragment(fragment: Fragment, containerId: Int) {
    val ft = parentFragmentManager.beginTransaction()
    ft.replace(containerId, fragment)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    containerId: Int,
    addToStack: Boolean = true
) {
    val ft = supportFragmentManager.beginTransaction()
    ft.add(containerId, fragment)
    if (addToStack) ft.addToBackStack(fragment.javaClass.name)
    ft.commitAllowingStateLoss()
}

fun Fragment.addFragment(fragment: Fragment, containerId: Int, addToStack: Boolean = true) {
    val ft = childFragmentManager.beginTransaction()
    ft.add(containerId, fragment)
    if (addToStack) ft.addToBackStack(fragment.javaClass.name)
    ft.commitAllowingStateLoss()
}

fun Fragment.removeFragment(fragment: Fragment) {
    val ft = parentFragmentManager.beginTransaction()
    ft.remove(fragment)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.show(fragment)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.hide(fragment)
    ft.commitAllowingStateLoss()
}


/**
 * This method is responsible to transfer MainActivity into fullscreen mode.
 */
fun setToFullScreen(activity: Activity) {
    @Suppress("DEPRECATION")
    if (SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

}

fun hopAnimation(view: View) {
    val animation = view
        .animate()
        .translationYBy(-40f)
        .alpha(0.9f)
        .setDuration(400)
    animation.withEndAction {
        view
            .animate()
            .alpha(0.5f)
            .translationYBy(40f).duration = 400
    }
}

/*
fun setHomeScreenTitle(requireActivity: Activity, title: String) {
    val toolbar = requireActivity.findViewById<View>(R.id.toolbar)
    val tvTitle: TextviewBold = toolbar.findViewById(R.id.tvTitle)
    tvTitle.setText(title)
}
*/


