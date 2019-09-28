package com.example.duckgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.LinkedList;

public class PlayerProjectile extends GameObject {

    //for physics
    private float mass = 1;
    private float bounciness = 0.6F;

    private PointF velocity = new PointF(0, 0);
    private PointF force = new PointF(0, 0);

    @Override
    public int getSprite() {
        return R.drawable.player;
    }

    @Override
    public String getShape() {
        return "CIRCLE";
    }

    public void addForce(PointF vector) {
        force.x += vector.x;
        force.y += vector.y;
    }

    public void calculateTrajectory(float deltat){
        PointF pos = this.getPosition();
        velocity.x += (force.x / mass) * deltat;
        velocity.y += (force.y / mass) * deltat;
        force.x = 0;
        force.y = 0;
        rotation = (float)Math.toDegrees(Math.atan2(velocity.y, velocity.x));
        pos.x += velocity.x * deltat;
        pos.y += velocity.y * deltat;
    }

    PlayerProjectile(GameWorld p) {
        super(p);
    }

    PlayerProjectile(GameWorld p, PointF vel) {
        super(p);
        velocity = vel;
    }

    @Override
    public void doGameTick(float deltat) {
        LinkedList<GameObject> objs = this.getParent().getGameObjects();

        calculateTrajectory(deltat);;

        //deal with collision against walls
        if(position.x - scale / 2 <= 0){
            position.x = scale / 2;
            velocity.x += 2 * Math.abs(velocity.x);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
        if(position.x + scale / 2 >= this.getParent().getSize().x){
            position.x = this.getParent().getSize().x - scale / 2;
            velocity.x -= 2 * Math.abs(velocity.x);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
        if(position.y - scale / 2 <= 0){
            position.y = scale / 2;
            velocity.y += 2 * Math.abs(velocity.y);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
        if(position.y + scale / 2 >= this.getParent().getSize().y){
            position.y = this.getParent().getSize().y - scale / 2;
            velocity.y -= 2 * Math.abs(velocity.y);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }


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
                        float sumrads = (this.scale + objscale) / 2;
                        if(dsq < sumrads * sumrads){
                            float velangle = (float)Math.atan2(velocity.y, velocity.x);

                            // angle from ball to object - normal to plane of reflection
                            float normangle = (float)Math.atan2(ydist, xdist);
                            // calculate reflected angle
                            float newAngle = 2*normangle - velangle;

                            float speed = (float)Math.sqrt(velocity.x*velocity.x + velocity.y*velocity.y);
                            velocity.x += speed * bounciness * (float)Math.cos(newAngle);
                            velocity.y += speed * bounciness * (float)Math.sin(newAngle);
                        }
                        break;
                    default:
                        // simple square, no rotation consideration
                        // do nothing for now though

                }

            }
        }

    }
}
