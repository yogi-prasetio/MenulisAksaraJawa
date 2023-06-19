package com.android.menulisaksarajawa.ui.model;

public class Characters {
    private Integer aksara;
    private String romaji;
    private Integer image;
    private Integer audio;

    public Characters(Integer aksara, String romaji, Integer image, Integer audio) {
        this.aksara = aksara;
        this.romaji = romaji;
        this.image = image;
        this.audio = audio;
    }

    public Integer getAksara() {
        return aksara;
    }

    public void setAksara(Integer aksara) {
        this.aksara = aksara;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getAudio() {
        return audio;
    }

    public void setAudio(Integer audio) {
        this.audio = audio;
    }
}
