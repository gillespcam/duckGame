package com.example.duckgame;

import android.graphics.PointF;

/** Wall - Immobile object that reflects the player away on collision **/
public class Wall extends GameObject implements CollisionRectangle {

    private final String TAG = "Wall";

    private float width = 0;
    private float height = 0;
    private PointF[] corners = new PointF[4];

    // Will result in a square with side lengths of size scale
    public Wall(GameWorld parent, int sprite, PointF centre, float rotation, float scale) {
        super(parent, sprite, centre, rotation, scale);
        width = scale;
        height = scale;
        corners[0] = new PointF(centre.x - width / 2, centre.y - height / 2);
        corners[1] = new PointF(centre.x + width / 2, centre.y - height / 2);
        corners[2] = new PointF(centre.x + width / 2, centre.y + height / 2);
        corners[3] = new PointF(centre.x - width / 2, centre.y + height / 2);
    }

    // The scale will be the width of the rectangle
    public Wall(GameWorld parent, int sprite, PointF centre, float rotation, float width, float height) {
        super(parent, sprite, centre, rotation, width);
        this.width = width;
        this.height = height;
        corners[0] = new PointF(centre.x - width / 2, centre.y - height / 2);
        corners[1] = new PointF(centre.x + width / 2, centre.y - height / 2);
        corners[2] = new PointF(centre.x + width / 2, centre.y + height / 2);
        corners[3] = new PointF(centre.x - width / 2, centre.y + height / 2);
    }

    /** GameObject **/

    public Wall clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Wall(parent, sprite, position, rotation, scale);
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
    public boolean reflectUponCollision(){
        // Actions taken by player
        return true;
    }

    /** Properties **/

    public PointF[] getCorners(){
        return corners;
    }
}
