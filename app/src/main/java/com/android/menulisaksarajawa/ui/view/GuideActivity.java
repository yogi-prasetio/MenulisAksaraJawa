package com.android.menulisaksarajawa.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.databinding.ActivityGuideBinding;

public class GuideActivity extends AppCompatActivity {
    private ActivityGuideBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPrefLogin = getApplicationContext().getSharedPreferences("LoginSharedPreferences", Context.MODE_PRIVATE);

        binding.btnLogout.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sharedPrefLogin.edit().clear().apply();
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                Toast.makeText(getApplicationContext(),"Logout Berhasil!",Toast.LENGTH_LONG).show();
            }
        });
    }
}