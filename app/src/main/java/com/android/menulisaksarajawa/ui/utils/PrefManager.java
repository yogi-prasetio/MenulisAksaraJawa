package com.android.menulisaksarajawa.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    public static final String PREF_NAME = "LoginSharedPreferences";

    public static final String SES_ID = "id";
    public static final String SES_NAMA = "name";
    public static final String SES_KELAS = "kelas";
    public static final String SES_USERNAME = "username";
    public static final String SES_ROLE = "role";
    public static final String NILAI = "nilai";

    public static final String IS_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public PrefManager(Context context){
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPId(){
        return sp.getString(SES_ID, "");
    }

    public String getSPNama(){
        return sp.getString(SES_NAMA, "");
    }

    public String getSPKelas(){
        return sp.getString(SES_KELAS, "");
    }

    public String getSPUsername(){
        return sp.getString(SES_USERNAME, "");
    }

    public String getSPRole(){
        return sp.getString(SES_ROLE, "");
    }

    public String getNilai(){
        return sp.getString(NILAI, "");
    }

    public Boolean loginStatus(){
        return sp.getBoolean(IS_LOGIN, false);
    }
}
