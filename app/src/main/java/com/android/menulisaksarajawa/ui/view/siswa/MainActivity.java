package com.android.menulisaksarajawa.ui.view.siswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.databinding.ActivityMainBinding;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.GuideActivity;
import com.android.menulisaksarajawa.ui.view.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    PrefManager prefManager;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        sharedPref = getApplicationContext().getSharedPreferences("WriteTypes", Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
        spEditor = sharedPref.edit();
        loginCheck();

//        Toast.makeText(getApplicationContext(),"Welcome "+prefManager.getSPNama(),Toast.LENGTH_LONG).show();
        binding.tvName.setText(prefManager.getSPNama());
        binding.tvClass.setText(prefManager.getSPKelas());
        binding.btnInfo.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GuideActivity.class));
            }
        });

        binding.btnMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TheoryActivity.class));
            }
        });

        binding.btnBelajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(MainActivity.this, TypesActivity.class);
                spEditor.putString("write", "learn");
                spEditor.commit();
                move.putExtra("type", "learn");;
                startActivity(move);
            }
        });

        binding.btnLatihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(MainActivity.this, TypesActivity.class);
                spEditor.putString("write", "test");
                spEditor.commit();
                move.putExtra("type", "test");;
                startActivity(move);
            }
        });

        binding.btnNilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScoreActivity.class));
            }
        });
    }
    private void loginCheck(){
        if(!prefManager.loginStatus()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}