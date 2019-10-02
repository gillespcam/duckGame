package com.example.duckgame;

import android.graphics.PointF;
import android.util.Log;

import java.util.LinkedList;

public class Player extends ActiveGameObject {

    private boolean launched = false;

    private float mass = 1; // How much forces affect the change in velocity of the projectile
    private float bounciness = 0.7F; // Fraction of speed the projectile conserves after a bounce collision
    private float friction = -0.9F;
    private float launchSpeed = 3F;
    private float stopLimit = 0.2F;

    private PointF force = new PointF(0, 0); // Net force acting on projectile
    private PointF velocity = new PointF(0, 0); // Current velocity of projectile

    public boolean isCollidable() { return false; }
    public void onCollision(Player player) {}
    public String getShape() { return "CIRCLE"; }

    public Player(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        super(parent, sprite, position, rotation, scale);
    }

    public Player clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Player(parent, sprite, position, rotation, scale);
    }

    public void tick(double deltaTime) {
        if(launched){
            if(velocity.x * velocity.x + velocity.y * velocity.y < stopLimit * stopLimit) { // if we're going below the lower speed limit
                parent.playerStopped();
                return;
            }
            else {
                calculateTrajectory(deltaTime);
                boundaryCollision();
                objectCollision();
            }
        }
    }

    public void aim(PointF coords) {
        rotation = (float) Math.toDegrees(Math.atan2((coords.y - position.y), (coords.x - position.x)));
    }

    public void launch(PointF coords){
        velocity.x = (coords.x - position.x) * launchSpeed;
        velocity.y = (coords.y - position.y) * launchSpeed;
        launched = true;
    }

    public void addForce(PointF vector) {
        force.x += vector.x;
        force.y += vector.y;
    }

    private void calculateTrajectory(double deltaTime){
        // Add friction force
        addForce(new PointF(velocity.x * friction, velocity.y * friction));

        velocity.x += (force.x / mass) * deltaTime;
        velocity.y += (force.y / mass) * deltaTime;
        force.x = 0;
        force.y = 0;
        rotation = (float)Math.toDegrees(Math.atan2(velocity.y, velocity.x));
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    private void boundaryCollision(){
        //deal with collision against walls
        if(position.x - scale / 2 <= 0){
            position.x = scale / 2;
            velocity.x = Math.abs(velocity.x);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
        if(position.x + scale / 2 >= parent.getSize().x){
            position.x = this.getParent().getSize().x - scale / 2;
            velocity.x = -Math.abs(velocity.x);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
        if(position.y - scale / 2 <= 0){
            position.y = scale / 2;
            velocity.y = Math.abs(velocity.y);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
        if(position.y + scale / 2 >= parent.getSize().y){
            position.y = this.getParent().getSize().y - scale / 2;
            velocity.y = -Math.abs(velocity.y);
            velocity.x *= bounciness;
            velocity.y *= bounciness;
        }
    }

    private void objectCollision(){
        LinkedList<GameObject> objects = parent.getObjects();
        for(GameObject obj : objects) {
            // If a collision with this object would result in anything
            if(obj.isCollidable()){
                PointF objpos = obj.getPosition();
                float objrot = obj.getRotation();
                float objscale = obj.getScale();
                String shape = obj.getShape();

                // Check if colliding
                switch (shape){
                    case "CIRCLE":
                        float xdist = this.position.x - objpos.x;
                        float ydist = this.position.y - objpos.y;
                        // Use the square of the distances, so we don't have to square root
                        float dsq = xdist * xdist + ydist * ydist;
                        // The sum of the radii of the circles: the minimum distance the circles can be apart without touching
                        float sumrads = (this.scale + objscale) / 2;

                        // If colliding, call result of collision
                        if(dsq < sumrads * sumrads) obj.onCollision(this);
                        break;
                    default:
                        // Simple square, no rotation consideration
                        //...
                }
            }
        }
    }

    /** Properties **/

    public float getBounciness() {
        return bounciness;
    }

    public PointF getVelocity() {
        return velocity;
    }
    public void setVelocity(PointF velocity) {
        this.velocity = velocity;
    }
}
