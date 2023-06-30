package com.android.menulisaksarajawa.ui.view.guru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.databinding.ActivityNilaiBinding;
import com.android.menulisaksarajawa.ui.utils.PrefManager;

public class NilaiActivity extends AppCompatActivity {
    private ActivityNilaiBinding binding;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNilaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefManager = new PrefManager(this);
        
        binding.btnKelasA.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20A");
                startActivity(move);
            }
        });
        binding.btnKelasB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20B");
                startActivity(move);
            }
        });
        binding.btnKelasC.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20C");
                startActivity(move);
            }
        });
        binding.btnKelasD.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20D");
                startActivity(move);
            }
        });
        binding.btnKelasE.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20E");
                startActivity(move);
            }
        });
        binding.btnKelasF.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20F");
                startActivity(move);
            }
        });
    }
}