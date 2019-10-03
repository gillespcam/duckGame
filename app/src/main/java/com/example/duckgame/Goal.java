package com.example.duckgame;

import android.graphics.PointF;

/** Marker indicating where the player needs to aim to get **/
public class Goal extends GameObject {

    private final String TAG = "Goal";

    public Goal(GameWorld parent, int sprite, PointF position, float rotation, float scale){
        super(parent, sprite, position, rotation, scale);
    }

    /** GameObject **/

    public Goal clone(GameWorld parent, int sprite, PointF position, float rotation, float scale) {
        return new Goal(parent, sprite, position, rotation, scale);
    }
}
