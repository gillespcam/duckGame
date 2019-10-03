package com.example.duckgame;

import android.graphics.PointF;

public interface CollisionCircle {

    PointF getPosition(); // Position of the circle in game units
    float getRadius(); // Radius of the circle in game units

    boolean reflectUponCollision(); // The player responds to collision
}
