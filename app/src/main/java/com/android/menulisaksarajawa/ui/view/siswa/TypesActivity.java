package com.android.menulisaksarajawa.ui.view.siswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
                move.putExtra("jenis", "Angka");
                startActivity(move);
            }
        });

        binding.btnCarakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "Carakan");
                startActivity(move);
            }
        });

        binding.btnPasangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "Pasangan");
                startActivity(move);
            }
        });

        binding.btnSwara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(TypesActivity.this, CharacterListActivity.class);
                move.putExtra("jenis", "Swara");
                startActivity(move);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TypesActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}