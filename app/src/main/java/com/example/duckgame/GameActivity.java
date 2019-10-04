package com.example.duckgame;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private GraphicsView graphicsView;
    private GameThread game;

    private ImageView pauseOverlay;
    private TextView scoreDisplay;
    private ImageButton buttonPause;
    private ImageButton buttonRestart;
    private ImageButton buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttonPause = findViewById(R.id.ButtonPause);
        scoreDisplay = findViewById(R.id.ScoreDisplay);
        buttonBack = findViewById(R.id.ButtonBack);
        pauseOverlay = findViewById(R.id.pauseOverlay);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonRestart = findViewById(R.id.buttonRestart);

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
        levels.recycle();
        // Create blueprint of the level that can be used to replicate a GameWorld anytime
        LevelBlueprint levelBlueprint = new LevelBlueprint(this, levelID, levelData);

        // Create new instance of GraphicsView with selected level specifications
        graphicsView = new GraphicsView(this, levelBlueprint);
        game = graphicsView.getGameThread();
        FrameLayout gameView = findViewById(R.id.GameView);
        gameView.addView(graphicsView);
    }

    public void endGame(int score){
        class Disposer implements Runnable {
            int score;
            Disposer(int score) { this.score = score; }
            public void run() {
                onClickButtonPause(buttonPause);
                buttonPause.setVisibility(View.GONE);
                scoreDisplay.setText(String.valueOf(score));
                scoreDisplay.setVisibility(View.VISIBLE);
            }
        }

        runOnUiThread(new Disposer(score));

    }

    public void onClickButtonPause(View view){
        if (!game.getPaused()) {
            game.pauseGame();
            pauseOverlay.setVisibility(View.VISIBLE);
            buttonPause.setImageResource(R.drawable.button_resume);
            buttonRestart.setVisibility(View.VISIBLE);
            buttonBack.setVisibility(View.VISIBLE);
        }
        else {
            game.resumeGame();
            pauseOverlay.setVisibility(View.INVISIBLE);
            buttonPause.setImageResource(R.drawable.button_pause);
            buttonRestart.setVisibility(View.GONE);
            buttonBack.setVisibility(View.GONE);
        }
    }

    public void onClickButtonBack(View view){
        finish();
        overridePendingTransition(0, 0);
    }

    public void onClickButtonRestart(View view){
        recreate();
        overridePendingTransition(0, 0);
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