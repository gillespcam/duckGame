package com.example.duckgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";

    private final double TICKRATE = 60; // Amount of times the game updates each second
    private final double TIME_PER_TICK = 1000000000 / TICKRATE; // Amount of time between each update tick in nanoseconds
    private final int MAX_FRAMESKIP = 10; // Maximum number of times for the game to skip drawing

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
        SharedPreferences persistentScores = graphicsView.getContext().getSharedPreferences("highScores", Context.MODE_PRIVATE);
        Editor scores = persistentScores.edit();

        String key = String.valueOf(levelBlueprint.getLevelID());
        ArrayList<String> levelScores = new ArrayList<>();

        Set<String> set = persistentScores.getStringSet(key, new HashSet<String>());
        levelScores.addAll(set);

        if (levelScores.size() == 5) {
            for (int i = 0; i < levelScores.size(); i++) {
                if (Integer.parseInt(levelScores.get(i)) < score) {
                    if (i < 1) levelScores.set(i + 4, levelScores.get(i + 3));
                    if (i < 2) levelScores.set(i + 3, levelScores.get(i + 2));
                    if (i < 3) levelScores.set(i + 2, levelScores.get(i + 1));
                    if (i < 4) levelScores.set(i + 1, levelScores.get(i));
                    levelScores.set(0, String.valueOf(score));
                }
            }
        }
        else {
            levelScores.add(String.valueOf(score));
            Collections.sort(levelScores);
        }

        set = new HashSet<>();
        set.addAll(levelScores);
        scores.putStringSet(key, set);
        scores.apply();
    }

    public void endGame(int score){
        ((GameActivity)graphicsView.getContext()).endGame(score);
    }
    public void pauseGame(){paused = true;}
    public void resumeGame(){paused = false;}

    /** Properties **/

    public void setRunning(boolean running){
        this.running = running;
    }
    public void setLaunched(boolean launched) { this.launched = launched; }
    public boolean getPaused(){
        return paused;
    }
    public boolean getLaunched(){ return launched;}
}
