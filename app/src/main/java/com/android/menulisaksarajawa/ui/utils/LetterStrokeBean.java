package com.android.menulisaksarajawa.ui.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LetterStrokeBean {
    public String id;
    @SerializedName("char")
    public String letter;
    public List<Strokes> strokes;
    public String style;

    public static class Strokes {
        public List<String> points;
    }

    public List<String> getCurrentStrokePoints(int currentStroke) {
        return ((Strokes) this.strokes.get(currentStroke)).points;
    }
}
