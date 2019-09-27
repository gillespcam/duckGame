package com.example.duckgame;

import android.graphics.Bitmap;
import android.graphics.PointF;

abstract class GameObject {
    //for drawing
    protected PointF position;
    protected float rotation;
    protected float scale;

    private GameWorld parent;

    protected GameWorld getParent(){return parent;}

    public boolean isDrawable(){return false;}

    public abstract int getSprite();

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

    public boolean projectileCollision() {return false;}

    public void doGameTick(){
        //by default, the object does nothing
    }

    public GameObject(GameWorld p) {
        parent = p;
    }

}
