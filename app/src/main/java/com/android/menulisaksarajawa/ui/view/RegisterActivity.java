package com.android.menulisaksarajawa.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.databinding.ActivityRegisterBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    JSONParser jsonParser=new JSONParser();
    private ArrayAdapter<String> adapter = null;
    private String kelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String[] ClassItem = {"VIII A", "VIII B", "VIII C", "VIII D", "VIII E", "VIII F"};

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ClassItem);
        binding.spClass.setAdapter(adapter);
        registerUser();

        binding.tvLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkNetwork()){
                    Toast.makeText(RegisterActivity.this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
                } else {
                    //initialize data
                    String name = binding.etName.getText().toString().trim();
                    binding.spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            kelas = adapter.getItem(position).trim();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            TextView errorText = (TextView) binding.spClass.getSelectedItem();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);
                            errorText.setText("Kelas belum dipilih!");
                        }
                    });

                    String username = binding.etUsername.getText().toString().trim();
                    String password = binding.etPassword.getText().toString().trim();

                    //data validation
                    if (name.isEmpty()) {
                        binding.etName.setError("Nama tidak boleh kosong!");
                    } else if (username.isEmpty()) {
                        binding.etUsername.setError("Username tidak boleh kosong!");
                    } else if (password.isEmpty()) {
                        binding.etPassword.setError("Password tidak boleh kosong!");
                    } else if (password.length() < 6) {
                        binding.etPassword.setError("Password minimal terdiri dari 6 karakter!");
                    } else {
                        class AuthRegister extends AsyncTask<Void, Void, JSONObject> {

                            ProgressDialog loading;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(RegisterActivity.this, "Daftar...", "Tunggu...", false, false);
                            }

                            @Override
                            protected void onPostExecute(JSONObject result) {
                                super.onPostExecute(result);
                                try {
                                    if (result.getInt("status") == 1) {
                                        Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Register gagal!", Toast.LENGTH_LONG).show();
                                    }
                                    loading.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected JSONObject doInBackground(Void... v) {
                                ArrayList params = new ArrayList();
                                params.add(new BasicNameValuePair("name", name));
                                params.add(new BasicNameValuePair("kelas", kelas));
                                params.add(new BasicNameValuePair("username", username));
                                params.add(new BasicNameValuePair("password", password));
                                params.add(new BasicNameValuePair("role", "Siswa"));

                                return jsonParser.makeHttpRequest(Config.URL_REGISTER, "POST", params);
                            }
                        }

                        AuthRegister ar = new AuthRegister();
                        ar.execute();
                    }
                }
            }
        });
    }
    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }
}