package com.android.menulisaksarajawa.ui.model;

public class Nilai {
    private String name;
    private String angka;
    private String carakan;
    private String pasangan;
    private String swara;
    private String total;
    
    public Nilai(String name, String angka, String carakan, String pasangan, String swara, String total){
        this.name = name;
        this.angka = angka;
        this.carakan = carakan;
        this.pasangan = pasangan;
        this.swara = swara;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAngka() {
        return angka;
    }

    public void setAngka(String angka) {
        this.angka = angka;
    }

    public String getCarakan() {
        return carakan;
    }

    public void setCarakan(String carakan) {
        this.carakan = carakan;
    }

    public String getPasangan() {
        return pasangan;
    }

    public void setPasangan(String pasangan) {
        this.pasangan = pasangan;
    }

    public String getSwara() {
        return swara;
    }

    public void setSwara(String swara) {
        this.swara = swara;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
