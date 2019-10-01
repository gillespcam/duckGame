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

import androidx.core.content.ContextCompat;

import java.util.LinkedList;

public class GraphicsView extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "GraphicsView";

    private float screenWidth;
    private float screenHeight;
    float scale;

    private Paint spritePaint;
    private Paint grassPaint;
    private Paint waterPaint;

    private Matrix matrix = new Matrix();
    private Bitmap bitmap;
    private SparseArray<Bitmap> sprites;

    private GameThread game;
    private LinkedList<GameObject> gameObjects;
    private PointF gameSize;

    private int pixmargin = 20;
    private PointF offset = new PointF(0,0);

    private boolean launchInProgress = false;

    GraphicsView (Context context, LevelBlueprint levelBlueprint) {
        super(context);
        gameSize = levelBlueprint.getSize();

        getHolder().addCallback(this);
        game = new GameThread(getHolder(), this, levelBlueprint);
        setFocusable(true);

        // Initialise the paint options
        spritePaint = new Paint();
        spritePaint.setAntiAlias(true);
        spritePaint.setFilterBitmap(true);
        spritePaint.setDither(true);
        grassPaint = new Paint();
        grassPaint.setFilterBitmap(true);
        grassPaint.setDither(true);
        grassPaint.setColor(ContextCompat.getColor(context, R.color.colorGrass));
        waterPaint = new Paint();
        waterPaint.setFilterBitmap(true);
        waterPaint.setDither(true);
        waterPaint.setColor(ContextCompat.getColor(context, R.color.colorWater));

        // Load all the resources we'll need to be drawing
        sprites = new SparseArray<>();
        sprites.put(R.drawable.goal,  BitmapFactory.decodeResource(getResources(),R.drawable.goal));
        sprites.put(R.drawable.player,  BitmapFactory.decodeResource(getResources(),R.drawable.player));
    }

    @Override
    public void draw(Canvas canvas){
        // Clear the canvas - comment out this line to activate the  W I N D O W S  X P  I M M E R S I V E  E X P E R I E N C E
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        super.draw(canvas);

        // Draw basic pond with grass margin
        canvas.drawRect(0, 0, scale * screenWidth, scale* screenHeight, grassPaint);
        canvas.drawRect(offset.x, offset.y, scale * gameSize.x + offset.x, scale * gameSize.y + offset.y, waterPaint);

        // Draw GameWorld objects with updated positions, dimensions etc.
        for (GameObject gameObject : gameObjects){
            bitmap = sprites.get(gameObject.getSprite());
            float spriteScale = gameObject.getScale() * scale / bitmap.getWidth();
            PointF middleCoord = new PointF(bitmap.getWidth() / 2F, bitmap.getHeight() / 2F);

            matrix.setRotate(gameObject.getRotation(), middleCoord.x, middleCoord.y );
            matrix.postScale(spriteScale, spriteScale);
            matrix.postTranslate(gameObject.getPosition().x * scale + offset.x - middleCoord.x * spriteScale, gameObject.getPosition().y * scale + offset.y - middleCoord.y * spriteScale);

            canvas.drawBitmap(bitmap, matrix, spritePaint);
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
        screenWidth = width - 2 * pixmargin;
        screenHeight = height - 2 * pixmargin;

        float xscale = screenWidth / gameSize.x;
        float yscale = screenHeight / gameSize.y;

        if(xscale < yscale){
            scale = xscale;
            offset.y = (screenHeight - gameSize.y * xscale) / 2 + pixmargin;
            offset.x = pixmargin;
        } else {
            scale = yscale;
            offset.x = (screenWidth - gameSize.x * yscale) / 2 + pixmargin;
            offset.y = pixmargin;
        }
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
            Log.i(TAG, "DOWN");
            // if we're not paused or launched, we can start the launch procedure
            if(!game.getPaused() && !game.getLaunched()){
                // no launch motion currently in progress
                if(!launchInProgress) {
                    launchInProgress = true;
                    return true;
                }
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.i(TAG, "MOVE" + event.getPointerCount());
            // if we currently have a launch motion in progress
            if(launchInProgress){
                int pointer = event.findPointerIndex(0);
                PointF aimGamePoint = new PointF(event.getX(pointer) / scale, event.getY(pointer) / scale);
                game.aimPlayer(aimGamePoint);
                return true;
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(TAG, "UP" + event.getPointerCount());
            // if we currently have a launch motion in progress
            if(launchInProgress){
                launchInProgress = false;
                // hopefully we don't need this, saving it for later though
                // int pointer = event.findPointerIndex(0);
                PointF launchGamePoint = new PointF(event.getX() / scale, event.getY() / scale);
                game.launchPlayer(launchGamePoint);
                return true;
            }
        } else if(event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            Log.i(TAG, "POINTERUP" + event.getPointerCount());
            // if we currently have a launch motion in progress
            if(launchInProgress && (event.getActionIndex()) == 0){
                launchInProgress = false;
                // hopefully we don't need this, saving it for later though
                //int pointer = event.findPointerIndex(0);
                PointF launchGamePoint = new PointF(event.getX() / scale, event.getY() / scale);
                game.launchPlayer(launchGamePoint);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /** Properties **/

    public GameThread getGameThread(){
        return game;
    }
    public void setGameObjects(LinkedList<GameObject> gameObjects){
        this.gameObjects = gameObjects;
    }
}
