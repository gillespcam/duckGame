package com.example.duckgame;

import android.graphics.PointF;

abstract class GameObject {
    //for drawing
    private PointF position;
    private float rotation;
    private float scale;


    public boolean isDrawable(){return true;}

    public PointF getPosition() {
        return position;
    }
    public void setPosition(PointF pos){
        position = pos;
    }

    public float getRotation() {
        return rotation;
    }
    public void setRotation(float rot) {
        rotation = rot;
    }

    public float getScale() {
        return scale;
    }

    public String getShape() {
        return "CIRCLE";
    }

}
