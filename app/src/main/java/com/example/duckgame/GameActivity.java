package com.example.duckgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    public class GraphicsView extends View {

        Paint paint;
        private LinkedList<GameObject> drawables = new LinkedList<>();
        private GameWorld world;

        public GraphicsView (Context context) {
            super(context);
            //initialise the paint options
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);

            //paint.setColor(getColor(R.color.colorPrimary));
            //make sure we know the screen size

            // load all the resources we'll need to be drawing
            sprites = new SparseArray<>();
            sprites.put(R.drawable.goal,  BitmapFactory.decodeResource(getResources(),R.drawable.goal));
            sprites.put(R.drawable.player,  BitmapFactory.decodeResource(getResources(),R.drawable.player));

            world = new GameWorld();


        }


        // Initialize a new Matrix instance
        Matrix matrix = new Matrix();
        Bitmap bitmap;
        // improves performance by loading all the resources outside the draw loop
        SparseArray<Bitmap> sprites;
        // how big is one game unit in pixels
        float scale;

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            Log.i("TAG", Float.toString(scale * world.getSize().x));
            canvas.drawRect(5,5, scale * world.getSize().x, scale * world.getSize().y, paint);

            for (GameObject gameObject : drawables){
                //draw objects with updated positions, dimensions etc.
                bitmap = sprites.get(gameObject.getSprite());
                matrix.setRotate(gameObject.getRotation(),bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                matrix.postTranslate(gameObject.getPosition().x - bitmap.getWidth() / 2, gameObject.getPosition().y - bitmap.getHeight() / 2);

                canvas.drawBitmap(bitmap, matrix, paint);
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            screenWidth = w - 10;
            screenHeight = h - 10;

            float xscale = screenWidth / world.getSize().x;
            float yscale = screenHeight / world.getSize().y;

            scale = (xscale < yscale) ? xscale : yscale;
        }

        public void SetDrawables(GameObject[] gameObjects, PointF levelsize){
            //update list of drawables
            //...
            //calculate multiplier for dimensions and position for gameObjects based on screenWidth & screenHeight
            //...
            invalidate();
        }
    }

    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //make fullscreen
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //set up the views
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutGame);
        GraphicsView graphicsView = new GraphicsView(this);
        constraintLayout.addView(graphicsView);

    }
}