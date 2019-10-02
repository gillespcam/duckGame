package com.example.duckgame;

import android.graphics.PointF;

public class Goal extends GameObject {

    public boolean isCollidable() { return false; }
    public void onCollision(Player player) {}
    public String getShape() { return "CIRCLE"; }

    public Goal(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }

    public Goal clone(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        return new Goal(parent, sprite, position, rotation, scale);
    }
}
