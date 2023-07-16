package com.android.menulisaksarajawa.ui.view.siswa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
    private int aksara, image, audio, index = 1, listSize = 0;
    private  MenuItem indicatorMenu;
    private  Menu menu;

    PrefManager prefManager;
    JSONParser jsonParser=new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
//        binding.imgLetter.setImageBitmap(letterBitmap);
        type = getIntent().getStringExtra("type");

        aksara = getIntent().getIntExtra("aksara", 0);
        romaji = getIntent().getStringExtra("romaji");
//        image = getIntent().getIntExtra("image", 0);
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

        setType();
        getNilai();

        listSize = listAksara.size()-1;
        result = new ArrayList<>();

        binding.btnNext.setVisibility(View.GONE);
        if (index == 0) {
            binding.btnBefore.setVisibility(View.GONE);
        } else if (index == listSize) {
            binding.btnNext.setVisibility(View.GONE);
        }

        getSupportActionBar().setTitle("Menulis Aksara " +romaji.toUpperCase());
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLetterChar(romaji);

        gesture = binding.gestureOverlay;
        gesture.addOnGestureListener(this);
        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {

            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                PracticeActivity.this.onGesturePerformed(overlay, gesture);
            }
        });

//        gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_angka);
//        gLibrary.load();

        binding.btnBefore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Characters data = listAksara.get(index - 1);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(PracticeActivity.this, PracticeActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
//                intent.putExtra("image", data.getImage());
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
//                intent.putExtra("image", data.getImage());
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
        this.indicatorMenu = menu.findItem(R.id.indikator_nilai);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PracticeActivity.this, CharacterListActivity.class);
            intent.putExtra("jenis", type);
            startActivity(intent);
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
                letterFactory.getLetterAssets(),
                letterFactory.getStrokeAssets()
        );
    }

    private void initializeLetterAssets(
            String letterAssets,
            String strokeAssets
    ) {
        InputStream stream = null;
        try {
            letterBitmap = getBitmapByAssetName(letterAssets);
//            binding.imgLetter.setImageBitmap(letterBitmap);
            stream = getAssets().open(strokeAssets);
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

        if (prediction.size() < 1 || prediction.get(0).score <= 3.0) {
            ab.setTitle("Salah");
            ab.setMessage("huruf yang Anda tulis tidak tepat.");
            ab.setPositiveButton("Coba lagi", null);
            ab.show();
        } else if (prediction.get(0).name.equals(romaji)) {
            ab.setTitle("Benar");
            ab.setMessage("huruf yang Anda tulis sudah tepat.");
            ab.setPositiveButton("OK", null);
            ab.show();

            //update nilai
            class GetNilai extends AsyncTask<Void, Void, JSONObject> {

                ProgressDialog loading;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.canvas.setVisibility(View.GONE);
//                loading = ProgressDialog.show(PrakticeActivity.this, "Fetching...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    super.onPostExecute(result);
                    try {
                        if (result.getInt("status") == 1) {
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
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

                    JSONObject json = jsonParser.makeHttpRequest(Config.URL_UPDATE_NILAI, "POST", params);
                    return json;
                }
            }

            GetNilai na = new GetNilai();
            na.execute();
        } else {
            ab.setTitle("Salah");
            ab.setMessage("aksara yang Anda tulis tidak tepat");
            ab.setPositiveButton("Coba lagi", null);
            ab.show();
        }
//            menuCheck.isEnabled = false
//        }
//        else {
//            when {
//                result!!.contains("Outside") -> {
//                    ab.setTitle("Salah")
//                    ab.setMessage("huruf yang Anda tulis tidak tepat.")
//                    ab.setPositiveButton("Coba lagi", null)
//                    ab.show()
//                    result!!.clear()
//                    menuCheck.isEnabled = false
//                }
//                result!!.isEmpty() -> {
//                    ab.setTitle("Salah")
//                    ab.setMessage("huruf yang Anda tulis tidak tepat.")
//                    ab.setPositiveButton("Coba lagi", null)
//                    ab.show()
//                    result!!.clear()
//                    menuCheck.isEnabled = false
//                }
//                else -> {
//                    if (predictions.size < 1 || predictions[0].score <= 3.0) {
//                        ab.setTitle("Salah")
//                        ab.setMessage("huruf yang Anda tulis tidak tepat.")
//                        ab.setPositiveButton("Coba lagi", null)
//                        ab.show()
//                    } else if (predictions[0].name.equals(romaji)) {
//                        ab.setTitle("Benar")
//                        ab.setMessage("huruf yang Anda tulis sudah tepat.")
//                        ab.setPositiveButton("OK", null)
//                        ab.show()
//
//                        if (userData == null){
//                            Toast.makeText(this, "Data Gagal diperbarui! Tidak ada koneksi internet!", Toast.LENGTH_LONG).show()
//                        } else {
//                            //update data
//                            val dbRef: DatabaseReference = database!!.child(userId)
//                            val score: Int = userData!![0].score!! + 1
//                            val rows: Users = Users(userData!![0].id, userData!![0].nama, userData!![0].kelas, score)
//                            dbRef.setValue(rows)
//                        }
//                    } else {
//                        ab.setTitle("Salah")
//                        ab.setMessage("huruf yang Anda tulis tidak tepat")
//                        ab.setPositiveButton("Coba lagi", null)
//                        ab.show()
//                    }
//                    menuCheck.isEnabled = false
//                    result!!.clear()
//                }
//            }
//        }
    }

    private void onTracing(PointInPolygon pointLoc){
        Log.i("X :", pointLoc.x.toString());
        Log.i("Y :", pointLoc.y.toString());
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
            case "angka":
                listAksara.addAll(aksara.getAksaraAngka());
                String[] angka = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                index = Arrays.asList(angka).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_angka);
                gLibrary.load();
                break;
            case "carakan":
                listAksara.addAll(aksara.getAksarCarakan());
                String[] carakan = {"ha","na","ca","ra","ka","da","ta","sa","wa","la",
                        "pa","dha","ja","ya","nya","ma","ga","ba","tha","nga"};
                index = Arrays.asList(carakan).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_carakan);
                gLibrary.load();
                break;
            case "pasangan":
                listAksara.addAll(aksara.getAksarPasangan());
                String[] pasangan = {"h","n","c","r","k","d","t","s","w","l",
                        "p","dh","j","y","ny","m","g","b","th","ng"};
                index = Arrays.asList(pasangan).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_pasangan);
                gLibrary.load();
                break;
            case "swara":
                listAksara.addAll(aksara.getAksarSwara());
                String[] swara = {"a", "i", "u", "e", "o"};
                index = Arrays.asList(swara).indexOf(romaji);
                gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_swara);
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
//                loading = ProgressDialog.show(PracticeActivity.this,"Loading...","Tunggu sebentar...",false,false);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    if (result.getInt("status") == 1) {
                        JSONArray data = result.getJSONArray("data");
                        switch (type) {
                            case "angka":
                                nilai = data.getJSONObject(0).getString("nilai").concat("/10");
                                break;
                            case "carakan":
                                nilai = data.getJSONObject(1).getString("nilai").concat("/20");
                                break;
                            case "pasangan":
                                nilai = data.getJSONObject(2).getString("nilai").concat("/20");
                                break;
                            case "swara":
                                nilai = data.getJSONObject(3).getString("nilai").concat("/5");
                                break;
                        }
                        indicatorMenu = menu.findItem(R.id.indikator_nilai);
                        indicatorMenu.setTitle(nilai);
                        indikator = this.nilai;
                        int idx = index + 1;
                        String nil = String.valueOf(nilai.charAt(0));
                        Log.e("INDEXES", nil+" = "+idx);
                        if(Integer.valueOf(nil) < idx) {
                            Log.e("INDEX", nil+" = "+idx);
                            binding.btnNext.setVisibility(View.GONE);
                        } else {
                            binding.btnNext.setVisibility(View.VISIBLE);
                        }
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
                params.add(prefManager.getSPId());

                JSONObject json = jsonParser.makeHttpRequest(Config.URL_GET_NILAI_USER, "GET", params);
                return json;
            }
        }

        GetNilai na = new GetNilai();
        na.execute();
    }
}