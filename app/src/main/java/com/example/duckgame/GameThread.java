package com.example.duckgame;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.LinkedList;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";

    private final double TICKRATE = 60; // Amount of times the game updates each second
    private final double TIME_PER_TICK = 1000000000 / TICKRATE; // Amount of time between each update tick in nanoseconds
    private final int MAX_FRAMESKIP = 10; // Maximum number of times for the game to skip drawing

    private SurfaceHolder surfaceHolder;
    private GraphicsView graphicsView;
    public static Canvas canvas;

    private boolean running;
    private boolean paused;
    private boolean launched = false;
    private long prevTime; // Previous recorded time in nanoseconds
    private long deltaTime; // Difference between previous recorded time and current time in nanoseconds
    private int framesSkipped; // Amount of drawing frames that have been skipped to update level objects

    private GameWorld level;

    GameThread(SurfaceHolder surfaceHolder, GraphicsView graphicsView, LinkedList<GameObject> levelObjects, PointF levelSize){
        super();
        this.surfaceHolder = surfaceHolder;
        this.graphicsView = graphicsView;
        // Create new world using given specifications by levelsActivity
        level = new GameWorld(levelObjects, levelSize);
    }

    @Override
    public void run(){
        paused = false;
        deltaTime = 0;
        while (running){
            prevTime = System.nanoTime();

            framesSkipped = 0;
            /* Keep updating active objects as long as the time passed since the last cycle exceeds the time needed
               to pass for an update loop, until the number of drawing frames skipped exceeds a set value. */
            while (deltaTime >= TIME_PER_TICK && framesSkipped < MAX_FRAMESKIP){
                // Update all active objects on the level
                level.tick(TIME_PER_TICK);
                deltaTime -= TIME_PER_TICK;
                framesSkipped++;
                Log.i(TAG, "Executed Tick");
            }

            // Draw all objects on the level
            try {
                canvas = this.surfaceHolder.lockCanvas();
                if (canvas == null) break;
                level.draw(graphicsView, canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            } catch (IllegalArgumentException e) {
                break;
            }

            if (!paused) {
                deltaTime += System.nanoTime() - prevTime;
                Log.i(TAG, "deltaTime: " + deltaTime);
            }
        }
    }

    public void pauseGame(){paused = true;}
    public void resumeGame(){paused = false;}

    public void launchPlayer(PointF coords){
        launched = true;
        level.launchPlayer(coords);
    }

    public void aimPlayer(PointF coords){
        level.aimPlayer(coords);
    }

    /** Properties **/

    public void setRunning(boolean running){
        this.running = running;
    }
    public boolean getPaused(){
        return paused;
    }
    public boolean getLaunched(){ return launched;}
}
