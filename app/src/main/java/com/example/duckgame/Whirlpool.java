package com.example.duckgame;

import android.graphics.PointF;

/** Whirlpool - Applies constant force from the player to itself while they are colliding **/
public class Whirlpool extends ActiveGameObject implements CollisionCircle{

    private final String TAG = "Whirlpool";

    private final int revolutionTicks = 60; // Amount of ticks it takes to complete a full rotation
    private final float degreesPerTick = 360 / revolutionTicks; // Amount of degrees rotated every tick
    private final float forceMultiplier = 10F; // Base modifier for tinkering with how much pull the whirlpool applies
    private final float scaleMutliplier = 0.25F; // How much scale affects the force of the pull

    private int tock = 0;

    public Whirlpool(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }

    /** ActiveGameObject **/

    public Whirlpool clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Whirlpool(parent, sprite, position, rotation, scale);
    }

    public void tick(double deltaTime) {
        if (tock == 60) tock = 0;
        setRotation(tock * degreesPerTick);
        tock++;
    }

    /** CollisionCircle **/

    @Override
    public float getRadius(){ return scale / 2; }

    @Override
    public boolean onCollision(){
        Player player = parent.getPlayer();

        PointF force = new PointF(
                position.x - player.getPosition().x,
                position.y - player.getPosition().y);
        float length = force.length();
        force.x = (force.x / length) * forceMultiplier * (scaleMutliplier * scale);
        force.y = (force.y / length) * forceMultiplier * (scaleMutliplier * scale);

        player.addForce(force);

        // Own actions taken
        return false;
    }
}
