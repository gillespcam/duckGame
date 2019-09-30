package com.example.duckgame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;

@SuppressWarnings("ResourceType")

public class LevelBlueprint {

    private final String TAG = "LevelBlueprint";

    private GameWorld level;
    private PointF size = new PointF(1,1);

    LevelBlueprint(Context context, int levelID, TypedArray levelData) {

        // Get levelSize to instantiate model GameWorld
        TypedArray levelSize = context.getResources().obtainTypedArray(levelData.getResourceId(0, 0));
        size.x = levelSize.getFloat(0, 0F);
        size.y = levelSize.getFloat(1, 0F);
        levelSize.recycle();

        level = new GameWorld(size);

        // Instantiate and add levelObjects to level
        TypedArray levelObjects = context.getResources().obtainTypedArray(levelData.getResourceId(1, 0));
        TypedArray levelTypes = context.getResources().obtainTypedArray(levelObjects.getResourceId(0,0));
        for (int i = 0; i < levelTypes.length(); ++i) {
            for (int j = 0; j < levelObjects.length(); ++j) {
                // Get types
                //...
                // Get sprites
                //...
                // Get positions
                //...
                // Get rotations
                //...
                // Get scales
                //...
            }
        }
        levelObjects.recycle();
        levelTypes.recycle();

        // 🚧🚧🚧 Testing Zone 🚧🚧🚧 //
        level.addPlayerObject(new Player(level, R.drawable.player, new PointF( 2F, 4F), 0, 1));
    }

    public GameWorld createGameWorld() {
        // Return a deep clone of the GameWorld made from this LevelBlueprint
        return level.clone();
    }

    /** Properties **/

    public PointF getSize(){
        return level.getSize();
    }
}
