package com.android.menulisaksarajawa.ui.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LetterFactory {
    private String letter = "";
    @Retention(RetentionPolicy.SOURCE)
    public @interface Letter {
    }

    public String getLetterAssets() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("letters/");
        stringBuilder.append(this.letter);
        stringBuilder.append(".png");
        return stringBuilder.toString();
    }
    public String getStrokeAssets() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("strokes/");
        stringBuilder.append(this.letter);
        stringBuilder.append(".json");
        return stringBuilder.toString();
    }

    public void setLetter(String letterChar) {
        this.letter = letterChar;
    }
}
