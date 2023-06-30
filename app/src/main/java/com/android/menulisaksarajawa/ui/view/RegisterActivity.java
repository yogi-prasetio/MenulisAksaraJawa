package com.android.menulisaksarajawa.ui.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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
                //initialize data
                String name = binding.etName.getText().toString().trim();
                String kelas = binding.etKelas.getText().toString().trim();
                String username = binding.etUsername.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                //data validation
                if (name.isEmpty()) {
                    binding.etName.setError("Email tidak boleh kosong!");
                } else if (kelas.isEmpty()){
                    binding.etKelas.setError("Email tidak boleh kosong!");
                } else if (username.isEmpty()){
                    binding.etUsername.setError("Email tidak boleh kosong!");
                } else if (password.isEmpty()){
                    binding.etPassword.setError("Password tidak boleh kosong!");
                } else if (password.length() < 6){
                    binding.etPassword.setError("Password minimal terdiri dari 6 karakter!");
                } else {
                    class AuthRegister extends AsyncTask<Void,Void,JSONObject> {

                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(RegisterActivity.this,"Daftar...","Tunggu...",false,false);
                        }

                        @Override
                        protected void onPostExecute(JSONObject result) {
                            super.onPostExecute(result);
                            try {
                                if (result.getInt("status") == 1) {
                                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Register failed!", Toast.LENGTH_LONG).show();
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
        });
    }
}