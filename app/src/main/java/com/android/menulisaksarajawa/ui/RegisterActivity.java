package com.android.menulisaksarajawa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        registerUser();
    }

    private void registerUser() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize data
                String name = binding.etName.getText().toString().trim();
                String kelas = binding.etKelas.getText().toString().trim();
                String emailUser = binding.etUsername.getText().toString().trim();
                String passwordUser = binding.etPassword.getText().toString().trim();

                //data validation
                if (name.isEmpty()) {
                    binding.etName.setError("Email tidak boleh kosong!");
                } else if (kelas.isEmpty()){
                    binding.etKelas.setError("Email tidak boleh kosong!");
                } else if (emailUser.isEmpty()){
                    binding.etUsername.setError("Email tidak boleh kosong!");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()){
                    binding.etUsername.setError("Email tidak valid!");
                } else if (passwordUser.isEmpty()){
                    binding.etPassword.setError("Password tidak boleh kosong!");
                } else if (passwordUser.length() < 6){
                    binding.etPassword.setError("Password minimal terdiri dari 6 karakter!");
                } else {
                    //create user with firebase auth
                    auth.createUserWithEmailAndPassword(emailUser,passwordUser)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //jika gagal register do something
                                    if (!task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,
                                                "Register Failed because: "+ task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }else {
                                        //jika sukses akan menuju ke login activity
                                        Toast.makeText(RegisterActivity.this,"Register Success!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    }
                                }
                            });
                }
            }
        });
    }
}