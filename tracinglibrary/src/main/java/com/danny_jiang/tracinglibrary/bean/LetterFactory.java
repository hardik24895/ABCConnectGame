package com.danny_jiang.tracinglibrary.bean;



import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LetterFactory {

    public static final String A = "A";
    public static final String  B = "B";
    public static final String C = "C";
    public static final String D = "D";
    public static final String E = "E";
    public static final String F ="F";
    public static final String G ="G";
    public static final String H = "H";
    public static final String I = "I";
    public static final String J = "J";
    public static final String K = "K";
    public static final String L ="L";
    public static final String M = "M";
    public static final String N = "N";
    public static final String O = "O";
    public static final String P = "P";
    public static final String Q = "Q";
    public static final String R = "R";
    public static final String S = "S";
    public static final String T = "T";
    public static final String U = "U";
    public static final String V = "V";
    public static final String W = "W";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String Z = "Z";

    public String getLetterAssets() {
        return "letter/" + letter + "_bg.png";
    }

    public String getTracingAssets() {
        return "trace/" + letter + "_tracing.png";
    }

    public String getStrokeAssets() {
        return "strokes/" + letter + "_PointsInfo.json";
    }

    @StringDef({A, B, C, D, E, F, G,
            H, I, J, K, L, M, N,
            O, P, Q, R, S, T,
            U, V, W, X, Y, Z})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Letter {
    }

    private String letter = A;

    public void setLetter(@Letter String letterChar) {
        this.letter = letterChar;
    }
}
