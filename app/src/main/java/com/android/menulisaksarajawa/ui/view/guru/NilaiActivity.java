package com.android.menulisaksarajawa.ui.view.guru;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityNilaiBinding;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.GuideActivity;

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
                move.putExtra("title", "VIII A");
                startActivity(move);
            }
        });
        binding.btnKelasB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20B");
                move.putExtra("title", "VIII B");
                startActivity(move);
            }
        });
        binding.btnKelasC.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20C");
                move.putExtra("title", "VIII C");
                startActivity(move);
            }
        });
        binding.btnKelasD.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20D");
                move.putExtra("title", "VIII D");
                startActivity(move);
            }
        });
        binding.btnKelasE.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20E");
                move.putExtra("title", "VIII E");
                startActivity(move);
            }
        });
        binding.btnKelasF.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(NilaiActivity.this, NilaiDetailActivity.class);
                move.putExtra("kelas", "VIII%20F");
                move.putExtra("title", "VIII F");
                startActivity(move);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_info_app) {
            Intent intent = new Intent(NilaiActivity.this, GuideActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}