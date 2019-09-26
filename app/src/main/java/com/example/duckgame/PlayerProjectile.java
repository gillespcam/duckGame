package com.example.duckgame;

import android.graphics.PointF;

public class PlayerProjectile extends GameObject {

    //for physics
    private float mass;

    private PointF velocity;
    private PointF force;

    @Override
    public String getShape() {
        return "CIRCLE";
    }

    public void addForce(PointF vector) {
        force.x += vector.x;
        force.y += vector.y;
    }

    public void calculateTrajectory(){
        PointF pos = this.getPosition();
        velocity.x += (force.x / mass);
        velocity.y += (force.y / mass);
        pos.x += velocity.x;
        pos.y += velocity.y;
    }

    PlayerProjectile() {
        velocity = new PointF(0,0);
        force = new PointF(0, 0);
    }
}
