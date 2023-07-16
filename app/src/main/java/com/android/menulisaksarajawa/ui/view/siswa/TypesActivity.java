package com.android.menulisaksarajawa.ui.view.siswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityTypesBinding;

public class TypesActivity extends AppCompatActivity {
    private ActivityTypesBinding binding;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTypesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        binding.btnAngka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "angka");
                startActivity(move);
            }
        });

        binding.btnCarakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "carakan");
                startActivity(move);
            }
        });

        binding.btnPasangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "pasangan");
                startActivity(move);
            }
        });

        binding.btnSwara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "swara");
                startActivity(move);
            }
        });
    }
}