package com.example.duckgame;

import android.graphics.PointF;

interface GameObject {
    PointF getPosition();
    void setPosition(PointF pos);

    float getRotation();
    void setRotation(float rot);

    float getScale();

    boolean isDrawable();

    String getShape();
}
