package com.android.menulisaksarajawa.ui.view;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.menulisaksarajawa.ui.model.Aksara;
import com.android.menulisaksarajawa.ui.model.Characters;
import com.android.menulisaksarajawa.ui.model.Users;
import com.android.menulisaksarajawa.ui.model.pip.PointInPolygon;
import com.android.menulisaksarajawa.ui.utils.LetterFactory;
import com.android.menulisaksarajawa.ui.utils.LetterStrokeBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    private String romaji, type, userId;
    private int aksara, image, audio, index = 1;
    private  MenuItem indicatorMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgLetter.setImageBitmap(letterBitmap);
        type = getIntent().getStringExtra("type");
        aksara = getIntent().getIntExtra("aksara", 0);
        romaji = getIntent().getStringExtra("romaji");
        image = getIntent().getIntExtra("image", 0);
        audio = getIntent().getIntExtra("audio", 0);
//        userId = getUserDetail();


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
        index = listAksara.indexOf(new Characters(image, romaji, image, audio));
        Integer listSize = listAksara.size()-1;
        result = new ArrayList();

        if (index == 0) {
            binding.btnBefore.setVisibility(View.GONE);
        } else if (index == listSize) {
            binding.btnNext.setVisibility(View.GONE);
        }
        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gesture = binding.gestureOverlay;
        gesture.addOnGestureListener(this);
        onGesturePerformed(gesture.getGesture());

//        gesture.addOnGestureListener( { gesture ->
//                this.onGesturePerformed(gesture)
//        });

//        gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures_rya);
        gLibrary.load();

        binding.btnBefore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Characters data = listAksara.get(index - 1);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(PracticeActivity.this, PracticeActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
                intent.putExtra("image", data.getImage());
                intent.putExtra("audio", data.getAudio());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(500, 500);
            }
        });
        binding.btnNext.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Characters data = listAksara.get(index - 1);
                finish();
                overridePendingTransition(500, 500);
                Intent intent = new Intent(PracticeActivity.this, PracticeActivity.class);
                intent.putExtra("aksara", data.getAksara());
                intent.putExtra("romaji", data.getRomaji());
                intent.putExtra("image", data.getImage());
                intent.putExtra("audio", data.getAudio());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(500, 500);
            }
        });
    }
//
//    @Override
//    public void onStart() {
//        super.onStart()
//        database!!.orderByChild("id").equalTo(userId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                /**
//                 * Saat ada data baru, masukkan datanya ke ArrayList
//                 */
//                userData = ArrayList()
//                for (noteDataSnapshot in dataSnapshot.children) {
//                    val user: Users? = noteDataSnapshot.getValue(Users::class.java)
//                    userData!!.add(user!!)
//                }
//                if (userData!!.isEmpty()) {
//                    Toast.makeText(this@TestActivity, "Data is Empty!", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                println(databaseError.details + " " + databaseError.message)
//            }
//        })
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return true;
    }

    private void onGesturePerformed(Gesture gesture) {
        prediction = gLibrary.recognize(gesture);
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
            binding.imgLetter.setImageBitmap(letterBitmap);
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
//        prediction = gLibrary.recognize(gesture);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);
//        if (type == "dakuon" || type == "handakuon" || type == "yoon") {
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

            if (userData == null){
                Toast.makeText(this, "Data Gagal diperbarui! Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
            } else {
                //update data
//                val dbUser: DatabaseReference = database!!.child(userId)
//                val score: Int = userData!![0].score!! + 1
//                val rows: Users = Users(userData!![0].id, userData!![0].nama, userData!![0].kelas, score)
//                dbUser.setValue(rows)
            }
//            menuCheck.isEnabled = false
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

    private void setType(){
        Aksara aksara = new Aksara();
//        switch(type){
//            case "angka" :
//                listAksara.addAll(aksara.getAksaraAngka());
//                break;
//            case "carakan" :
//                listAksara.addAll(aksara.getAksarCarakan());
//                break;
//            case "pasangan" :
//                listAksara.addAll(aksara.getAksarPasangan());
//                break;
//            case "swara" :
//                listAksara.addAll(aksara.getAksarSwara());
//                break;
//        }
    }

    private String getUserDetail() {
//        val mSharedPreferences = getSharedPreferences("CheckUser", MODE_PRIVATE)
//        return mSharedPreferences.getString("user", "")
        return null;
    }
}