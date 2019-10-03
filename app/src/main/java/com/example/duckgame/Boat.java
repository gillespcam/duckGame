package com.example.duckgame;

import android.graphics.PointF;

/** Boat - Travels on a fixed path, reflecting the player away on collision **/
public class Boat extends ActiveGameObject implements CollisionRectangle {

    public Boat(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        super(parent, sprite, position, rotation, scale);
    }

    /** ActiveGameObject **/

    public Boat clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Boat(parent, sprite, position, rotation, scale);
    }

    public void tick(double deltaTime) {
        // Calculate movement
        //...
    }

    /** CollisionRectangle **/

    @Override
    public float getWidth() { return scale; }
    @Override
    public float getHeight() { return scale / 2; }

    @Override
    public boolean onCollision(){
        // Actions taken by player
        return true;
    }
}
