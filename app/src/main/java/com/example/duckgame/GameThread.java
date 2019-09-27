package com.example.duckgame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.LinkedList;

public class GameThread extends Thread {

    private final double TICKRATE = 60; // Amount of times the game updates each second
    private final double TIME_PER_TICK = 1000000 / TICKRATE; // Amount of time between each update tick in nanoseconds
    private final int MAX_FRAMESKIP = 10;

    private SurfaceHolder surfaceHolder;
    private GraphicsView graphicsView;
    public static Canvas canvas;

    private boolean running;
    private long prev; // Previous recorded time in nanoseconds
    private long deltaTime; // Difference between previous recorded time and current time in nanoseconds
    private int framesSkipped; // Amount of drawing frames that have been skipped to update level objects

    private GameWorld level;

    public GameThread(SurfaceHolder surfaceHolder, GraphicsView graphicsView, LinkedList<GameObject> gameObjects){
        super();
        this.surfaceHolder = surfaceHolder;
        this.graphicsView = graphicsView;
        //level = new GameWorld(gameObjects);
    }

    @Override
    public void run(){
        while (running){
            prev = System.nanoTime();
            deltaTime = 0L;

            framesSkipped = 0;
            while (deltaTime >= TIME_PER_TICK && framesSkipped < MAX_FRAMESKIP){
                //level.update(deltaTime);
                deltaTime -= TIME_PER_TICK;
                framesSkipped++;
            }

            //canvas = this.surfaceHolder.lockCanvas();
            //level.draw();
            //surfaceHolder.unlockCanvasAndPost(canvas);

            deltaTime = System.nanoTime() - prev;
        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}
