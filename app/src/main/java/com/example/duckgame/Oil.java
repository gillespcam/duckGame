package com.example.duckgame;

import android.graphics.PointF;
import android.util.Log;

public class Oil extends GameObject {

    private float slipperyness = 2F;

    public boolean isCollidable() { return true; }
    public void onCollision(Player player) {
        // Accelerate player
        PointF velocity = player.getVelocity();
        PointF force = new PointF(velocity.x * slipperyness, velocity.y * slipperyness);
        player.addForce(force);
    }
    public String getShape() { return "CIRCLE"; }

    public Oil(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }

    public Oil clone(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        return new Oil(parent, sprite, position, rotation, scale);
    }
}
