package com.example.duckgame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;

public class LevelBlueprint {

    private final String TAG = "LevelBlueprint";

    private GameWorld level;
    private PointF levelSize = new PointF(1,1);

    LevelBlueprint(Context context, int levelID, TypedArray levelData) {

        ArrayList<String> types = new ArrayList<>();
        ArrayList<Drawable> sprites = new ArrayList<>();
        ArrayList<PointF> positions = new ArrayList<>();
        ArrayList<Float> rotations = new ArrayList<>();
        ArrayList<Float> scales = new ArrayList<>();

        for (int i = 0; i < levelData.length(); ++i){
            // Get levelSize
            if (i == 0) {
                int[] levelSize = context.getResources().getIntArray(levelData.getResourceId(i, 0));
                this.levelSize.x = levelSize[0];
                this.levelSize.y = levelSize[1];
            }
            // Get levelObjects
            else {
                TypedArray levelObjects = context.getResources().obtainTypedArray(levelData.getResourceId(i, 0));
                for (int j = 0; j < levelObjects.length(); ++j){
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
        }

        level = new GameWorld(levelSize);

        // Instantiate and add levelObjects to level
        //...

        // 🚧🚧🚧 Testing Zone 🚧🚧🚧 //
        level.addPlayerObject(new Player(level, R.drawable.player, new PointF( 2F, 4F), 0, 1, new PointF(3F,3F)));
    }

    public GameWorld createGameWorld() {
        // Return a deep clone of the GameWorld made from this LevelBlueprint
        return level.clone();
    }

    /** Properties **/

    public PointF getLevelSize(){
        return levelSize;
    }
}
