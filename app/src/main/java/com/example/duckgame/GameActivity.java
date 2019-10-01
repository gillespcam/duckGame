package com.example.duckgame;

import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private GraphicsView graphicsView;
    private GameThread game;

    private ImageButton buttonPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttonPause = findViewById(R.id.ButtonPause);

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
        int levelID = getIntent().getIntExtra("id", 0);
        TypedArray levelData = getResources().obtainTypedArray(levels.getResourceId(levelID, 0));
        // Create blueprint of the level that can be used to replicate a GameWorld anytime
        LevelBlueprint levelBlueprint = new LevelBlueprint(this, levelID, levelData);

        // Create new instance of GraphicsView with selected level specifications
        graphicsView = new GraphicsView(this, levelBlueprint);
        game = graphicsView.getGameThread();
        FrameLayout gameView = findViewById(R.id.GameView);
        gameView.addView(graphicsView);
    }

    public void onClickButtonPause(View view){
        if (!game.getPaused()) {
            game.pauseGame();
            buttonPause.setImageResource(R.drawable.button_resume);
        }
        else {
            game.resumeGame();
            buttonPause.setImageResource(R.drawable.button_pause);
        }
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