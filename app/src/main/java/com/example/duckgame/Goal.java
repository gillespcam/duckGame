package com.example.duckgame;

import android.graphics.PointF;

public class Goal extends GameObject {

    public boolean isColliding(GameObject object) { return false; }
    public String getShape() { return "CIRCLE"; }

    public GameObject clone(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        return new Goal(parent, sprite, position, rotation, scale);
    }


    public Goal(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }


}
