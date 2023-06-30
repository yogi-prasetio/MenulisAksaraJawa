package com.android.menulisaksarajawa.ui.view.guru;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.menulisaksarajawa.databinding.ActivityNilaiDetailBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.model.Nilai;
import com.android.menulisaksarajawa.ui.view.adapter.ListNilaiAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NilaiDetailActivity extends AppCompatActivity {

    private ActivityNilaiDetailBinding binding;
    private ListNilaiAdapter adapter;
    private ArrayList<Nilai> list = new ArrayList();
    private JSONParser jsonParser = new JSONParser();
    private String kelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNilaiDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        kelas = getIntent().getStringExtra("kelas");

        class GetNilai extends AsyncTask<Void,Void, JSONObject> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NilaiDetailActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    if (result.getInt("status") == 1) {
                        JSONArray data = result.getJSONArray("data");
                        JSONArray dataNilai = null;

                        String name="", angka = "0", carakan = "0", pasangan = "0", swara = "0", jenis;
                        int total = 0;
                        for (int i=0; i<data.length(); i++) {
                            dataNilai = data.getJSONArray(i);
                            for (int j = 0; j < dataNilai.length(); j++) {
                                name = dataNilai.getJSONObject(j).getString("nama_user");
                                jenis = dataNilai.getJSONObject(j).getString("jenis");
                                switch (jenis) {
                                    case "Angka":
                                        angka = dataNilai.getJSONObject(j).getString("nilai");
                                        break;
                                    case "Carakan":
                                        carakan = dataNilai.getJSONObject(j).getString("nilai");
                                        break;
                                    case "Pasangan":
                                        pasangan = dataNilai.getJSONObject(j).getString("nilai");
                                        break;
                                    case "Swara":
                                        swara = dataNilai.getJSONObject(j).getString("nilai");
                                        break;
                                }
                                total = (Integer.parseInt(angka)) + (Integer.parseInt(carakan)) + (Integer.parseInt(pasangan)) + (Integer.parseInt(swara));
                            }
                            list.add(new Nilai(name, angka, carakan, pasangan, swara, String.valueOf(total)));
                            Log.i("NILAI USER", angka + " : " +carakan+ " : " +total );
                        }
                        binding.rvAksara.setLayoutManager(new GridLayoutManager(NilaiDetailActivity.this, 1));
                        binding.rvAksara.setHasFixedSize(true);
                        adapter = new ListNilaiAdapter(list);
                        binding.rvAksara.setAdapter(adapter);
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
                params.add(kelas);

                JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NILAI, "GET", params);
                return json;
            }
        }

        GetNilai na = new GetNilai();
        na.execute();
    }
}