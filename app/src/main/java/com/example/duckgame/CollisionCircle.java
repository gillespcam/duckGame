package com.example.duckgame;

import android.graphics.PointF;

public interface CollisionCircle {

    float getRadius(); // Radius of the circle in game units
    PointF getCentre(); // Centre of the circle from top left of the sprite
    boolean reflectUponCollision(); // The player responds to collision
}
