package com.example.duckgame;

import android.graphics.PointF;

/** Oil - Accelerates player in direction of velocity when colliding with it **/
public class Oil extends GameObject implements CollisionCircle{

    private final String TAG = "Oil";

    private float slipperyness = 4F;

    public Oil(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }

    /** GameObject **/

    public Oil clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Oil(parent, sprite, position, rotation, scale);
    }

    /** CollisionCircle **/

    @Override
    public float getRadius(){ return scale / 2; }

    @Override
    public boolean onCollision(){
        Player player = parent.getPlayer();

        PointF velocity = player.getVelocity();
        PointF force = new PointF(
                velocity.x * slipperyness,
                velocity.y * slipperyness);

        player.addForce(force);

        // Own actions taken
        return false;
    }
}
