package com.example.duckgame;

import android.graphics.PointF;

public class Boat extends ActiveGameObject {

    public boolean isCollidable() { return true; }
    public void onCollision(Player player) {
        // Bounce the player away
        /*PointF position = player.getPosition();
        float bounciness = player.getBounciness();
        PointF velocity = player.getVelocity();

        float xdist = position.x - this.position.x;
        float ydist = position.y - this.position.y;

        float velAngle = (float)Math.atan2(velocity.y, velocity.x);

        // Angle from ball to object - normal to plane of reflection
        float normAngle = (float)Math.atan2(ydist, xdist);
        // Calculate reflected angle
        float newAngle = 2*normAngle - velAngle;

        float speed = (float)Math.sqrt(velocity.x*velocity.x + velocity.y*velocity.y);
        velocity.x = speed * bounciness * (float)Math.cos(newAngle);
        velocity.y = speed * bounciness * (float)Math.sin(newAngle);

        player.setVelocity(velocity);*/
    }
    public String getShape() { return "CIRCLE"; }

    public Boat(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        super(parent, sprite, position, rotation, scale);
    }

    public Boat clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Boat(parent, sprite, position, rotation, scale);
    }

    public void tick(double deltaTime) {
        // Calculate movement
        //...
    }
}
