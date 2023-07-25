package com.example.abclearngame.utils

import com.danny_jiang.tracinglibrary.bean.LetterFactory

object Constant {

    const val DATA ="Data"
    const val LETTERS ="LETTERS"
    const val LETTER_POSITION ="LETTERS_POSITION"
    const val CAPITALLATTER="CapitalLatter/"
    const val ALPHABATESBALLOON="AlphabatesBalloon/"
    const val ALPHABETCONNECT="ConnectLetter/"
    const val ALPHABETCOBJECT="AlphabetObject/"

    fun getLetterPath(letter:String) = "letter/" + letter + "_bg.png"


    const val adUnitId = "ca-app-pub-3940256099942544/1033173712"
    const val adShowIntervalMillis = 30000L // 1 minute interval

    val LettersList=  listOf<String>(
        LetterFactory.A,
        LetterFactory.B,
        LetterFactory.C,
        LetterFactory.D,
        LetterFactory.E,
        LetterFactory.F,
        LetterFactory.G,
        LetterFactory.H,
        LetterFactory.I,
        LetterFactory.J,
        LetterFactory.K,
        LetterFactory.L,
        LetterFactory.M,
        LetterFactory.N,
        LetterFactory.O,
        LetterFactory.P,
        LetterFactory.Q,
        LetterFactory.R,
        LetterFactory.S,
        LetterFactory.T,
        LetterFactory.U,
        LetterFactory.V,
        LetterFactory.W,
        LetterFactory.X,
        LetterFactory.Y,
        LetterFactory.Z
    )


}