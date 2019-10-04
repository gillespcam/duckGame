package com.example.duckgame;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ScoresActivity extends AppCompatActivity {

    private final String TAG = "ScoresActivity";

    private SoundPool menuSounds;
    private int tapSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Enable Fullscreen
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        FrameLayout ContentView = findViewById(R.id.scores);
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

        String[] levelNames = getResources().getStringArray(R.array.level_names);
        ScoreListAdapter adapter = new ScoreListAdapter(this, R.layout.scores_list_item, levelNames);
        ListView ScoresList = findViewById(R.id.ScoreList);
        ScoresList.setAdapter(adapter);
    }

    public void onClickButtonBack(View view) {
        menuSounds.play(tapSound, 1.0f, 1.0f, 0, 0, 1);
        finish();
        overridePendingTransition(0, 0);
    }
}
