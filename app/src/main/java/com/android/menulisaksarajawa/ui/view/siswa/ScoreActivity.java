package com.android.menulisaksarajawa.ui.view.siswa;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityScoreBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.model.NilaiHistory;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.GuideActivity;
import com.android.menulisaksarajawa.ui.view.MainActivity;
import com.android.menulisaksarajawa.ui.view.guru.NilaiDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;
    PrefManager prefManager;
    JSONParser jsonParser=new JSONParser();

    private String carakan, pasangan, swara, angka, kata, id_user, kelas, nama;
    private String id_carakan, id_pasangan, id_swara, id_angka, id_kata;

    private  MenuItem noteMenu;
    private  Menu menu;
    private Dialog noteDialog;
    private EditText edNotes;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_circle);

        noteDialog = new Dialog(this);
        prefManager = new PrefManager(this);
        if(prefManager.getSPRole().equals("Siswa")){
            id_user = prefManager.getSPId();
        } else {
            nama = getIntent().getStringExtra("nama");
            id_user = getIntent().getStringExtra("id_user");
            kelas = getIntent().getStringExtra("kelas");

            binding.toolbar.setTitle("Daftar Nilai "+nama);
        }

        getNilai();
        getNotes();

        binding.nilaiCarakan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent move = new Intent(ScoreActivity.this, NilaiHistoryActivity.class);
                move.putExtra("title", getIntent().getStringExtra("title"));
                move.putExtra("nama", nama);
                move.putExtra("kelas", kelas);
                move.putExtra("id_user", id_user);
                move.putExtra("id_nilai", id_carakan);
                move.putExtra("id_jenis", "CAR");
                move.putExtra("jenis", "Carakan");
                startActivity(move);
            }
        });
        binding.nilaiPasangan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent move = new Intent(ScoreActivity.this, NilaiHistoryActivity.class);
                move.putExtra("title", getIntent().getStringExtra("title"));
                move.putExtra("nama", nama);
                move.putExtra("kelas", kelas);
                move.putExtra("id_user", id_user);
                move.putExtra("id_nilai", id_pasangan);
                move.putExtra("id_jenis", "PAS");
                move.putExtra("jenis", "Pasangan");
                startActivity(move);
            }
        });
        binding.nilaiSwara.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent move = new Intent(ScoreActivity.this, NilaiHistoryActivity.class);
                move.putExtra("title", getIntent().getStringExtra("title"));
                move.putExtra("nama", nama);
                move.putExtra("kelas", kelas);
                move.putExtra("id_user", id_user);
                move.putExtra("id_nilai", id_swara);
                move.putExtra("id_jenis", "SWA");
                move.putExtra("jenis", "Swara");
                startActivity(move);
            }
        });
        binding.nilaiAngka.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent move = new Intent(ScoreActivity.this, NilaiHistoryActivity.class);
                move.putExtra("title", getIntent().getStringExtra("title"));
                move.putExtra("nama", nama);
                move.putExtra("kelas", kelas);
                move.putExtra("id_user", id_user);
                move.putExtra("id_nilai", id_angka);
                move.putExtra("id_jenis", "ANG");
                move.putExtra("jenis", "Angka");
                startActivity(move);
            }
        });
        binding.nilaiKata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent move = new Intent(ScoreActivity.this, NilaiHistoryActivity.class);
                move.putExtra("title", getIntent().getStringExtra("title"));
                move.putExtra("nama", nama);
                move.putExtra("kelas", kelas);
                move.putExtra("id_user", id_user);
                move.putExtra("id_nilai", id_kata);
                move.putExtra("id_jenis", "KTA");
                move.putExtra("jenis", "Kata");
                startActivity(move);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        this.menu = menu;
        if(prefManager.getSPRole().equals("Siswa")) {
            menu.removeItem(R.id.btn_add_notes);
        } else {
            this.noteMenu = menu.findItem(R.id.btn_add_notes);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(prefManager.getSPRole().equals("Siswa")) {
                startActivity(new Intent(ScoreActivity.this, MainActivity.class));
            } else if(prefManager.getSPRole().equals("Guru")) {
                Intent move = new Intent(ScoreActivity.this, NilaiDetail.class);
                move.putExtra("title", getIntent().getStringExtra("title"));
                move.putExtra("kelas", kelas);
                startActivity(move);
            }
        } else if (item.getItemId() == R.id.btn_add_notes) {
            noteDialog.setContentView(R.layout.notes_dialog);
            Button btnSubmit = noteDialog.findViewById(R.id.btn_submit);
            edNotes = noteDialog.findViewById(R.id.ed_notes);
            noteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noteDialog.show();

            btnSubmit.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    String notes = edNotes.getText().toString();
                    if (notes.isEmpty()) {
                        edNotes.setError("Catatan tidak boleh kosong");
                    } else {
                        addNotes();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
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
                        if (result.getString("status").equals("1")) {
                            JSONArray data = result.getJSONArray("data");

                            id_angka = data.getJSONObject(0).getString("id_nilai");
                            id_carakan = data.getJSONObject(1).getString("id_nilai");
                            id_pasangan = data.getJSONObject(2).getString("id_nilai");
                            id_swara = data.getJSONObject(3).getString("id_nilai");
                            id_kata = data.getJSONObject(4).getString("id_nilai");

                            angka = data.getJSONObject(0).getString("nilai");
                            carakan = data.getJSONObject(1).getString("nilai");
                            pasangan = data.getJSONObject(2).getString("nilai");
                            swara = data.getJSONObject(3).getString("nilai");
                            kata = data.getJSONObject(4).getString("nilai");

                            String start_angka = data.getJSONObject(0).getString("createdAt");
                            String start_carakan = data.getJSONObject(1).getString("createdAt");
                            String start_pasangan = data.getJSONObject(2).getString("createdAt");
                            String start_swara = data.getJSONObject(3).getString("createdAt");
                            String start_kata = data.getJSONObject(4).getString("createdAt");

                            String last_angka = data.getJSONObject(0).getString("modifiedAt");
                            String last_carakan = data.getJSONObject(1).getString("modifiedAt");
                            String last_pasangan = data.getJSONObject(2).getString("modifiedAt");
                            String last_swara = data.getJSONObject(3).getString("modifiedAt");
                            String last_kata = data.getJSONObject(4).getString("modifiedAt");

                            binding.tvScoreAngka.setText(angka + "/10");
                            binding.tvScoreCarakan.setText(carakan + "/20");
                            binding.tvScorePasangan.setText(pasangan + "/20");
                            binding.tvScoreSwara.setText(swara + "/5");
                            binding.tvScoreKata.setText(kata + "/2");

//                            binding.startAngka.setText(start_angka.equals("null") ? "Mulai : -" : "Mulai : " + start_angka);
//                            binding.startCarakan.setText(start_carakan.equals("null") ? "Mulai : -" : "Mulai : " + start_carakan);
//                            binding.startPasangan.setText(!start_pasangan.equals("null") ? "Mulai : " + start_pasangan : "Mulai : -");
//                            binding.startSwara.setText(!start_swara.equals("null") ? "Mulai : " + start_swara : "Mulai : -");
//
//                            binding.lastAngka.setText(!last_angka.equals("null") ? "Akhir : " + last_angka : "Akhir : -");
//                            binding.lastCarakan.setText(!last_carakan.equals("null") ? "Akhir : " + last_carakan : "Akhir : -");
//                            binding.lastPasangan.setText(!last_pasangan.equals("null") ? "Akhir : " + last_pasangan : "Akhir : -");
//                            binding.lastSwara.setText(!last_swara.equals("null") ? "Akhir : " + last_swara : "Akhir : -");

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
                    params.add(id_user);

                    JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NILAI_USER, "GET", params);
                    return json;
                }
            }

            GetNilai na = new GetNilai();
            na.execute();
        }
    }

    private void getNotes(){
        if (!checkNetwork()){
            Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
        } else {
            class GetNotes extends AsyncTask<Void, Void, JSONObject> {
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
                        if (result.getString("status").equals("1")) {
                            JSONArray data = result.getJSONArray("data");

                            if(data.length() > 0) {
                                String notes = "";
                                for(int i=0; i<data.length(); i++) {
                                    notes += i+1 + ". " +data.getJSONObject(i).getString("notes") + "\n";
                                }
                                binding.notes.setText(notes);
                            } else {
                                binding.cardNotes.setVisibility(View.GONE);
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
                    params.add(id_user);

                    JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NOTES_BY_USER, "GET", params);
                    return json;
                }
            }

            GetNotes na = new GetNotes();
            na.execute();
        }
    }

    private void addNotes(){
        if (!checkNetwork()){
            Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
        } else {
            class AddNotes extends AsyncTask<Void, Void, JSONObject> {
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
                        if (result.getString("status").equals("1")) {
                            noteDialog.dismiss();

                            Intent intent = getIntent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
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

                    params.add(new BasicNameValuePair("id_user", id_user));
                    params.add(new BasicNameValuePair("notes", edNotes.getText().toString()));

                    return jsonParser.makeHttpRequest(Config.URL_ADD_NOTES, "POST", params);
                }
            }

            AddNotes na = new AddNotes();
            na.execute();
        }
    }

    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }
}