package com.example.duckgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private SoundPool menuSounds;
    private int tapSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        FrameLayout ContentView = findViewById(R.id.main);
        ContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        menuSounds = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()).setMaxStreams(4).build();
        tapSound = menuSounds.load(this, R.raw.quack, 1);
    }

    public void onClickButtonPlay(View view){
        menuSounds.play(tapSound, 1.0f, 1.0f, 0, 0, 1);
        Intent intent = new Intent(this, LevelsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void onClickButtonScores(View view){
        menuSounds.play(tapSound, 1.0f, 1.0f, 0, 0, 1);
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
