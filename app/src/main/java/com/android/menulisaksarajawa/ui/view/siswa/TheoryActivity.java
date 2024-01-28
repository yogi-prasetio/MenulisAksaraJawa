package com.android.menulisaksarajawa.ui.view.siswa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityTheoryBinding;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

public class TheoryActivity extends AppCompatActivity {

    private ActivityTheoryBinding binding;
    PrefManager prefManager;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor spEditor;
    JSONParser jsonParser=new JSONParser();
    private Dialog mDialog;
    private boolean guide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getApplicationContext().getSharedPreferences("Guide", Context.MODE_PRIVATE);
        spEditor = sharedPref.edit();
        prefManager = new PrefManager(this);

        guide = sharedPref.getBoolean("guideTheory", true);

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.warning_dialog);
        mDialog.setCancelable(false);
//
//        if(guide){
//            info();
//        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_circle);
        binding.tvAngkaTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Angka");
                startActivity(move);;
            }
        });

        binding.tvCarakanTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Carakan");
                startActivity(move);;
            }
        });

        binding.tvPasanganTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Pasangan");
                startActivity(move);;
            }
        });

        binding.tvSwaraTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent move = new Intent(TheoryActivity.this, CharacterListActivity.class);
                move.putExtra("type", "theory");
                move.putExtra("jenis", "Swara");
                startActivity(move);;
            }
        });
    }

    private void info(){
        new TapTargetSequence(TheoryActivity.this)
                .targets(
                        TapTarget.forView(
                                        binding.tvCarakanTitle,
                                        "Button",
                                        "Tekan tombol untuk menampilkan daftar Aksara Carakan\n"
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(150),
//                        binding.tvPasanganTitle.getParent().requestChildFocus(binding.tvPasanganTitle, binding.tvPasanganTitle)
                        TapTarget.forView(
                                        binding.tvPasanganTitle,
                                        "Button",
                                        "Tekan tombol untuk menampilkan daftar Aksara Pasangan\n"
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.66f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(150),
                        TapTarget.forView(
                                        binding.tvSwaraTitle,
                                        "Button",
                                        "Tekan tombol untuk menampilkan daftar Aksara Swara\n"
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.46f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(175),
                        TapTarget.forView(
                                        binding.tvAngkaTitle,
                                        "Button Angka",
                                        "Tekan tombol untuk menampilkan daftar Aksara Angka\n"
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.76f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(100)
                ).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        if(guide) {
                            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            mDialog.show();
                            TextView btnOke = mDialog.findViewById(R.id.btn_oke);
                            CheckBox mCheckBox = mDialog.findViewById(R.id.checkBox);
                            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (buttonView.isChecked()) {
                                        spEditor.putBoolean("guideThoery", false);
                                        spEditor.commit();
                                    }
                                }
                            });
                            btnOke.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) { }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) { }
                }).start();
    }
}