package com.example.duckgame;

import android.graphics.PointF;

public class Bonus extends GameObject implements CollisionCircle {

    public Bonus (GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        super(parent, sprite, position, rotation, scale);
    }

    /** GameObject **/

    public Bonus clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Bonus(parent, sprite, position, rotation, scale);
    }

    /** CollisionCircle **/

    public float getRadius(){ return scale / 2; }

    public boolean onCollision(){
        return false;
    }
}
