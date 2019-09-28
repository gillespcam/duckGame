package com.example.duckgame;

import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private GraphicsView graphicsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Enable Fullscreen
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        FrameLayout ContentView = findViewById(R.id.game);
        ContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Create LevelData for current game
        TypedArray levels = getResources().obtainTypedArray(R.array.levels);
        // Get the TypedArray for the chosen level
        TypedArray levelData = getResources().obtainTypedArray(levels.getResourceId(getIntent().getIntExtra("id", 0),0));
        LevelBlueprint levelBlueprint = new LevelBlueprint(levelData);

        // Create new instance of GraphicsView with selected level specifications
        graphicsView = new GraphicsView(this, new LinkedList<GameObject>(), new PointF(16, 8));
        graphicsView.setZOrderOnTop(true);
        graphicsView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        ContentView.addView(graphicsView);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        GameThread game = graphicsView.getGameThread();
        if (!game.getPaused()) game.pauseGame();
        else game.resumeGame();
    }

    @Override
    protected void onPause(){
        super.onPause();
        graphicsView.getGameThread().pauseGame();
    }

    @Override
    protected void onResume(){
        super.onResume();
        graphicsView.getGameThread().resumeGame();
    }
}