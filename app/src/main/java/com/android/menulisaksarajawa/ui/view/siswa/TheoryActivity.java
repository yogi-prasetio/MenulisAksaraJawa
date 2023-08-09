package com.android.menulisaksarajawa.ui.view.siswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityTheoryBinding;

public class TheoryActivity extends AppCompatActivity {

    private ActivityTheoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        binding.tvAngkaTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Angka");
                startActivity(move);;
            }
        });

        binding.tvCarakanTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Carakan");
                startActivity(move);;
            }
        });

        binding.tvPasanganTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Pasangan");
                startActivity(move);;
            }
        });

        binding.tvSwaraTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Swara");
                startActivity(move);;
            }
        });
    }
}