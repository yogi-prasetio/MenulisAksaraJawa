package com.android.menulisaksarajawa.ui.model;

public class Users {
    private String id;
    private String name;
    private String kelas;
    private String role;
    private Integer score;

    public Users(String id, String name, String kelas, String role, Integer score) {
        this.id = id;
        this.name = name;
        this.kelas = kelas;
        this.role = role;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
