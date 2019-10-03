package com.example.duckgame;

import android.graphics.PointF;
import android.util.Log;

/** Boat - Travels in a straight line in the direction it's facing then back, reflecting the player away on collision **/
public class Boat extends ActiveGameObject implements CollisionRectangle {

    private final String TAG = "Boat";

    private final float SPAN = 8F; // Distance the boat travels one-way
    private final int ROUND_TRIP = 360; // Amount of ticks it takes to complete a round trip
    private final int COLLISION_COOLDOWN = 60; // Amount of ticks before player can collide with this object again

    private int tock = 0;

    private boolean reverse = false; // If the boat is travel towards or away from home
    private PointF homePosition; // Initial position of this object
    private double homeBearing; // Initial rotation in radians
    private int cooldown = 0; // Amount of ticks left before collision is enabled again

    public Boat(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        super(parent, sprite, position, rotation, scale);
        homePosition = new PointF(position.x, position.y);
        homeBearing = Math.toRadians(rotation);
    }

    /** ActiveGameObject **/

    public Boat clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Boat(parent, sprite, position, rotation, scale);
    }

    public void tick(double deltaTime) {
        setPosition(new PointF(
                homePosition.x + (float)(tock * (SPAN * Math.cos(homeBearing)) / (ROUND_TRIP / 2F)),
                homePosition.y + (float)(tock * (SPAN * Math.sin(homeBearing)) / (ROUND_TRIP / 2F))));

        if (reverse) tock--; else tock++;

        if (tock == ROUND_TRIP / 2 || tock == 0) {
            rotation += 180;
            reverse = !reverse;
        }

        if (cooldown > 0) cooldown--;
    }

    /** CollisionRectangle **/

    @Override
    public float getWidth() { return scale; }
    @Override
    public float getHeight() { return scale / 2; }

    @Override
    public boolean onCollision(){

        // Actions taken by player
        if (cooldown == 0) {
            cooldown = COLLISION_COOLDOWN;
            return true;
        }
        else return false;
    }
}
