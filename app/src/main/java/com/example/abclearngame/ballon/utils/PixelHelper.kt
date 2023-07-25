package com.example.abclearngame.ballon.utils

import android.content.Context
import android.util.TypedValue

/**
 * <h1>PixelHelper</h1>
 *
 *
 * **PixelHelper class is responsible to convert pixels into dp.**
 */
object PixelHelper {
    /**
     * This method convert screen pixels into dp.
     *
     * @param px      Number of screen pixels.
     * @param context Context of activity which call this method.
     * @return int Number of screen's dp.
     * @see TypedValue
     */
    fun pixelsToDp(px: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            px.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}