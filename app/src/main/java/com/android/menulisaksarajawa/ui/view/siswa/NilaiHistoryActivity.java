package com.android.menulisaksarajawa.ui.view.siswa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityNilaiHistoryBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.model.Aksara;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.model.NilaiHistory;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.adapter.ListAksaraAdapter;
import com.android.menulisaksarajawa.ui.view.adapter.ListNilaiAdapter;
import com.android.menulisaksarajawa.ui.view.adapter.ListNilaiHistoryAdapter;
import com.android.menulisaksarajawa.ui.view.guru.NilaiDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NilaiHistoryActivity extends AppCompatActivity {

    PrefManager prefManager;
    JSONParser jsonParser = new JSONParser();
    private ListNilaiHistoryAdapter adapter;
    private ArrayList<NilaiHistory> list = new ArrayList();
    private String id_nilai, id_jenis, jenis, nilai, start, end, id_user, nama;
    ActivityNilaiHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNilaiHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);

        nama = getIntent().getStringExtra("nama");
        id_user = getIntent().getStringExtra("id_user");
        id_nilai = getIntent().getStringExtra("id_nilai");
        id_jenis = getIntent().getStringExtra("id_jenis");
        jenis = getIntent().getStringExtra("jenis");

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_circle);

        setRecycleView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent move = new Intent(NilaiHistoryActivity.this, ScoreActivity.class);
            move.putExtra("title", getIntent().getStringExtra("title"));
            move.putExtra("nama", getIntent().getStringExtra("nama"));
            move.putExtra("kelas", getIntent().getStringExtra("kelas"));
            move.putExtra("id_user", getIntent().getStringExtra("id_user"));
            startActivity(move);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecycleView(){
        if (!checkNetwork()) {
            Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
        } else {
            class GetNilai extends AsyncTask<Void, Void, JSONObject> {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(NilaiHistoryActivity.this, "Loading...", "Tunggu sebentar...", false, false);
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    super.onPostExecute(result);
                    try {
                        if (result.getString("status").equals("1")) {
                            JSONArray data = result.getJSONArray("data");

                            for(int i=0; i<data.length(); i++) {
                                nilai = data.getJSONObject(i).getString("nilai");
                                start = data.getJSONObject(i).getString("startTime");
                                end = data.getJSONObject(i).getString("endTime");

                                list.add(i, new NilaiHistory(nilai, start, end));
                            }
                            binding.rvNilaiHistory.setLayoutManager(new LinearLayoutManager(NilaiHistoryActivity.this));

                            if(list.size() == 0){
                                binding.rvNilaiHistory.setVisibility(View.GONE);
                                binding.ivEmpty.setVisibility(View.VISIBLE);
                                binding.textEmpty.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                        }
                        loading.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected JSONObject doInBackground(Void... v) {
                    ArrayList params = new ArrayList();
                    params.add(id_nilai);

                    JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NILAI_HISTORY, "GET", params);
                    return json;
                }
            }

            GetNilai na = new GetNilai();
            na.execute();
        }

        binding.rvNilaiHistory.setHasFixedSize(true);
        adapter = new ListNilaiHistoryAdapter(list);
        binding.rvNilaiHistory.setAdapter(adapter);
        adapter.setJenis(jenis);

//        Toast.makeText(getApplicationContext(), String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
//        if(list.size() == 0){
//            binding.rvNilaiHistory.setVisibility(View.GONE);
//            binding.ivEmpty.setVisibility(View.VISIBLE);
//            binding.textEmpty.setVisibility(View.VISIBLE);
//        }
    }

    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }


}