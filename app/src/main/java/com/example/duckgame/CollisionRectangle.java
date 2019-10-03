package com.example.duckgame;

import android.graphics.PointF;

public interface CollisionRectangle {

    PointF getPosition(); // Position of the rectangle
    float getRotation(); // Rotation of the rectangle
    float getWidth(); // Width of the rectangle
    float getHeight(); // Height of the rectangle
    boolean reflectUponCollision(); // The player responds to collision
}
