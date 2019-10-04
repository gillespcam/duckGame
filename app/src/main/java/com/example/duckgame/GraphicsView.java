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

public class GraphicsView extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "GraphicsView";

    private final float margin = 20F;

    private Context context;
    private float screenWidth;
    private float screenHeight;
    private PointF offset = new PointF();
    float scale = 0F;

    private Paint spritePaint;
    private Paint grassPaint;
    private Paint shadePaint;
    private Paint waterPaint;

    private SparseArray<Bitmap> sprites;

    private GameThread game;
    private GameWorld world;
    private PointF worldSize;
    private boolean launchInProgress = false;

    GraphicsView (Context context, LevelBlueprint levelBlueprint) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);
        game = new GameThread(getHolder(), this, levelBlueprint);
        worldSize = world.getSize();
        setFocusable(true);

        // Initialise the paint options
        spritePaint = new Paint();
        spritePaint.setAntiAlias(true);
        spritePaint.setFilterBitmap(true);
        spritePaint.setDither(true);

        grassPaint = new Paint();
        grassPaint.setColor(ContextCompat.getColor(context, R.color.Grass));

        shadePaint = new Paint();
        shadePaint.setColor(ContextCompat.getColor(context, R.color.Shade));

        waterPaint = new Paint();
        waterPaint.setColor(ContextCompat.getColor(context, R.color.Water));

        // Load all the resources we'll need to be drawing
        sprites = new SparseArray<>();
        sprites.put(R.drawable.player,  BitmapFactory.decodeResource(getResources(),R.drawable.player));
        sprites.put(R.drawable.goal,  BitmapFactory.decodeResource(getResources(),R.drawable.goal));
        sprites.put(R.drawable.bonus,  BitmapFactory.decodeResource(getResources(),R.drawable.bonus));
        sprites.put(R.drawable.whirlpool,  BitmapFactory.decodeResource(getResources(),R.drawable.whirlpool));
        sprites.put(R.drawable.oil,  BitmapFactory.decodeResource(getResources(),R.drawable.oil));
        sprites.put(R.drawable.boat,  BitmapFactory.decodeResource(getResources(),R.drawable.boat));
        sprites.put(R.drawable.launch_pad,  BitmapFactory.decodeResource(getResources(),R.drawable.launch_pad));
    }

    @Override
    public void draw(Canvas canvas) {
        // Clear the canvas - comment out this line to activate the  W I N D O W S  X P  I M M E R S I V E  E X P E R I E N C E
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        super.draw(canvas);

        // Draw Grass and Pond
        canvas.drawRect(0, 0, scale * screenWidth, scale * screenHeight, grassPaint);
        canvas.drawRect(offset.x, offset.y, scale * worldSize.x + offset.x, scale * worldSize.y + offset.y, shadePaint);
        canvas.drawRect(offset.x + margin, offset.y + margin, scale * worldSize.x + offset.x, scale * worldSize.y + offset.y, waterPaint);

        // Draw GameObjects
        for (GameObject obj : world.getObjects()) {
            if (obj instanceof Wall) {
                canvas.drawRect(
                        offset.x + scale * ((Wall) obj).getCorners()[0].x,
                        offset.y + scale * ((Wall) obj).getCorners()[0].y,
                        offset.x + scale * ((Wall) obj).getCorners()[2].x,
                        offset.y + scale * ((Wall) obj).getCorners()[2].y,
                        grassPaint);
            } else {
                Bitmap bitmap = sprites.get(obj.getSprite());
                float spriteScale = obj.getScale() * scale / bitmap.getWidth();
                PointF middlePoint = new PointF(bitmap.getWidth() / 2F, bitmap.getHeight() / 2F);

                Matrix matrix = new Matrix();
                matrix.setRotate(obj.getRotation(), middlePoint.x, middlePoint.y);
                matrix.postScale(spriteScale, spriteScale);
                matrix.postTranslate(
                        obj.getPosition().x * scale + offset.x - middlePoint.x * spriteScale,
                        obj.getPosition().y * scale + offset.y - middlePoint.y * spriteScale);
                canvas.drawBitmap(bitmap, matrix, spritePaint);
            }
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
        screenWidth = width - 2 * margin;
        screenHeight = height - 2 * margin;

        float xscale = screenWidth / worldSize.x;
        float yscale = screenHeight / worldSize.y;

        if(xscale < yscale){
            scale = xscale;
            offset.y = (screenHeight - worldSize.y * xscale) / 2 + margin;
            offset.x = margin;
        } else {
            scale = yscale;
            offset.x = (screenWidth - worldSize.x * yscale) / 2 + margin;
            offset.y = margin;
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
            // If we're not paused or launched, we can start the launchPlayer procedure
            if(!game.getPaused() && !game.getLaunched()){
                // no launchPlayer motion currently in progress
                if(!launchInProgress) launchInProgress = true;
                int pointer = event.findPointerIndex(0);
                PointF aimPoint = new PointF(event.getX(pointer), event.getY(pointer));
                // Aim with aimPoint in game units
                game.aimPlayer(new PointF((aimPoint.x - offset.x) / scale, (aimPoint.y - offset.y) / scale));
                return true;
            }
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.i(TAG, "MOVE" + event.getPointerCount());
            // If we currently have a launchPlayer motion in progress
            if(!game.getPaused() && launchInProgress){
                int pointer = event.findPointerIndex(0);
                PointF aimPoint = new PointF(event.getX(pointer), event.getY(pointer));
                // Aim with aimPoint in game units
                game.aimPlayer(new PointF((aimPoint.x - offset.x) / scale, (aimPoint.y - offset.y) / scale));
                return true;
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(TAG, "UP" + event.getPointerCount());
            // If we currently have a launchPlayer motion in progress
            if(launchInProgress){
                launchInProgress = false;
                if(!game.getPaused()){
                    // hopefully we don't need this, saving it for later though
                    // int pointer = event.findPointerIndex(0);
                    PointF launchPoint = new PointF(event.getX(), event.getY());
                    // Launch with launchPoint in game units
                    game.launchPlayer(new PointF((event.getX() - offset.x) / scale, (event.getY() - offset.y) / scale));
                }
                return true;
            }
        } else if(event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            Log.i(TAG, "POINTERUP" + event.getPointerCount());
            // If we currently have a launchPlayer motion in progress
            if(launchInProgress && (event.getActionIndex()) == 0){
                launchInProgress = false;
                if(!game.getPaused()){
                    // hopefully we don't need this, saving it for later though
                    //int pointer = event.findPointerIndex(0);
                    PointF launchPoint = new PointF(event.getX(), event.getY());
                    // Launch with launchPoint in game units
                    game.launchPlayer(new PointF((event.getX() - offset.x) / scale, (event.getY() - offset.y) / scale));
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /** Properties **/

    public GameThread getGameThread(){
        return game;
    }
    public void setGameWorld(GameWorld world){
        this.world = world;
    }
}
