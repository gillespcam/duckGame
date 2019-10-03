package com.example.duckgame;

import android.graphics.PointF;

public interface CollisionCircle {

    PointF getPosition(); // Position of the circle in game units
    float getRadius(); // Radius of the circle in game units

    /* Returns true if collision results in player being reflected.
       Returns false if collision result is handled by object */
    boolean onCollision();
}
