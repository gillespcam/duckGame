package com.example.duckgame;

import android.graphics.PointF;

public class PlayerProjectile implements GameObject {
    //for drawing
    private PointF position;
    private float rotation;
    private float scale;

    //for physics
    private float mass;

    public boolean isDrawable(){return true;}

    @Override
    public PointF getPosition() {
        return position;
    }
    public void setPosition(PointF pos){
        position = pos;
    }

    @Override
    public float getRotation() {
        return rotation;
    }
    public void setRotation(float rot) {
        rotation = rot;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public String getShape() {
        return "CIRCLE";
    }
}
