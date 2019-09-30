package com.example.duckgame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.Log;

import java.lang.reflect.Constructor;

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

        TypedArray levelObjects = context.getResources().obtainTypedArray(levelData.getResourceId(1, 0));
        String[] types = context.getResources().getStringArray(levelObjects.getResourceId(0,0));
        int[] sprites = context.getResources().getIntArray(levelObjects.getResourceId(1,0));
        TypedArray xpositions = context.getResources().obtainTypedArray(levelObjects.getResourceId(2,0));
        TypedArray ypositions = context.getResources().obtainTypedArray(levelObjects.getResourceId(3,0));
        TypedArray rotations = context.getResources().obtainTypedArray(levelObjects.getResourceId(4,0));
        TypedArray scales = context.getResources().obtainTypedArray(levelObjects.getResourceId(5,0));

        // Instantiate and add all levelObjects to level
        for (int i = 0; i < types.length; ++i) {

            // Get levelObject class name
            String typeName = types[i];
            Log.i(TAG, "Creating GameObject " + i + ", type " + typeName);

            try {
                // Find a class whose name matches the given string in 'types', else log an error and skip adding this object
                Class type = Class.forName("com.example.duckgame." + typeName);
                // Find a constructor for the objects class whose parameters' classes match those given, else log an error and skip adding this object
                Constructor c = type.getConstructor(GameWorld.class, int.class, PointF.class, float.class, float.class);
                //...
            }
            catch(ClassNotFoundException | NoSuchMethodException e) { Log.e(TAG, "ERROR: Cannot find class or constructor of class " + typeName + ", skipping object"); }
        }
        levelObjects.recycle();
        xpositions.recycle();
        ypositions.recycle();
        rotations.recycle();
        scales.recycle();

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
