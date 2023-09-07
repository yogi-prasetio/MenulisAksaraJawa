package com.android.menulisaksarajawa.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.databinding.ActivityLoginBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.android.menulisaksarajawa.ui.view.guru.NilaiActivity;
import com.android.menulisaksarajawa.ui.view.siswa.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    JSONParser jsonParser=new JSONParser();
    PrefManager prefManager;
    private String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        prefManager = new PrefManager(this);
        loginCheck();

        binding.btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (!checkNetwork()){
                    Toast.makeText(LoginActivity.this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
                } else {
                    validation();
                }
            }
        });
        binding.tvRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void login(){
        class AuthLogin extends AsyncTask<Void,Void,JSONObject> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Login...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    if (result.getInt("status") == 1) {
                        Toast.makeText(getApplicationContext(), "Login berhasil!",Toast.LENGTH_LONG).show();
                        JSONArray data = result.getJSONArray("data");
                        JSONObject user = data.getJSONObject(0);
                        String id = user.getString("id_user");
                        String name = user.getString("nama_user");
                        String kelas = user.getString("kelas");
                        String username = user.getString("username");
                        String role = user.getString("role");
                        prefManager.saveSPBoolean(PrefManager.IS_LOGIN, true);
                        prefManager.saveSPString(PrefManager.SES_ID, id);
                        prefManager.saveSPString(PrefManager.SES_NAMA, name);
                        prefManager.saveSPString(PrefManager.SES_KELAS, kelas);
                        prefManager.saveSPString(PrefManager.SES_USERNAME, username);
                        prefManager.saveSPString(PrefManager.SES_ROLE, role);
                        if(role.equals("Guru")){
                            startActivity(new Intent(LoginActivity.this, NilaiActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login gagal!", Toast.LENGTH_LONG).show();
                    }
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(Void... v) {
                ArrayList params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                JSONObject json = jsonParser.makeHttpRequest(Config.URL_LOGIN, "POST", params);
                return json;
            }
        }

        AuthLogin ae = new AuthLogin();
        ae.execute();
    }

    private void validation(){
        username = binding.etUsername.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();

        if(username.isEmpty()){
            binding.etUsername.setError("Username tidak boleh kosong!");
        } else if(password.isEmpty()){
            binding.etPassword.setError("Password tidak boleh kosong!");
        } else {
            login();
        }
    }

    private void loginCheck(){
        if(prefManager.loginStatus()){
            if(prefManager.getSPRole().equals("Siswa")){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, NilaiActivity.class));
            }
        }
    }

    private boolean checkNetwork() {
        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }
}