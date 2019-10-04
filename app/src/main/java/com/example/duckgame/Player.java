package com.example.duckgame;

import android.graphics.PointF;
import android.util.Log;

import java.util.LinkedList;

public class Player extends ActiveGameObject {

    private final String TAG = "Player";

    private boolean launched = false; // Whether the player has launched yet or not

    private float mass = 1; // How much force affects the change in velocity of the player
    private float bounciness = 0.7F; // Fraction of speed the projectile conserves after a bounce collision
    private float friction = -0.9F; // Fraction of current velocity lost each tick
    private float launchSpeed = 3F; // Initial launchPlayer speed multiplier
    private float stopLimit = 0.2F; // Speed the player has to be going under for the level to be considered finished

    private PointF force = new PointF(0, 0); // Net force acting on projectile
    private PointF velocity = new PointF(0, 0); // Current velocity of projectile

    public Player(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }

    /** ActiveGameObject **/

    public Player clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Player(parent, sprite, position, rotation, scale);
    }

    public void tick(double deltaTime) {
        if(launched){
            /* After launching, if under stopLimit then
               halt collision and trajectory calculations */
            if(velocity.length() < stopLimit){
                parent.end();
                launched = false;
                return;
            }
            else{
                calculateTrajectory(deltaTime);
                boundaryCollision();
                objectCollision();
            }
        }
    }

    /** Public Methods **/

    public void addForce(PointF vector){
        force.x += vector.x;
        force.y += vector.y;
    }

    public void aim(PointF coords){
        rotation = (float) Math.toDegrees(Math.atan2((coords.y - position.y), (coords.x - position.x)));
    }

    public void launch(PointF coords){
        velocity.x = (coords.x - position.x) * launchSpeed;
        velocity.y = (coords.y - position.y) * launchSpeed;
        launched = true;
    }

    /** Helper Methods **/

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
        LinkedList<GameObject> gameObjects = parent.getObjects();
        for(GameObject obj : gameObjects){
            if (obj instanceof CollisionCircle) handleCircle((CollisionCircle)obj);
            if (obj instanceof CollisionRectangle) handleRectangle((CollisionRectangle)obj);
        }
    }

    private void handleCircle(CollisionCircle obj){
        PointF objPos = obj.getPosition();
        float objRad = obj.getRadius();
        float xDist = position.x - objPos.x;
        float yDist = position.y - objPos.y;
        float Dist = (float)Math.sqrt(xDist * xDist + yDist * yDist);
        float radSum = (scale / 2) + objRad;

        // If the player is too far away to collide, we do nothing
        if (Dist > radSum) return;

        Log.i(TAG + "/Collision", "Circle");

        // Does player reflect upon collision with this object?
        if (obj.onCollision()) {
            // Angle of velocity from center of player
            float velAngle = (float)Math.atan2(velocity.y, velocity.x);
            // Angle from ball to object: normal to plane of reflection
            float normAngle = (float)Math.atan2(yDist, xDist);
            // Calculate reflected angle
            float newAngle = 2*normAngle - velAngle;
            // Calculate reflected velocity
            float speed = (float)Math.sqrt(velocity.x*velocity.x + velocity.y*velocity.y);
            velocity.x = speed * bounciness * (float)Math.cos(newAngle);
            velocity.y = speed * bounciness * (float)Math.sin(newAngle);
        }
    }

    private void handleRectangle(CollisionRectangle obj){
        PointF objPos = obj.getPosition();
        float objWidth = obj.getWidth();
        float objHeight = obj.getHeight();
        float xdist = Math.abs(this.position.x - objPos.x);
        float ydist = Math.abs(this.position.y - objPos.y);

        // If the player is too far away to collide, we do nothing
        if(xdist  > (objWidth + scale) / 2 || ydist  > (objHeight + scale) / 2 ) return;

        Log.i(TAG + "/Collision", "Rectangle");

        // Does player reflect upon collision with this object?
        if(obj.onCollision()){
            // Gradient of the diagonal of the rectangle, used to determine which edge to snap to if the point is inside the rectangle
            float diagGrad = objHeight / objWidth;
            // Edge collisions, where the player is away from the corners
            // Top edge
            if(xdist <= objWidth / 2 && position.y > objPos.y + xdist * diagGrad) {
                position.y = objPos.y + (objHeight + scale )/ 2;
                velocity.y = Math.abs(velocity.y);
                velocity.x *= bounciness;
                velocity.y *= bounciness;
                return; }
            // Bottom edge
            if(xdist <= objWidth / 2 && position.y < objPos.y - xdist * diagGrad) {
                position.y = objPos.y - (objHeight + scale )/ 2;
                velocity.y = -Math.abs(velocity.y);
                velocity.x *= bounciness;
                velocity.y *= bounciness;
                return; }
            // Right edge
            if(ydist <= objHeight / 2 && position.x > objPos.x + ydist / diagGrad) {
                position.x = objPos.x + (objWidth + scale )/ 2;
                velocity.x = Math.abs(velocity.y);
                velocity.x *= bounciness;
                velocity.y *= bounciness;
                return; }
            // Left edge
            if(ydist <= objHeight / 2 && position.x < objPos.x - ydist / diagGrad) {
                position.x = objPos.x - (objWidth + scale )/ 2;
                velocity.x = -Math.abs(velocity.x);
                velocity.x *= bounciness;
                velocity.y *= bounciness;
                return; }

            // TODO: Corners
            //...
        }
    }

    /** Properties **/

    public PointF getVelocity() {
        return velocity;
    }
}
