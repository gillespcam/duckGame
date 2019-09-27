package com.example.duckgame;

import android.graphics.PointF;

import java.util.LinkedList;

public class PlayerProjectile extends GameObject {

    //for physics
    private float mass = 1;
    private float bounciness = 0.8F;

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

    PlayerProjectile(GameWorld p) {
        super(p);
        velocity = new PointF(0,0);
        force = new PointF(0, 0);
    }

    PlayerProjectile(GameWorld p, PointF vel) {
        super(p);
        velocity = vel;
        force = new PointF(0, 0);
    }

    @Override
    public void doGameTick() {
        boolean collision = false;
        PointF closestPoint = null;
        LinkedList<GameObject> objs = this.getParent().getGameObjects();
        for(GameObject obj : objs){
            // if the projectile bounces off the object, handle that. Otherwise ignore this object
            if(obj.projectileCollision()){
                PointF objpos = obj.getPosition();
                float objrot = obj.getRotation();
                float objscale = obj.getScale();
                String shape = obj.getShape();
                switch (shape){
                    case "CIRCLE":
                        float xdist = this.position.x - objpos.x;
                        float ydist = this.position.y - objpos.y;
                        //use the square of the distances, so we don't have to square root
                        float dsq = xdist * xdist + ydist * ydist;
                        // the sum of the radii of the circles: the minimum distance the circles can be apart without touching
                        float sumrads = this.scale + objscale;
                        if(dsq < sumrads * sumrads){
                            collision = true;
                            float velangle = (float)Math.atan2(velocity.y, velocity.x);

                            // angle from ball to object - normal to plane of reflection
                            float normangle = (float)Math.atan2(ydist, xdist);
                            // calculate reflected angle
                            float newAngle = 2*normangle - velangle;

                            float speed = (float)Math.sqrt(velocity.x*velocity.x + velocity.y*velocity.y);
                            velocity.x = speed * bounciness * (float)Math.cos(newAngle);
                            velocity.y = speed * bounciness * (float)Math.sin(newAngle);
                        }
                        break;
                    default:
                        // simple square, no rotation consideration


                }

            }
        }

    }
}
