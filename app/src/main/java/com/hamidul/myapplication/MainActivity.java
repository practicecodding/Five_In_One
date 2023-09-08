package com.hamidul.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button,cancel,exit;
    TextView toolbar;
    Animation left_to_right,zoom_in,zoom_out;
    TextToSpeech toSpeech;
    int anim = 0, left = 0;
    BroadcastReceiver broadcastReceiver;
    LinearLayout playTune;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //======================================================================================================================

        broadcastReceiver = new ConnectionReceiver();
        registerNetwork();

        if (mediaPlayer!=null) mediaPlayer.release();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("https://smhamidulcodding.000webhostapp.com/mp3/Alone_Sad_Ringtone.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        toolbar = findViewById(R.id.toolbar);
        left_to_right = AnimationUtils.loadAnimation(MainActivity.this,R.anim.left_to_right);
        zoom_in = AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoom_in);
        zoom_out = AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoom_out);
        playTune = findViewById(R.id.playTune);
        toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        toolbar.setAnimation(left_to_right);

        editText.setOnTouchListener(touchListener);

        editText.addTextChangedListener(watcher);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toSpeech.speak(editText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null, null);
                }
            }
        });

        playTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer!=null) mediaPlayer.release();

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource("https://smhamidulcodding.000webhostapp.com/mp3/Alone_Sad_Ringtone.mp3");
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });




    }//====================================================================================================================

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String s1 = editText.getText().toString();
            button.setEnabled(!s1.isEmpty());
            if (before>count){
                toSpeech.stop();
            }else {
                toSpeech.stop();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length()>0 && s.toString().startsWith("0")){
                s.delete(0,1);
            }

        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            toolbar.setText("EditText");
            if (anim==0){
                toolbar.startAnimation(zoom_in);
                anim=1;
            }
            return false;
        }
    };

    private void alertDialog(){
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        cancel = view.findViewById(R.id.cancel);
        exit = view.findViewById(R.id.exit);

        final  AlertDialog alertDialog = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        toSpeech.stop();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        alertDialog();
    }

    protected void registerNetwork(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unRegisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterNetwork();
    }


}//*************************************************************************************************************