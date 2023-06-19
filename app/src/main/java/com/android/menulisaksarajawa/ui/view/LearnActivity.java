package com.android.menulisaksarajawa.ui.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LearnActivity extends AppCompatActivity {
    private ActivityLearnBinding binding;
    CanvasView canvas;
    ArrayList<Characters> listAksara = new ArrayList();
    private String romaji, type;
    private Integer aksara, image, audio, index = 1;

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
                .load(aksara)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.myanimation);

        index = listAksara.indexOf(new Characters(aksara, romaji, image, audio));
        Toast.makeText(getApplicationContext(), aksara.longValue() + " " + romaji + " " + image + " " +audio, Toast.LENGTH_SHORT).show();
        int listSize = listAksara.size()-1;

        if(index == 0){
            binding.btnBefore.setVisibility(View.GONE);
        } else if(index == listSize){
            binding.btnNext.setVisibility(View.GONE);
        }


        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        canvas = binding.letter;
        canvas.setLetterChar(romaji);
        canvas.setPointColor(R.color.gray);
        canvas.setTracingListener(new CanvasView.TracingListener() {
            @Override
            public void onFinish() {
                Toast.makeText(LearnActivity.this, "Selesai!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTracing(PointInPolygon pointLocation) {
                Log.d("Point", "Tracing X : " + pointLocation.x + " Y : " + pointLocation.y);
            }
        });

        binding.btnBefore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index-1;
                Characters data = listAksara.get(index);
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
                index = index+1;
                Characters data = listAksara.get(index);
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

    public void setType(){
        Aksara aksara = new Aksara();
        if(type.equals("angka")){
            listAksara.addAll(aksara.getAksaraAngka());
        } else if(type.equals("carakan")){
            listAksara.addAll(aksara.getAksarCarakan());
        } else if(type.equals("pasangan")){
            listAksara.addAll(aksara.getAksarPasangan());
        } else if(type.equals("swara")){
            listAksara.addAll(aksara.getAksarSwara());
        }
    }
}
