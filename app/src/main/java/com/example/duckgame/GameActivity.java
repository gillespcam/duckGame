package com.example.duckgame;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        FrameLayout ContentView = findViewById(R.id.game);
        ContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        GraphicsView graphicsView = new GraphicsView(this, new LinkedList<GameObject>());
        graphicsView.setZOrderOnTop(true);
        graphicsView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        ContentView.addView(graphicsView);
    }
}