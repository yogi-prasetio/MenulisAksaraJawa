package com.android.menulisaksarajawa.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityGuideBinding;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.guru.NilaiActivity;
import com.android.menulisaksarajawa.ui.view.siswa.MainActivity;

public class GuideActivity extends AppCompatActivity {
    private ActivityGuideBinding binding;
    private SharedPreferences sharedPrefLogin;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        sharedPrefLogin = getApplicationContext().getSharedPreferences("LoginSharedPreferences", Context.MODE_PRIVATE);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        binding.btnLogout.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sharedPrefLogin.edit().clear().apply();
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                Toast.makeText(getApplicationContext(),"Logout Berhasil!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            String role = prefManager.getSPRole();
            if (role.equals("siswa")) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
            } else if (role.equals("guru")){
                startActivity(new Intent(GuideActivity.this, NilaiActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}