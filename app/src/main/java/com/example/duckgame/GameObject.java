package com.example.duckgame;

import android.graphics.PointF;

abstract class GameObject {

    protected GameWorld parent;
    protected int sprite;
    protected PointF position;
    protected float rotation;
    protected float scale;

    public GameObject(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        this.parent = parent;
        this.sprite = sprite;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public abstract GameObject clone(GameWorld parent, int sprite, PointF position, float rotation, float scale);

    /** Properties **/

    protected GameWorld getParent(){
        return parent;
    }

    public int getSprite(){
        return sprite;
    }
    public void setSprite(int sprite){
        this.sprite = sprite;
    }

    public PointF getPosition() {
        return position;
    }
    public void setPosition(PointF position){
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
}
