package com.android.menulisaksarajawa.ui.view.guru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.GuideActivity;
import com.android.menulisaksarajawa.ui.view.LoginActivity;
import com.android.menulisaksarajawa.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {
    private ActivityInfoBinding binding;
    private SharedPreferences sharedPrefLogin;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefLogin = getApplicationContext().getSharedPreferences("LoginSharedPreferences", Context.MODE_PRIVATE);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        binding.btnLogout.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sharedPrefLogin.edit().clear().apply();
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                Toast.makeText(getApplicationContext(),"Logout Berhasil!",Toast.LENGTH_LONG).show();
            }
        });
    }
}