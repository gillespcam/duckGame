package com.example.duckgame;

import android.graphics.PointF;

abstract class ActiveGameObject extends GameObject {

    public ActiveGameObject(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        super(parent, sprite, position, rotation, scale);
    }

    abstract void tick(double deltaTime);
}
