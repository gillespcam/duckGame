package com.example.duckgame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

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
            if (i == 1) {
                int[] levelSize = context.getResources().getIntArray(levelData.getResourceId(i, 0));
                this.levelSize.x = levelSize[0];
                this.levelSize.y = levelSize[1];
            }
            // Get levelObjectsData
            else {
            }
        }

        level = new GameWorld(levelSize);

        // 🚧🚧🚧 Testing Zone 🚧🚧🚧 //
        level.addPlayerObject(new Player(level, R.drawable.player, new PointF( 2F, 4F), 0, 1, new PointF(3F,3F)));

        // Create and add levelObjects
        /*for (int i = 0; i < types.size(); ++i) {
            switch (types.get(i)){
                case "Player":
                    break;
                case "Goal":
                    break;
                default:
                    Log.e(TAG, "ERROR: Specified GameObject in level specification does not exist, skipping object");
            }
        }*/
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
