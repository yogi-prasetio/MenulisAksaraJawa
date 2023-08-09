package com.android.menulisaksarajawa.ui.view.siswa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityCharacterListBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.model.Aksara;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.adapter.ListAksaraAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class CharacterListActivity extends AppCompatActivity {
    private ActivityCharacterListBinding binding;
    private ListAksaraAdapter adapter;
    private ArrayList<Characters> list = new ArrayList();
    private String type, jenis, types;
    private String[] angka, carakan, pasangan, swara;
    private int index=0;
    PrefManager prefManager;
    JSONParser jsonParser=new JSONParser();
    private int[] nilai = new int[1];

    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCharacterListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        prefManager = new PrefManager(this);
        mDialog = new Dialog(this);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("WriteTypes", Context.MODE_PRIVATE);
        type = sharedPref.getString("write", "");
        jenis = getIntent().getStringExtra("jenis");
        types = getIntent().getStringExtra("type");

        if(type.equals("learn")) {
            binding.toolbar.setTitle("Belajar Menulis Aksara Jawa");
        } else if(type.equals("test")) {
            binding.toolbar.setTitle("Latihan Menulis Aksara Jawa");
        } else {
            binding.toolbar.setTitle("Daftar Aksara "+jenis);
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        setRecycleView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            list.clear();
            if (type.equals("learn") || type.equals("test")) {
                startActivity(new Intent(CharacterListActivity.this, TypesActivity.class));
            } else {
                startActivity(new Intent(CharacterListActivity.this, TheoryActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView() {
        Aksara aksara = new Aksara();
        switch (jenis) {
            case "Angka":
                binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
                list.addAll(aksara.getAksaraAngka());
                angka = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                break;
            case "Carakan":
                binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
                list.addAll(aksara.getAksarCarakan());
                carakan = new String[]{"ha", "na", "ca", "ra", "ka", "da", "ta", "sa", "wa", "la",
                        "pa", "dha", "ja", "ya", "nya", "ma", "ga", "ba", "tha", "nga"};
                break;
            case "Pasangan":
                binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
                list.addAll(aksara.getAksarPasangan());
                pasangan = new String[]{"h", "n", "c", "r", "k", "d", "t", "s", "w", "l",
                        "p", "dh", "j", "y", "ny", "m", "g", "b", "th", "ng"};
                break;
            case "Swara":
                binding.rvAksara.setLayoutManager(new GridLayoutManager(this, 5));
                list.addAll(aksara.getAksarSwara());
                swara = new String[]{"a", "i", "u", "e", "o"};
                break;
        }
        binding.rvAksara.setHasFixedSize(true);
    }

    private void setRecycleView(){
        if(type.equals("test")){
            if (!checkNetwork()){
                Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
                binding.progressBar.setVisibility(View.GONE);
            } else {
                class GetNilai extends AsyncTask<Void, Void, JSONObject> {

                    ProgressBar loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onPostExecute(JSONObject result) {
                        super.onPostExecute(result);
                        try {
                            if (result.getInt("status") == 1) {
                                JSONArray data = result.getJSONArray("data");
                                switch (jenis) {
                                    case "Angka":
                                        nilai[0] = Integer.parseInt(data.getJSONObject(0).getString("nilai"));
                                        break;
                                    case "Carakan":
                                        nilai[0] = Integer.parseInt(data.getJSONObject(1).getString("nilai"));
                                        break;
                                    case "Pasangan":
                                        nilai[0] = Integer.parseInt(data.getJSONObject(2).getString("nilai"));
                                        break;
                                    case "Swara":
                                        nilai[0] = Integer.parseInt(data.getJSONObject(3).getString("nilai"));
                                        break;
                                }
                                Log.e("NILAI", String.valueOf(nilai[0]));
                                adapter.setNilai(nilai[0], type);
                                setView();
                            } else {
                                Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            binding.progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected JSONObject doInBackground(Void... v) {
                        ArrayList params = new ArrayList();
                        params.add(prefManager.getSPId());

                        JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NILAI_USER, "GET", params);
                        return json;
                    }
                }

                GetNilai na = new GetNilai();
                na.execute();
            }
        } else {
            binding.progressBar.setVisibility(View.GONE);
            setView();
        }

        adapter = new ListAksaraAdapter(list);
        binding.rvAksara.setAdapter(adapter);
        adapter.setNilai(nilai[0], type);

        adapter.setOnItemClickCallback(new ListAksaraAdapter.OnItemClickCallback() {
            public void onItemClicked(Characters data){
                switch (jenis) {
                    case "Angka":
                        index = Arrays.asList(angka).indexOf(data.getRomaji());
                        break;
                    case "Carakan":
                        index = Arrays.asList(carakan).indexOf(data.getRomaji());
                        break;
                    case "Pasangan":
                        index = Arrays.asList(pasangan).indexOf(data.getRomaji());
                        break;
                    case "Swara":
                        index = Arrays.asList(swara).indexOf(data.getRomaji());
                        break;
                }
                Log.e("INDEX", String.valueOf(index));

                if(type.equals("learn")){
                    Intent move = new Intent(CharacterListActivity.this, LearnActivity.class);
                    move.putExtra("image", data.getImage());
                    move.putExtra("aksara", data.getAksara());
                    move.putExtra("romaji", data.getRomaji());
                    move.putExtra("audio", data.getAudio());
                    move.putExtra("character", String.valueOf(data));
                    move.putExtra("type", jenis);
                    startActivity(move);
                } else if(type.equals("test")){
                    Log.e("CEK NILAI", String.valueOf(nilai[0]));
                    if(nilai[0]< index){
                        Toast.makeText(CharacterListActivity.this, "Maaf, Anda belum menyelesaikan huruf yang sebelumnya!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent move = new Intent(CharacterListActivity.this, PracticeActivity.class);
                        move.putExtra("image", data.getImage());
                        move.putExtra("aksara", data.getAksara());
                        move.putExtra("romaji", data.getRomaji());
                        move.putExtra("audio", data.getAudio());
                        move.putExtra("type", jenis);
                        startActivity(move);
                    }
                } else {
                    MediaPlayer audio = MediaPlayer.create(CharacterListActivity.this, data.getAudio());

                    mDialog.setContentView(R.layout.char_detail_dialog);
                    mDialog.setCancelable(false);
                    ImageView img = mDialog.findViewById(R.id.imgChar_detail);
                    TextView aksara = mDialog.findViewById(R.id.aksara);
                    aksara.setText(data.getRomaji());
                    Glide.with(getApplicationContext())
                            .load(data.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(img);
                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mDialog.show();

                    ImageButton btnClose = mDialog.findViewById(R.id.btn_close);
                    ImageButton btnAudio = mDialog.findViewById(R.id.btn_audio);

                    if (jenis.equals("Pasangan")){
                        btnAudio.setVisibility(View.GONE);
                    }

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                    btnAudio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            audio.start();
                        }
                    });
//                    btnAudio.setOnClickListener { audio.start() };
                }
            }
        });
    }

    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }
}