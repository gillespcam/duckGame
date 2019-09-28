package com.example.duckgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.LinkedList;

public class GraphicsView extends SurfaceView implements SurfaceHolder.Callback {


    private final String TAG = "GraphicsView";

    private int screenWidth;
    private int screenHeight;
    float scale; // How big one game unit is in terms of pixels

    private Paint paint;
    private Matrix matrix = new Matrix();
    private Bitmap bitmap;
    private SparseArray<Bitmap> sprites; // Improves performance by loading all the resources outside the draw loop

    private GameThread game;
    private LinkedList<GameObject> gameObjects;
    private PointF gameSize;

    private int launchMotion = -1; // -1 means no launch motion has been initiated

    GraphicsView (Context context, LinkedList<GameObject> levelObjects, PointF levelSize) {
        super(context);
        gameObjects = levelObjects;
        gameSize = levelSize;

        getHolder().addCallback(this);
        game = new GameThread(getHolder(), this, levelObjects, levelSize);
        setFocusable(true);

        // Initialise the paint options
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        // Load all the resources we'll need to be drawing
        sprites = new SparseArray<>();
        sprites.put(R.drawable.goal,  BitmapFactory.decodeResource(getResources(),R.drawable.goal));
        sprites.put(R.drawable.player,  BitmapFactory.decodeResource(getResources(),R.drawable.player));
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        super.draw(canvas);

        //canvas.drawRect(5,5, scale * gameSize.x, scale * gameSize.y, paint);

        // Draw GameWorld objects with updated positions, dimensions etc.
        for (GameObject gameObject : gameObjects){
            bitmap = sprites.get(gameObject.getSprite());
            float spriteScale = gameObject.getScale() * scale / bitmap.getWidth();
            PointF middleCoord = new PointF(bitmap.getWidth() / 2F, bitmap.getHeight() / 2F);
            matrix.setRotate(gameObject.getRotation(), middleCoord.x, middleCoord.y );
            matrix.postScale(spriteScale, spriteScale);
            matrix.postTranslate(gameObject.getPosition().x * scale + 5 - middleCoord.x * spriteScale, gameObject.getPosition().y * scale + 5 - middleCoord.y * spriteScale);


            canvas.drawBitmap(bitmap, matrix, paint);
        }
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
        screenWidth = width - 10;
        screenHeight = height - 10;

        float xscale = screenWidth / gameSize.x;
        float yscale = screenHeight / gameSize.y;

        scale = (xscale < yscale) ? xscale : yscale;
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
                Log.e(TAG, e.getMessage());
            }
            retry = false;
            Log.i(TAG, "Terminating GameThread");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            // if we're not paused or launched, we can start the launch procedure
            if(!game.getPaused() && !game.getLaunched()){
                // no launch motion currently in progress
                if(launchMotion == -1) {
                    launchMotion = event.getPointerId(0);
                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            // if we currently have a launch motion in progress
            if(launchMotion != -1){
                int pointer = event.findPointerIndex(launchMotion);
                float aimx = event.getX(pointer);
                float aimy = event.getY(pointer);
                PointF aimGamePoint = new PointF(aimx / scale, aimy / scale);
                game.aimPlayer(aimGamePoint);
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            // if we currently have a launch motion in progress
            if(launchMotion != -1){
                int pointer = event.findPointerIndex(launchMotion);
                float launchx = event.getX(pointer);
                float launchy = event.getY(pointer);
                launchMotion = -1; // we no longer need the pointer
                PointF launchGamePoint = new PointF(launchx / scale, launchy / scale);
                game.launchPlayer(launchGamePoint);
            }
        }
        return true;
    }

    /** Properties **/

    public GameThread getGameThread(){
        return game;
    }
    public void setGameObjects(LinkedList<GameObject> gameObjects){
        this.gameObjects = gameObjects;
    }
}
