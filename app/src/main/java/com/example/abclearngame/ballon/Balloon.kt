package com.example.abclearngame.ballon

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.abclearngame.ballon.utils.PixelHelper
import com.example.abclearngame.utils.AppUtils
import com.example.abclearngame.utils.AppUtils.getAlphabetBalloonPath
import rb.popview.PopField


/**
 * <h1>Balloon</h1>
 *
 *
 * **Balloon** class represent balloon game object.
 */
@SuppressLint("AppCompatCustomView")
class Balloon : ImageView, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private var mAnimator: ValueAnimator? = null

    private var mListener: BalloonListener? = null
    private var mPopped = false
     var letter ="A"
    private lateinit var context: Context

    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        alphabet: String,
        rawHeight: Int,
    ) : super(context) {
        this.context = context
        letter =alphabet
        val path= getAlphabetBalloonPath(alphabet)
        val bm  = AppUtils.getBitmapByAssetName(path, context = context)
        Glide.with(context).load(bm).into(this)

         mListener = context as BalloonListener

        layoutParams= ViewGroup.LayoutParams(
            PixelHelper.pixelsToDp(rawHeight / 2, context),
            PixelHelper.pixelsToDp(rawHeight, context)

        )
    }


    /**
     * This method is responsible for create releasing balloons animation during gameplay.
     *
     * @param screenHeight represents screen height
     * @param duration     represents number of milliseconds for animation duration. With increasing level, this number is reduces.
     * @see ValueAnimator
     */
    fun releaseBalloon(screenHeight: Int, duration: Int) {
        mAnimator = ValueAnimator()
        /*val startY = resources.displayMetrics.heightPixels.toFloat()
        val endY =this.height
        val finalEnd = -endY*/


     //   mAnimator = ObjectAnimator.ofFloat(this, "translationY", startY, finalEnd.toFloat())
        mAnimator!!.duration = duration.toLong()
        mAnimator!!.setFloatValues(screenHeight.toFloat(), -400f)
        mAnimator!!.interpolator = LinearInterpolator()
        mAnimator!!.setTarget(this)
        mAnimator!!.addListener(this)
        mAnimator!!.addUpdateListener(this)
        mAnimator!!.start()
    }

    /**
     * This method is calling every time when animation is update. Y axis of balloon is then increasing.
     *
     * @param valueAnimator represent object to animate.
     * @see ValueAnimator.getAnimatedValue
     * @see ValueAnimator.AnimatorUpdateListener.onAnimationUpdate
     */
    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        y = valueAnimator.animatedValue as Float
    }

    override fun onAnimationStart(animator: Animator) {}

    /**
     * This method is called when animation is finished. If user failed to pop balloon, balloon is popped mannualy and user
     * lose one life or lose the game depends on number of life that he has.
     */
    override fun onAnimationEnd(animator: Animator) {
        if (!mPopped) mListener!!.popBalloon(this, false)
    }

    override fun onAnimationCancel(animator: Animator) {}
    override fun onAnimationRepeat(animator: Animator) {}

    /**
     * This method is called when user touch the screen. If user pop balloon, method popBalloon is called.
     *
     * @param event represent which action user take.
     * @return boolean which return method onTouchEvent of superclass.
     * @see BalloonListener.popBalloon
     * @see android.app.Activity.onTouchEvent
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mPopped && event.action == MotionEvent.ACTION_DOWN) {
            val popField: PopField = PopField.attach2Window(context as Activity?)
            popField.popView(this)
            mListener!!.popBalloon(this, true)
            mPopped = true
            mAnimator!!.cancel()

        }
        return super.onTouchEvent(event)
    }

    /**
     * This method set state that represent if balloon is popped or not and if it is cancel animation.
     *
     * @param isBalloonPopped indicate if balloon is popped or not.
     * @see Animation.cancel
     */
    fun setPopped(isBalloonPopped: Boolean) {
        mPopped = isBalloonPopped
        if (isBalloonPopped) mAnimator!!.cancel()
    }


    fun getCurrentLetter():String{
       return letter
    }

    /**
     * This interface define one method, popBalloon which is implemented by GamePlayActivity.
     *
     *
     */
    interface BalloonListener {
        /**
         * @param balloon   represent Balloon object which should be popped.
         * @param userTouch boolean which indicate if user pop balloon or not.
         */
        fun popBalloon(balloon: Balloon?, userTouch: Boolean)
    }
}