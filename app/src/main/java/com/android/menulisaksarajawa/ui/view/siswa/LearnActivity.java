package com.android.menulisaksarajawa.ui.view.siswa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityLearnBinding;
import com.android.menulisaksarajawa.ui.model.Aksara;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.model.pip.PointInPolygon;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;

public class LearnActivity extends AppCompatActivity {
    private ActivityLearnBinding binding;
    CanvasView canvas;
    ArrayList<Characters> listAksara = new ArrayList();
    private String romaji, type;
    private Integer aksara, image, audio, index = 0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        type = getIntent().getStringExtra("type");
        aksara = getIntent().getIntExtra("aksara", 0);
        romaji = getIntent().getStringExtra("romaji");
        image = getIntent().getIntExtra("image", 0);
        audio = getIntent().getIntExtra("audio", 0);
        setType();

        binding.tvCharInfo.setText(romaji.toUpperCase());
        Glide.with(LearnActivity.this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.myanimation);

        int listSize = listAksara.size()-1;

        if(index == 0){
            binding.btnBefore.setVisibility(View.GONE);
        } else if(index == listSize){
            binding.btnNext.setVisibility(View.GONE);
        }

        getSupportActionBar().setTitle("Menulis Aksara " +romaji.toUpperCase());
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        canvas = binding.letter;
        canvas.setLetterChar(romaji);
        canvas.setPointColor(Color.RED);
        canvas.setTracingListener(new CanvasView.TracingListener() {
            @Override
            public void onFinish() {
                Toast.makeText(LearnActivity.this, "Finished!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTracing(PointInPolygon pointLocation) {
                Log.d("Point", "Tracing X : " + pointLocation.x + " Y : " + pointLocation.y);
            }
        });

        binding.btnBefore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idx = index-1;
                Characters data = listAksara.get(idx);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(LearnActivity.this, LearnActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
                intent.putExtra("image", data.getImage());
                intent.putExtra("audio", data.getAudio());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(500, 500);
            }
        });
        binding.btnNext.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idx = index+1;
                Characters data = listAksara.get(idx);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(LearnActivity.this, LearnActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
                intent.putExtra("image", data.getImage());
                intent.putExtra("audio", data.getAudio());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(500, 500);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.write_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(LearnActivity.this, CharacterListActivity.class);
            intent.putExtra("jenis", type);
            startActivity(intent);
        } else if (item.getItemId() == R.id.btn_audio) {
            Toast.makeText(this, "Audio played!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setType(){
        Aksara aksara = new Aksara();
        switch (type) {
            case "angka":
                listAksara.addAll(aksara.getAksaraAngka());
                String[] angka = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                index = Arrays.asList(angka).indexOf(romaji);
                break;
            case "carakan":
                listAksara.addAll(aksara.getAksarCarakan());
                String[] carakan = {"ha","na","ca","ra","ka","da","ta","sa","wa","la",
                        "pa","dha","ja","ya","nya","ma","ga","ba","tha","nga"};
                index = Arrays.asList(carakan).indexOf(romaji);
                break;
            case "pasangan":
                listAksara.addAll(aksara.getAksarPasangan());
                String[] pasangan = {"h","n","c","r","k","d","t","s","w","l",
                        "p","dh","j","y","ny","m","g","b","th","ng"};
                index = Arrays.asList(pasangan).indexOf(romaji);
                break;
            case "swara":
                listAksara.addAll(aksara.getAksarSwara());
                String[] swara = {"a", "i", "u", "e", "o"};
                index = Arrays.asList(swara).indexOf(romaji);
                break;
        }
    }
}
