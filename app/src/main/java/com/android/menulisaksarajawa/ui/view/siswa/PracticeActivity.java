package com.android.menulisaksarajawa.ui.view.siswa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.databinding.ActivityPracticeBinding;
import com.android.menulisaksarajawa.ui.database.Config;
import com.android.menulisaksarajawa.ui.database.JSONParser;
import com.android.menulisaksarajawa.ui.model.Aksara;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.model.Users;
import com.android.menulisaksarajawa.ui.model.pip.PointInPolygon;
import com.android.menulisaksarajawa.ui.utils.LetterFactory;
import com.android.menulisaksarajawa.ui.utils.LetterStrokeBean;
import com.android.menulisaksarajawa.ui.utils.PrefManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class PracticeActivity extends AppCompatActivity implements GestureOverlayView.OnGestureListener {
    private ActivityPracticeBinding binding;
    private ArrayList<Users> userData;
    private ArrayList<String> result;
    private LetterStrokeBean strokeBean;
    private int currentStroke = 0;
    private Bitmap letterBitmap;
    private PointInPolygon pointLocation;

    private ArrayList<Characters> listAksara = new ArrayList();
    private ArrayList<Prediction> prediction = new ArrayList();
    private GestureLibrary gLibrary;
    private GestureOverlayView gesture;

    private String romaji, type, userId, indikator;
    private int aksara, image, audio, index = 1, listSize = 0, nilai_proses;
    private  MenuItem indicatorMenu;
    private  Menu menu;

    PrefManager prefManager;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor spEditor;
    JSONParser jsonParser=new JSONParser();
    private Dialog mDialog;
    private boolean guide;
    private String id_jenis, role, start, end, id_nilai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getApplicationContext().getSharedPreferences("Test", Context.MODE_PRIVATE);
        spEditor = sharedPref.edit();
        prefManager = new PrefManager(this);

        guide = sharedPref.getBoolean("guide", true);
        start = sharedPref.getString("start", "");
        String tmp_nilai = sharedPref.getString("nilai", "");
        if(tmp_nilai.equals("")){
            nilai_proses = 0;
        } else {
            nilai_proses = Integer.parseInt(tmp_nilai);
        }

        role = prefManager.getSPRole();

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.warning_dialog);
        mDialog.setCancelable(false);

        binding.imgLetter.setImageBitmap(letterBitmap);
        type = getIntent().getStringExtra("type");

        aksara = getIntent().getIntExtra("aksara", 0);
        romaji = getIntent().getStringExtra("romaji");
        audio = getIntent().getIntExtra("audio", 0);

        InputStream assets = null;
        try {
            assets = getAssets().open("letters/"+romaji+".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap img = BitmapFactory.decodeStream(assets);
        binding.myanimation.setImageBitmap(img);
        binding.tvCharInfo.setText(romaji);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                if(sharedPref.getString("start", "").equals("")) {
                    spEditor.putString("start", dateFormat.format(date));
                    spEditor.commit();
                }
                Save();
            }
        });

        setType();
        if(role.equals("Siswa")) {
            if(!type.equals("Kata")) {
                getNilai();
            }
        }

        listSize = listAksara.size()-1;
        result = new ArrayList<>();

        if(role.equals("Siswa")) {
            binding.btnNext.setVisibility(View.GONE);
            if (index == listSize) {
                binding.btnNext.setVisibility(View.GONE);
            }
        } else if(role.equals("Guru")) {
            binding.btnSave.setVisibility((View.GONE));
            if (index == 0) {
                binding.btnBefore.setVisibility(View.GONE);
            } else if (index == listSize) {
                binding.btnBefore.setVisibility(View.VISIBLE);
                binding.btnNext.setVisibility(View.GONE);
            } else {
                binding.btnNext.setVisibility(View.VISIBLE);
                binding.btnBefore.setVisibility(View.VISIBLE);
            }
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Menulis Aksara " +romaji.toUpperCase());
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_circle);
        setLetterChar(romaji);

        gesture = binding.gestureOverlay;
        gesture.addOnGestureListener(this);
        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {

            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                PracticeActivity.this.onGesturePerformed(overlay, gesture);
            }
        });

        binding.btnBefore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Characters data = listAksara.get(index - 1);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(PracticeActivity.this, PracticeActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
                intent.putExtra("audio", data.getAudio());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(500, 500);
            }
        });
        binding.btnNext.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Characters data = listAksara.get(index + 1);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(PracticeActivity.this, PracticeActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
                intent.putExtra("audio", data.getAudio());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(500, 500);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        this.menu = menu;
        if(role.equals("Guru") || type.equals("Kata")) {
            menu.removeItem(R.id.indikator_nilai);
        } else {
            this.indicatorMenu = menu.findItem(R.id.indikator_nilai);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(!sharedPref.getString("start", "").equals("")) {
                Save();
            } else {
                Intent intent = new Intent(PracticeActivity.this, CharacterListActivity.class);
                intent.putExtra("jenis", type);
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.btn_info) {
            if(role.equals("Siswa")) {
                infoStart();
            } else {
                info();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        prediction = gLibrary.recognize(gesture);
        CheckDraw();
    }

    @Override
    public void onGestureStarted(GestureOverlayView p0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        List points = strokeBean.getCurrentStrokePoints(currentStroke);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ACTION", "DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                pointLocation = new PointInPolygon(x, y, points);
                onTracing(pointLocation);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("ACTION", "UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("ACTION", "UP");
                break;
        }
    }

    @Override
    public void onGesture(GestureOverlayView p0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        List points = strokeBean.getCurrentStrokePoints(currentStroke);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ACTION", "DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                pointLocation = new PointInPolygon(x, y, points);
                onTracing(pointLocation);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("ACTION", "UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("ACTION", "UP");
                break;
        }
    }
    @Override
    public void onGestureEnded(GestureOverlayView p0, MotionEvent event) {

    }

    @Override
    public void onGestureCancelled(GestureOverlayView p0, MotionEvent event) { }

    private void setLetterChar(String letterChar) {
        LetterFactory letterFactory = new LetterFactory();
        letterFactory.setLetter(letterChar);
        initializeLetterAssets(
                letterFactory.getPolygonAssets()
        );
    }

    private void initializeLetterAssets(
            String polygonAssets
    ) {
        InputStream stream = null;
        try {
            InputStream assets = null;
            try {
                assets = getAssets().open("aksara/"+romaji+"_bg.png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Bitmap img = BitmapFactory.decodeStream(assets);
            binding.imgLetter.setImageBitmap(img);
            stream = getAssets().open(polygonAssets);
            Gson gson = new Gson();
            strokeBean = gson.fromJson(new InputStreamReader(stream), LetterStrokeBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getBitmapByAssetName(String path) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(path);
            is = assetFileDescriptor.createInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void CheckDraw() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        if(start.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            spEditor.putString("start", dateFormat.format(date));
            spEditor.commit();
        }

        if (prediction.size() < 1 || prediction.get(0).score <= 3.0) {
            ab.setTitle("Salah");
            ab.setMessage("aksara yang Anda tulis tidak tepat.");
            ab.setPositiveButton("Coba lagi", null);
            ab.show();
            result.clear();
        } else if (prediction.get(0).name.equals(romaji)) {

            class UpdateNilai extends AsyncTask<Void, Void, JSONObject> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.canvas.setVisibility(View.GONE);
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    super.onPostExecute(result);
                    try {
                        if (result.getString("status").equals("1")) {
                            int nilai = Integer.parseInt(indikator);

                            int nilaiTmp;
                            String tmp_nilai = sharedPref.getString("nilai", "");
                            if(tmp_nilai.equals("")){
                                nilaiTmp = 0;
                            } else {
                                nilaiTmp = Integer.parseInt(tmp_nilai);
                            }
                            spEditor.putString("nilai", String.valueOf(nilaiTmp + 1));
                            spEditor.commit();

                            if((index+1) > nilai) {
                                Toast.makeText(getApplicationContext(), "Nilai berhasil diperbarui!", Toast.LENGTH_LONG).show();
                                indicatorMenu = menu.findItem(R.id.indikator_nilai);
                                indicatorMenu.setTitle((nilai+1)+"/"+listAksara.size());
                            }
                            binding.btnNext.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.canvas.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected JSONObject doInBackground(Void... v) {
                    ArrayList params = new ArrayList();
                    params.add(new BasicNameValuePair("id_user", prefManager.getSPId()));
                    params.add(new BasicNameValuePair("aksara", romaji));
                    params.add(new BasicNameValuePair("id_jenis", id_jenis));

                    JSONObject json = jsonParser.makeHttpRequest(Config.URL_UPDATE_NILAI, "POST", params);
                    return json;
                }
            }
            ArrayList<String> algo = new ArrayList<String>();
            //Huruf yang dicek dengan algoritma
            String[] huruf =  {"ha", "na", "ca", "ra", "h", "n", "c", "a", "0", "1"};
            algo.addAll(Arrays.asList(huruf));

            //Check Tulisan dengan Algo
//            if(algo.contains(romaji)) {
                //Toast.makeText(getApplicationContext(), "with algorithm", Toast.LENGTH_SHORT).show();
//                if (result.contains("Outside")) {
//                    ab.setTitle("Salah");
//                    ab.setMessage("aksara yang Anda tulis tidak tepat.");
//                    ab.setPositiveButton("Coba lagi", null);
//                    ab.show();
//                    result.clear();
//                } else if (result.isEmpty()) {
//                    ab.setTitle("Salah");
//                    ab.setMessage("aksara yang Anda tulis tidak tepat.");
//                    ab.setPositiveButton("Coba lagi", null);
//                    ab.show();
//                    result.clear();
//                } else {
//                    ab.setTitle("Benar");
//                    ab.setMessage("aksara yang Anda tulis sudah tepat.");
//                    ab.setPositiveButton("OK", null);
//                    ab.show();
//
//                    UpdateNilai na = new UpdateNilai();
//                    na.execute();
//                }
//            }else {//Check tulisan tanpa Algo
                //Toast.makeText(getApplicationContext(), "no algorithm", Toast.LENGTH_SHORT).show();
                ab.setTitle("Benar");
                ab.setMessage("aksara yang Anda tulis sudah tepat.");
                ab.setPositiveButton("OK", null);
                ab.show();

                if(role.equals("Siswa")) {
                    UpdateNilai na = new UpdateNilai();
                    na.execute();
                }
//            }

        } else {
            ab.setTitle("Salah");
            ab.setMessage("aksara yang Anda tulis tidak tepat");
            ab.setPositiveButton("Coba lagi", null);
            ab.show();
            result.clear();
        }
    }

    private void onTracing(PointInPolygon pointLoc){
        Log.i("X :", pointLoc.x.toString());
        Log.i("Y :", pointLoc.y.toString());

        //Tampilkan Toast posisi X dan Y
//        Toast.makeText(getBaseContext(), "X: "+pointLoc.x.toString()+ "    Y: "+pointLoc.y.toString()+ "\n", Toast.LENGTH_SHORT).show();

        Log.i("RESULT :", pointLoc.pointInPolygon());
        result.add(pointLoc.pointInPolygon());
    }


    private void setMargins(View view, int left, int top, int right, int bottom) {
//        if (view.getLayoutParams() MarginLayoutParams) {
//            val p = view.layoutParams as MarginLayoutParams
//            p.setMargins(left, top, right, bottom)
//            view.requestLayout()
//        }
    }


    public void setType(){
        Aksara aksara = new Aksara();
        switch (type) {
            case "Angka":
                listAksara.addAll(aksara.getAksaraAngka());
                String[] angka = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                index = Arrays.asList(angka).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_angka);
                gLibrary.load();
                id_jenis = "ANG";
                break;
            case "Carakan":
                listAksara.addAll(aksara.getAksarCarakan());
                String[] carakan = {"ha","na","ca","ra","ka","da","ta","sa","wa","la",
                        "pa","dha","ja","ya","nya","ma","ga","ba","tha","nga"};
                index = Arrays.asList(carakan).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_carakan);
                gLibrary.load();
                id_jenis = "CAR";
                break;
            case "Pasangan":
                listAksara.addAll(aksara.getAksarPasangan());
                String[] pasangan = {"h","n","c","r","k","d","t","s","w","l",
                        "p","dh","j","y","ny","m","g","b","th","ng"};
                index = Arrays.asList(pasangan).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_pasangan);
                gLibrary.load();
                id_jenis = "PAS";
                break;
            case "Swara":
                listAksara.addAll(aksara.getAksarSwara());
                String[] swara = {"a", "i", "u", "e", "o"};
                index = Arrays.asList(swara).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_swara);
                gLibrary.load();
                id_jenis = "SWA";
                break;
            default:
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_angka);
                gLibrary.load();
                break;
        }
    }

    private void getNilai() {

        class GetNilai extends AsyncTask<Void, Void, JSONObject> {

            ProgressDialog loading;
            String nilai;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.canvas.setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    if (result.getString("status").equals("1")) {
                        JSONArray data = result.getJSONArray("data");

                        indicatorMenu = menu.findItem(R.id.indikator_nilai);
                        switch (type) {
                            case "Angka":
                                id_nilai = data.getJSONObject(0).getString("id_nilai");
                                nilai = data.getJSONObject(0).getString("nilai");
                                indicatorMenu.setTitle(nilai+"/10");
                                break;
                            case "Carakan":
                                id_nilai = data.getJSONObject(1).getString("id_nilai");
                                nilai = data.getJSONObject(1).getString("nilai");
                                indicatorMenu.setTitle(nilai+"/20");
                                break;
                            case "Pasangan":
                                id_nilai = data.getJSONObject(2).getString("id_nilai");
                                nilai = data.getJSONObject(2).getString("nilai");
                                indicatorMenu.setTitle(nilai+"/20");
                                break;
                            case "Swara":
                                id_nilai = data.getJSONObject(3).getString("id_nilai");
                                nilai = data.getJSONObject(3).getString("nilai");
                                indicatorMenu.setTitle(nilai+"/5");
                                break;
                        }
                        indikator = this.nilai;
                        int idx = index + 1;
                        String nil = String.valueOf(nilai.charAt(0));
                        Log.e("INDEXES", nil+" = "+idx);

                        if(Integer.parseInt(nil) < idx) {
                            Log.e("INDEX", nil+" = "+idx);
                            binding.btnNext.setVisibility(View.GONE);
                        } else {
                            binding.btnNext.setVisibility(View.VISIBLE);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        binding.canvas.setVisibility(View.VISIBLE);
                        if(guide){
                            if(index == 0) {
                                if(role.equals("Siswa")){
                                    infoStart();
                                } else {
                                    info();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                    }
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

    private void infoStart() {
        new TapTargetSequence(PracticeActivity.this)
                .targets(
                        TapTarget.forView(
                                        binding.canvas,
                                        "Canvas",
                                        "Coret huruf pada canvas dan tunggu coretan sampai menghilang. Kemudian akan muncul dialog hasil penilaian coretan.\n\n\n"
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(250),
                        TapTarget.forToolbarMenuItem(
                                        binding.toolbar,
                                        R.id.indikator_nilai,
                                        "Indikator Nilai",
                                        "Indikator Nilai untuk melihat berapa banyak yang telah anda kerjakan."
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(50)
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
                                        spEditor.putBoolean("guide", false);
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

    private void info() {
        new TapTargetSequence(PracticeActivity.this)
                .targets(
                        TapTarget.forView(
                                        binding.canvas,
                                        "Canvas",
                                        "Coret huruf pada canvas dan tunggu coretan sampai menghilang. Kemudian akan muncul dialog hasil penilaian coretan.\n\n\n"
                                )
                                .outerCircleColor(R.color.mega_mendung).outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white).titleTextSize(20)
                                .titleTextColor(R.color.white).descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .textColor(R.color.white).textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.white).drawShadow(true).cancelable(false)
                                .tintTarget(true).transparentTarget(true).targetRadius(250)
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
                                        spEditor.putBoolean("guide", false);
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

    private void Save(){
        if(prefManager.getSPRole().equals("Siswa")) {
            AlertDialog.Builder confirm = new AlertDialog.Builder(this);
            confirm.setCancelable(false);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            spEditor.putString("end", dateFormat.format(date));
            spEditor.commit();

            confirm.setTitle("Konfirmasi Tes");
            confirm.setMessage("Apakah Anda ingin mengakhiri sesi ini?");
            confirm.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    binding.canvas.setVisibility(View.GONE);
                    binding.btnNext.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);

                    class AddNilaiHistory extends AsyncTask<Void, Void, JSONObject> {

                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            binding.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected void onPostExecute(JSONObject result) {
                            super.onPostExecute(result);
                            try {
                                if (result.getString("status").equals("1")) {
                                    spEditor.remove("start");
                                    spEditor.commit();
                                    spEditor.remove("end");
                                    spEditor.commit();
                                    spEditor.remove("nilai");
                                    spEditor.commit();

                                    binding.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Nilai Berhasil Disimpan!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PracticeActivity.this, CharacterListActivity.class);
                                    intent.putExtra("jenis", type);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Nilai Gagal Disimpan!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        protected JSONObject doInBackground(Void... v) {
                            ArrayList params = new ArrayList();
                            String value = sharedPref.getString("nilai", "");

                            if(value.equals("")){
                                value = "0";
                            } else {
                                value = sharedPref.getString("nilai", "");
                            }
                            params.add(new BasicNameValuePair("id_nilai", id_nilai));
                            params.add(new BasicNameValuePair("nilai", value));
                            params.add(new BasicNameValuePair("start", sharedPref.getString("start", "")));
                            params.add(new BasicNameValuePair("end", sharedPref.getString("end", "")));

                            return jsonParser.makeHttpRequest(Config.URL_ADD_NILAI_HISTORY, "POST", params);
                        }
                    }

                    AddNilaiHistory ar = new AddNilaiHistory();
                    ar.execute();
                }
            });
            confirm.setNegativeButton("Tidak", null);
            confirm.setCancelable(false);
            confirm.show();
        } else {
            Intent intent = new Intent(PracticeActivity.this, CharacterListActivity.class);
            intent.putExtra("jenis", type);
            startActivity(intent);
        }
    }
}