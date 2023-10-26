package com.android.menulisaksarajawa.ui.view.guru;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityNilaiBinding;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.MainActivity;

public class NilaiActivity extends AppCompatActivity {
    private ActivityNilaiBinding binding;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNilaiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        prefManager = new PrefManager(this);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_circle);
        
        binding.btnKelasA.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetail.class);
                move.putExtra("kelas", "VIII%20A");
                move.putExtra("title", "VIII A");
                startActivity(move);
            }
        });
        binding.btnKelasB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetail.class);
                move.putExtra("kelas", "VIII%20B");
                move.putExtra("title", "VIII B");
                startActivity(move);
            }
        });
        binding.btnKelasC.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetail.class);
                move.putExtra("kelas", "VIII%20C");
                move.putExtra("title", "VIII C");
                startActivity(move);
            }
        });
        binding.btnKelasD.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetail.class);
                move.putExtra("kelas", "VIII%20D");
                move.putExtra("title", "VIII D");
                startActivity(move);
            }
        });
        binding.btnKelasE.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetail.class);
                move.putExtra("kelas", "VIII%20E");
                move.putExtra("title", "VIII E");
                startActivity(move);
            }
        });
        binding.btnKelasF.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetail.class);
                move.putExtra("kelas", "VIII%20F");
                move.putExtra("title", "VIII F");
                startActivity(move);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(NilaiActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}