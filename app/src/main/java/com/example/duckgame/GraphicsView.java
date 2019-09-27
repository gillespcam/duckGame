package com.example.duckgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.LinkedList;

public class GraphicsView extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "GraphicsView";

    int screenWidth;
    int screenHeight;
    private LinkedList<GameObject> gameObjects = new LinkedList<>();
    private GameThread game;

    public GraphicsView (Context context, LinkedList<GameObject> gameObjects) {
        super(context);
        getHolder().addCallback(this);
        game = new GameThread(getHolder(), this, gameObjects);
        setFocusable(true);
    }

    public void draw(LinkedList<GameObject> gameObjects, PointF levelsize){
        this.gameObjects = gameObjects;
        // Calculate multiplier for dimensions and positions of bitmaps based on screenWidth & screenHeight
        //...
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Commence game loop
        Log.i(TAG, "Starting GameThread");
        game.setRunning(true);
        game.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Clean shutdown of GameThread
        boolean retry = true;
        while (retry) {
            try {
                game.setRunning(false);
                game.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
