package com.example.abclearngame.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream

object AppUtils {

    fun getCapitalLatterPath(letter: String): String {
        return Constant.CAPITALLATTER + "${letter}.png"
    }

    fun getAlphabetBalloonPath(letter: String): String {
        return Constant.ALPHABATESBALLOON + "${letter}.png"
    }

    fun getAlphabetConnectPath(letter: String): String {
        return Constant.ALPHABETCONNECT + "${letter}.png"
    }
  fun getAlphabetObjectPath(letter: String): String {
        return Constant.ALPHABETCOBJECT + "${letter}.png"
    }

    fun getBitmapByAssetName(path: String, context: Context): Bitmap? {
        val bitmap: Bitmap?
        var `is`: InputStream? = null
        try {
            val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(path)
            `is` = assetFileDescriptor.createInputStream()
            val options = BitmapFactory.Options()
            options.inMutable = true
            bitmap = BitmapFactory.decodeStream(`is`, null, options)
            return bitmap
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun letterToSpeak(letter: String):String{
        return when(letter){

            "A"-> "a for Apple"
            "B"-> "b for Ball"
            "C"-> "c for Cat"
            "D"-> "d for Duck"
            "E"-> "e for Elephant"
            "F"-> "f for Fish"
            "G"-> "g for Guitar"
            "H"-> "h for Hen"
            "I"-> "i for IceCream"
            "J"-> "j for Joker"
            "K"-> "k for Kite"
            "L"-> "l for Lion"
            "M"-> "m for Monkey"
            "N"-> "n for Nest"
            "O"-> "o for Orange"
            "P"-> "p for Pineapple"
            "Q"-> "q for Queen"
            "R"-> "r for Rose"
            "S"-> "s for Snake"
            "T"-> "t for Tomato"
            "U"-> "u for Umbrella"
            "V"-> "v for Van"
            "W"-> "w for Whale"
            "X"-> "x for XmasTree"
            "Y"-> "y for Yak"
            "Z"-> "z for Zebra"
            else ->
                "no idea"
        }

    }
}