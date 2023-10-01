package com.android.menulisaksarajawa.ui.model;

public class Nilai {
    private String name;
    private String angka;
    private String carakan;
    private String pasangan;
    private String swara;

    private String start_angka;
    private String last_angka;
    private String start_carakan;
    private String last_carakan;
    private String start_pasangan;
    private String last_pasangan;
    private String start_swara;
    private String last_swara;

    private String total;
    
    public Nilai(String name, String angka, String carakan, String pasangan, String swara, String total, String last_angka, String start_carakan, String start_pasangan, String start_swara, String start_angka, String last_carakan, String last_pasangan, String last_swara){
        this.name = name;
        this.angka = angka;
        this.carakan = carakan;
        this.pasangan = pasangan;
        this.swara = swara;
        this.total = total;

        this.start_angka = start_angka;
        this.start_carakan = start_carakan;
        this.start_pasangan = start_pasangan;
        this.start_swara = start_swara;
        this.last_angka = last_angka;
        this.last_carakan = last_carakan;
        this.last_pasangan = last_pasangan;
        this.last_swara = last_swara;
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

    public String getStart_angka() {
        return start_angka;
    }

    public void setStart_angka(String start_angka) {
        this.start_angka = start_angka;
    }

    public String getLast_angka() {
        return last_angka;
    }

    public void setLast_angka(String last_angka) {
        this.last_angka = last_angka;
    }

    public String getStart_carakan() {
        return start_carakan;
    }

    public void setStart_carakan(String start_carakan) {
        this.start_carakan = start_carakan;
    }

    public String getLast_carakan() {
        return last_carakan;
    }

    public void setLast_carakan(String last_carakan) {
        this.last_carakan = last_carakan;
    }

    public String getStart_pasangan() {
        return start_pasangan;
    }

    public void setStart_pasangan(String start_pasangan) {
        this.start_pasangan = start_pasangan;
    }

    public String getLast_pasangan() {
        return last_pasangan;
    }

    public void setLast_pasangan(String last_pasangan) {
        this.last_pasangan = last_pasangan;
    }

    public String getStart_swara() {
        return start_swara;
    }

    public void setStart_swara(String start_swara) {
        this.start_swara = start_swara;
    }

    public String getLast_swara() {
        return last_swara;
    }

    public void setLast_swara(String last_swara) {
        this.last_swara = last_swara;
    }
}
