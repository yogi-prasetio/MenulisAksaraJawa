package com.android.menulisaksarajawa.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.menulisaksarajawa.databinding.ActivityCharacterListBinding;
import com.android.menulisaksarajawa.ui.model.Aksara;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.view.adapter.ListAksaraAdapter;

import java.util.ArrayList;

public class CharacterListActivity extends AppCompatActivity {
    private ActivityCharacterListBinding binding;
    private ListAksaraAdapter adapter;
    private ArrayList<Characters> list = new ArrayList();
    private String type;
    private String jenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCharacterListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        type = getIntent().getStringExtra("type");
        jenis = getIntent().getStringExtra("jenis");
//        Toast.makeText(getBaseContext(), type, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getBaseContext(), jenis, Toast.LENGTH_SHORT).show();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(Color.WHITE);
        if(type.equals("learn")) {
            binding.toolbar.setTitle("Belajar Menulis Aksara Jawa");
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CharacterListActivity.this, TypesActivity.class));
                }
            });
        } else if(type.equals("test")) {
            binding.toolbar.setTitle("Latihan Menulis Aksara Jawa");
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CharacterListActivity.this, TypesActivity.class));
                }
            });
        }
        setView();
        setRecycleView();
    }

    private void setView() {
        Aksara aksara = new Aksara();
        if (jenis.equals("angka")) {
            binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
            list.addAll(aksara.getAksaraAngka());
        } else if (jenis.equals("carakan")){
            binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
            list.addAll(aksara.getAksarCarakan());
        } else if (jenis.equals("pasangan")){
            binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
            list.addAll(aksara.getAksarPasangan());
        } else if (jenis.equals("swara")){
            binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
            list.addAll(aksara.getAksarSwara());
        }
        binding.rvAksara.setHasFixedSize(true);
    }

    private void setRecycleView(){
        adapter = new ListAksaraAdapter(list);
        binding.rvAksara.setAdapter(adapter);

        adapter.setOnItemClickCallback(new ListAksaraAdapter.OnItemClickCallback() {
            public void onItemClicked(Characters data){
//                Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_LONG).show();
                if(type.equals("learn")){
                    Intent move = new Intent(CharacterListActivity.this, LearnActivity.class);
                    move.putExtra("image", data.getImage());
                    move.putExtra("aksara", data.getAksara());
                    move.putExtra("romaji", data.getRomaji());
                    move.putExtra("audio", data.getAudio());
                    move.putExtra("type", jenis);
                    startActivity(move);
                } else if(type.equals("test")){
                    Intent move = new Intent(CharacterListActivity.this, PracticeActivity.class);
                    move.putExtra("aksara", data.getAksara());
                    move.putExtra("romaji", data.getRomaji());
                    startActivity(move);
                }
            }
        });
    }
}