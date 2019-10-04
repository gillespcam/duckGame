package com.example.duckgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";

    private final double TICKRATE = 60; // Amount of times the game updates each second
    private final double TIME_PER_TICK = 1000000000 / TICKRATE; // Amount of time between each update tick in nanoseconds
    private final int MAX_FRAMESKIP = 10; // Maximum number of times for the game to skip drawing

    SharedPreferences highScores; // Dictionary of high scores for each level that remains between launches

    private SurfaceHolder surfaceHolder;
    private GraphicsView graphicsView;
    private LevelBlueprint levelBlueprint;
    private static Canvas canvas;

    private boolean running; // Whether the thread is running or not
    private boolean paused; // Whether the game is ticking or not
    private boolean launched = false; // Whether the player has launched yet or not
    private long prevTime; // Previous recorded time in nanoseconds

    private GameWorld world; // GameWorld that this thread is ticking

    GameThread(SurfaceHolder surfaceHolder, GraphicsView graphicsView, LevelBlueprint levelBlueprint){
        super();
        this.surfaceHolder = surfaceHolder;
        this.graphicsView = graphicsView;
        this.levelBlueprint = levelBlueprint;

        // Create new world using given specifications by levelsBlueprint
        world = levelBlueprint.createGameWorld();
        // Setup new world to be run
        world.setGameThread(this);
        graphicsView.setGameWorld(world);

        // Get high scores
        highScores = graphicsView.getContext().getSharedPreferences("highScores", Context.MODE_PRIVATE);
    }

    @Override
    public void run(){
        paused = false;
        long deltaTime = 0;
        while (running){
            prevTime = System.nanoTime();

            int framesSkipped = 0;
            /* Keep updating active objects as long as the time passed since the last cycle exceeds the time needed
               to pass for an update loop, until the number of drawing frames skipped exceeds a set value */
            while (deltaTime >= TIME_PER_TICK && framesSkipped < MAX_FRAMESKIP){
                // Update all active objects on the world
                world.tick(TIME_PER_TICK);
                deltaTime -= TIME_PER_TICK;
                framesSkipped++;
                // Log.i(TAG, "Executed Tick");
            }

            // Draw all objects on the world
            try {
                canvas = this.surfaceHolder.lockCanvas();
                if (canvas == null) break;
                graphicsView.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            } catch (IllegalArgumentException e) {
                break;
            }

            if (!paused) {
                deltaTime += System.nanoTime() - prevTime;
                // Log.i(TAG, "deltaTime: " + deltaTime);
            }
        }
    }

    public void aimPlayer(PointF coords){
        world.aim(coords);
    }

    public void launchPlayer(PointF coords){
        launched = true;
        world.launch(coords);
    }

    public void writeScore(int score){
        Editor scores = highScores.edit();
        String key = String.valueOf(levelBlueprint.getLevelID());
        if (score > highScores.getInt(key, 0)) scores.putInt(key, score);
        scores.commit();
    }

    public void pauseGame(){paused = true;}
    public void resumeGame(){paused = false;}

    /** Properties **/

    public void setRunning(boolean running){
        this.running = running;
    }
    public boolean getPaused(){
        return paused;
    }
    public boolean getLaunched(){ return launched;}
}
