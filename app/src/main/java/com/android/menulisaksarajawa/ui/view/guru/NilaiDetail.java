package com.android.menulisaksarajawa.ui.view.guru;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.NilaiDetailBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.model.Nilai;
import com.android.menulisaksarajawa.ui.view.adapter.ListNilaiAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NilaiDetail extends AppCompatActivity {
    private NilaiDetailBinding binding;

    private ListNilaiAdapter adapter;
    private List<Nilai> list = new ArrayList<>();
    private JSONParser jsonParser = new JSONParser();
    private String kelas;
    private String start_angka, start_carakan, start_pasangan, start_swara;
    private String last_angka, last_carakan, last_pasangan, last_swara;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NilaiDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDialog = new Dialog(this);

        kelas = getIntent().getStringExtra("kelas");
        String title = getIntent().getStringExtra("title");

        getSupportActionBar().setTitle("Nilai Kelas " + title);
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        if (!checkNetwork()) {
            Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
        } else {
            GetNilai();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nilai_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_reset) {
            if (!checkNetwork()) {
                Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setCancelable(false);

                ab.setTitle("Reset Nilai");
                ab.setMessage("Apakah Anda yakin akan mereset semua nilai?");
                ab.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteNilai();
                    }
                });
                ab.setNegativeButton("Tidak", null);
                ab.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }

    private void GetNilai(){
        class GetNilai extends AsyncTask<Void, Void, JSONObject> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NilaiDetail.this, "Loading...", "Tunggu sebentar...", false, false);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    if (result.getInt("status") == 1) {
                        JSONArray data = result.getJSONArray("data");
                        JSONArray dataNilai = null;

                        String name = "", angka = "0", carakan = "0", pasangan = "0", swara = "0", jenis;
                        int total = 0;
                        for (int i = 0; i < data.length(); i++) {
                            dataNilai = data.getJSONArray(i);
                            for (int j = 0; j < dataNilai.length(); j++) {
                                name = dataNilai.getJSONObject(j).getString("nama_user");
                                jenis = dataNilai.getJSONObject(j).getString("jenis");
                                switch (jenis) {
                                    case "Angka":
                                        angka = dataNilai.getJSONObject(j).getString("nilai");
                                        start_angka = dataNilai.getJSONObject(j).getString("createdAt");
                                        last_angka = dataNilai.getJSONObject(j).getString("modifiedAt");
                                        break;
                                    case "Carakan":
                                        carakan = dataNilai.getJSONObject(j).getString("nilai");
                                        start_carakan = dataNilai.getJSONObject(j).getString("createdAt");
                                        last_carakan = dataNilai.getJSONObject(j).getString("modifiedAt");
                                        break;
                                    case "Pasangan":
                                        pasangan = dataNilai.getJSONObject(j).getString("nilai");
                                        start_pasangan = dataNilai.getJSONObject(j).getString("createdAt");
                                        last_pasangan = dataNilai.getJSONObject(j).getString("modifiedAt");
                                        break;
                                    case "Swara":
                                        swara = dataNilai.getJSONObject(j).getString("nilai");
                                        start_swara = dataNilai.getJSONObject(j).getString("createdAt");
                                        last_swara = dataNilai.getJSONObject(j).getString("modifiedAt");
                                        break;
                                }
                                total = (Integer.parseInt(angka)) + (Integer.parseInt(carakan)) + (Integer.parseInt(pasangan)) + (Integer.parseInt(swara));
                            }
                            list.add(new Nilai(name, angka, carakan, pasangan, swara, String.valueOf(total), start_angka, start_carakan, start_pasangan, start_swara, last_angka, last_carakan, last_pasangan, last_swara));
                            Log.i("NILAI USER", angka + " : " + carakan + " : " + total);
                        }
                        binding.rvNilai.setLayoutManager(new GridLayoutManager(NilaiDetail.this, 1));
                        binding.rvNilai.setHasFixedSize(true);
                        Collections.sort(list, new NilaiComparator());
                        Gson gson = new Gson();

                        Log.e("DATA", gson.toJson(list));

                        adapter = new ListNilaiAdapter(list);
                        binding.rvNilai.setAdapter(adapter);

                        adapter.setOnItemClickCallback(new ListNilaiAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(Nilai data) {
                                mDialog.setContentView(R.layout.nilai_dialog);
                                mDialog.setCancelable(true);

                                TextView carakan = mDialog.findViewById(R.id.carakan);
                                TextView pasangan = mDialog.findViewById(R.id.pasangan);
                                TextView swara = mDialog.findViewById(R.id.swara);
                                TextView angka = mDialog.findViewById(R.id.angka);
                                TextView total = mDialog.findViewById(R.id.total);

                                TextView st_carakan = mDialog.findViewById(R.id.st_carakan);
                                TextView st_pasangan = mDialog.findViewById(R.id.st_pasangan);
                                TextView st_swara = mDialog.findViewById(R.id.st_swara);
                                TextView st_angka = mDialog.findViewById(R.id.st_angka);

                                TextView ls_carakan = mDialog.findViewById(R.id.lt_carakan);
                                TextView ls_pasangan = mDialog.findViewById(R.id.lt_pasangan);
                                TextView ls_swara = mDialog.findViewById(R.id.lt_swara);
                                TextView ls_angka = mDialog.findViewById(R.id.lt_angka);

                                carakan.setText(data.getCarakan());
                                pasangan.setText(data.getPasangan());
                                swara.setText(data.getSwara());
                                angka.setText(data.getAngka());
                                total.setText(data.getTotal());

                                st_angka.setText(!data.getStart_angka().equals("null") ? "Mulai : " + data.getStart_angka() : "Mulai : -");
                                st_carakan.setText(!data.getStart_carakan().equals("null") ? "Mulai : " + data.getStart_carakan() : "Mulai : -");
                                st_pasangan.setText(!data.getStart_pasangan().equals("null") ? "Mulai : " + data.getStart_pasangan() : "Mulai : -");
                                st_swara.setText(!data.getStart_swara().equals("null") ? "Mulai : " + data.getStart_swara() : "Mulai : -");

                                ls_angka.setText(!data.getLast_angka().equals("null") ? "Akhir : " + data.getLast_angka() : "Akhir : -");
                                ls_carakan.setText(!data.getLast_carakan().equals("null") ? "Akhir : " + data.getLast_carakan() : "Akhir : -");
                                ls_pasangan.setText(!data.getLast_pasangan().equals("null") ? "Akhir : " + data.getLast_pasangan() : "Akhir : -");
                                ls_swara.setText(!data.getLast_swara().equals("null") ? "Akhir : " + data.getLast_swara() : "Akhir : -");

                                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                mDialog.show();
                            }
                        });
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

    private void DeleteNilai(){
        class DeleteNilai extends AsyncTask<Void, Void, JSONObject> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NilaiDetail.this, "Loading...", "Tunggu sebentar...", false, false);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    if (result.getInt("status") == 1) {
                        Toast.makeText(getApplicationContext(), "Reset Nilai Berhasil!", Toast.LENGTH_LONG).show();
                        list.clear();
                        GetNilai();
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

                JSONObject json = jsonParser.makeHttpRequest(Config.URL_DELETE_NILAI, "GET", params);
                return json;
            }
        }

        DeleteNilai na = new DeleteNilai();
        na.execute();
    }

    class NilaiComparator implements java.util.Comparator<Nilai>{
        @Override
        public int compare(Nilai a, Nilai b) {
            Log.d("COMPARE", a.getTotal() +" : "+ b.getTotal());
            return Integer.parseInt(b.getTotal()) - Integer.parseInt(a.getTotal());
        }
    }
}