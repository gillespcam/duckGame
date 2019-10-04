package com.example.duckgame;

import android.graphics.PointF;

/** Wall - Immobile object that reflects the player away on collision **/
public class Launchpad extends GameObject implements CollisionRectangle {

    private final String TAG = "Launchpad";

    private float width = 0;
    private float height = 0;
    private PointF[] corners = new PointF[4];

    // Will result in a square with side lengths of size scale
    public Launchpad(GameWorld parent, int sprite, PointF centre, float rotation, float scale) {
        super(parent, sprite, centre, rotation, scale);
        width = scale;
        height = scale;
        corners[0] = new PointF(centre.x - width / 2, centre.y - height / 2);
        corners[1] = new PointF(centre.x + width / 2, centre.y - height / 2);
        corners[2] = new PointF(centre.x + width / 2, centre.y + height / 2);
        corners[3] = new PointF(centre.x - width / 2, centre.y + height / 2);
    }


    /** GameObject **/

    public Launchpad clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Launchpad(parent, sprite, position, rotation, scale);
    }

    /** CollisionRectangle **/

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean onCollision(){
        Player playerObj = parent.getPlayer();
        playerObj.setPosition(new PointF(position.x, position.y));
        playerObj.setLaunched(false);
        parent.unLaunch();
        parent.removeObject(this);
        return false;
    }

    /** Properties **/

    public PointF[] getCorners(){
        return corners;
    }
}
