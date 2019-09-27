package com.example.duckgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    public class GraphicsView extends View {

        Paint paint;
        private LinkedList<GameObject> drawables;

        public GraphicsView (Context context) {
            super(context);
            //initialise the paint options
            paint = new Paint();
            paint.setColor(getColor(R.color.colorPrimary));
            //make sure we know the screen size

        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            for (GameObject gameObject : drawables){
                //draw objects with updated positions, dimensions etc.
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            screenWidth = w;
            screenHeight = h;
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
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraintLayoutGame);
        GraphicsView graphicsView = new GraphicsView(this);
        constraintLayout.addView(graphicsView);
    }
}