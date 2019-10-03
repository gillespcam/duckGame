package com.example.duckgame;

import android.graphics.PointF;

public class Wall extends GameObject implements CollisionRectangle {
    private float width = 0;
    private float height = 0;
    private PointF[] corners = new PointF[4]; // corners of the rectangle, clockwise from top left

    public Wall(GameWorld parent, int sprite, PointF centre, float rotation, float scale) { // will result in a square with side lengths of size scale
        super(parent, sprite, centre, rotation, scale);
        width = scale;
        height = scale;
        corners[0] = new PointF(centre.x - width / 2, centre.y - height / 2);
        corners[1] = new PointF(centre.x + width / 2, centre.y - height / 2);
        corners[2] = new PointF(centre.x + width / 2, centre.y + height / 2);
        corners[3] = new PointF(centre.x - width / 2, centre.y + height / 2);
    }

    public Wall(GameWorld parent, int sprite, PointF centre, float rotation, float width, float height) { // the scale will be the width of the rectangle
        super(parent, sprite, centre, rotation, width);
        this.width = width;
        this.height = height;
        corners[0] = new PointF(centre.x - width / 2, centre.y - height / 2);
        corners[1] = new PointF(centre.x + width / 2, centre.y - height / 2);
        corners[2] = new PointF(centre.x + width / 2, centre.y + height / 2);
        corners[3] = new PointF(centre.x - width / 2, centre.y + height / 2);
    }

    public Wall clone(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        return new Wall(parent, sprite, position, rotation, scale);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public PointF[] getCorners(){
        return corners;
    }
}
