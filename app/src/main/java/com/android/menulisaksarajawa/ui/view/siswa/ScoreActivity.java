package com.android.menulisaksarajawa.ui.view.siswa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityScoreBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;
    PrefManager prefManager;
    JSONParser jsonParser=new JSONParser();

    private String carakan, pasangan, swara, angka;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        prefManager = new PrefManager(this);
        getNilai();
    }

    private void getNilai(){
        if (!checkNetwork()){
            Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
        } else {
            class GetNilai extends AsyncTask<Void, Void, JSONObject> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(ScoreActivity.this, "Loading...", "Tunggu sebentar...", false, false);
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    super.onPostExecute(result);
                    try {
                        if (result.getInt("status") == 1) {
                            JSONArray data = result.getJSONArray("data");
                            angka = data.getJSONObject(0).getString("nilai");
                            carakan = data.getJSONObject(1).getString("nilai");
                            pasangan = data.getJSONObject(2).getString("nilai");
                            swara = data.getJSONObject(3).getString("nilai");

                            String start_angka = data.getJSONObject(0).getString("createdAt");
                            String start_carakan = data.getJSONObject(1).getString("createdAt");
                            String start_pasangan = data.getJSONObject(2).getString("createdAt");
                            String start_swara = data.getJSONObject(3).getString("createdAt");

                            String last_angka = data.getJSONObject(0).getString("modifiedAt");
                            String last_carakan = data.getJSONObject(1).getString("modifiedAt");
                            String last_pasangan = data.getJSONObject(2).getString("modifiedAt");
                            String last_swara = data.getJSONObject(3).getString("modifiedAt");

                            binding.tvScoreAngka.setText(angka + "/10");
                            binding.tvScoreCarakan.setText(carakan + "/20");
                            binding.tvScorePasangan.setText(pasangan + "/20");
                            binding.tvScoreSwara.setText(swara + "/5");

                            binding.startAngka.setText(start_angka.equals("null") ? "Mulai : -" : "Mulai : " + start_angka);
                            binding.startCarakan.setText(start_carakan.equals("null") ? "Mulai : -" : "Mulai : " + start_carakan);
                            binding.startPasangan.setText(!start_pasangan.equals("null") ? "Mulai : " + start_pasangan : "Mulai : -");
                            binding.startSwara.setText(!start_swara.equals("null") ? "Mulai : " + start_swara : "Mulai : -");

                            binding.lastAngka.setText(!last_angka.equals("null") ? "Akhir : " + last_angka : "Akhir : -");
                            binding.lastCarakan.setText(!last_carakan.equals("null") ? "Akhir : " + last_carakan : "Akhir : -");
                            binding.lastPasangan.setText(!last_pasangan.equals("null") ? "Akhir : " + last_pasangan : "Akhir : -");
                            binding.lastSwara.setText(!last_swara.equals("null") ? "Akhir : " + last_swara : "Akhir : -");

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
                    params.add(prefManager.getSPId());

                    JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NILAI_USER, "GET", params);
                    return json;
                }
            }

            GetNilai na = new GetNilai();
            na.execute();
        }
    }
    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }
}