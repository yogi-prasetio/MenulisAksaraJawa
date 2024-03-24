package com.android.menulisaksarajawa.ui.database;

public class Config {
    public static final String URL_REGISTER="https://undanganforyou.my.id/aksarajawa/users/register";
    public static final String URL_LOGIN="https://undanganforyou.my.id/aksarajawa/users/login";
    public static final String URL_GET_USER="https://undanganforyou.my.id/aksarajawa/users/";

    public static final String URL_GET_NILAI_USER="https://undanganforyou.my.id/aksarajawa/nilai/user/";
    public static final String URL_GET_NILAI="https://undanganforyou.my.id/aksarajawa/nilai/index.php?kelas=";
    public static final String URL_UPDATE_NILAI="https://undanganforyou.my.id/aksarajawa/nilai/update";
    public static final String URL_DELETE_NILAI="https://undanganforyou.my.id/aksarajawa/nilai/deleteNilai.php?kelas=";
    public static final String URL_RESET_NILAI="https://undanganforyou.my.id/aksarajawa/nilai/reset";
    public static final String URL_ADD_NILAI_HISTORY="https://undanganforyou.my.id/aksarajawa/nilai/addHistory";
    public static final String URL_GET_NILAI_HISTORY="https://undanganforyou.my.id/aksarajawa/nilai/history/";
    public static final String URL_GET_NOTES_BY_USER="https://undanganforyou.my.id/aksarajawa/notes/";
    public static final String URL_ADD_NOTES="https://undanganforyou.my.id/aksarajawa/notes/add/";

    public static final String URL_GET_AKSARA="https://undanganforyou.my.id/aksarajawa/aksara/";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_USER_ID = "id";
    public static final String KEY_NAMA = "name";
    public static final String KEY_KELAS = "class";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROLE = "role";

    public static final String KEY_ID_JENIS = "id_jenis";
    public static final String KEY_STATUS = "status";
    public static final String KEY_NILAI = "nilai";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_USER_ID = "id";
    public static final String TAG_NAMA = "name";
    public static final String TAG_KELAS = "class";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_ROLE = "role";

    public static final String USER_ID = "user_id";
    public static final String TAG_ID_JENIS = "id_jenis";
    public static final String TAG_STATUS = "status";
    public static final String TAG_NILAI = "nilai";
}
