package com.example.duckgame;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("ResourceType")

public class LevelBlueprint {

    private final String TAG = "LevelBlueprint";

    private GameWorld level;

    private int levelID;
    private PointF levelSize;

    LevelBlueprint(Context context, int levelID, TypedArray levelData) {
        this.levelID = levelID;

        // Get levelSize to instantiate model GameWorld
        TypedArray levelSize = context.getResources().obtainTypedArray(levelData.getResourceId(0, 0));
        this.levelSize = new PointF(levelSize.getFloat(0, 0F), levelSize.getFloat(1, 0F));
        levelSize.recycle();

        level = new GameWorld(this.levelSize);

        TypedArray levelObjects = context.getResources().obtainTypedArray(levelData.getResourceId(1, 0));
        String[] types = context.getResources().getStringArray(levelObjects.getResourceId(0,0));
        TypedArray sprites = context.getResources().obtainTypedArray(levelObjects.getResourceId(1,0));
        TypedArray xPositions = context.getResources().obtainTypedArray(levelObjects.getResourceId(2,0));
        TypedArray yPositions = context.getResources().obtainTypedArray(levelObjects.getResourceId(3,0));
        TypedArray rotations = context.getResources().obtainTypedArray(levelObjects.getResourceId(4,0));
        TypedArray scales = context.getResources().obtainTypedArray(levelObjects.getResourceId(5,0));
        Player playerObject = null;

        // Instantiate and add all levelObjects to level
        for (int i = 0; i < types.length; ++i) {

            // Get levelObject class name
            String typeName = types[i];
            Log.i(TAG, "Creating GameObject " + i + ", type " + typeName);

            try {
                // Find a class whose name matches the given string in 'types', else log an error and skip adding this object
                Class type = Class.forName("com.example.duckgame." + typeName);
                // Find a constructor for the objects class whose parameters' classes match those given, else log an error and skip adding this object
                Constructor constructor = type.getConstructor(GameWorld.class, int.class, PointF.class, float.class, float.class);

                // Get necessary GameObject parameters
                int sprite = sprites.getResourceId(i, -1);
                PointF position = new PointF(xPositions.getFloat(i, 0F), yPositions.getFloat(i, 0F));
                float rotation = rotations.getFloat(i, 0F);
                float scale = scales.getFloat(i,0F);
                Log.i(TAG, "GameObject " + i + " given sprite " + context.getResources().getResourceEntryName(sprite) +
                        ", position (" + position.x + "," + position.y + "), rotation " + rotation + ", and scale " + scale);

                // Instantiate and add GameObject to level
                GameObject obj = (GameObject)constructor.newInstance(level, sprite, position, rotation, scale);
                if (obj instanceof Player) playerObject = (Player)obj;
                else level.addObject(obj);
                Log.i(TAG, "GameObject " + i + " added successfully to level!");
            }
            catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                Log.e(TAG, "ERROR: Cannot find class or constructor of class " + typeName + ", skipping object");
            }
        }
        levelObjects.recycle();
        xPositions.recycle();
        yPositions.recycle();
        rotations.recycle();
        scales.recycle();

        if (playerObject != null) level.addObject(playerObject);
    }

    public GameWorld createGameWorld() {
        // Return a deep clone of the GameWorld made from this LevelBlueprint
        return level.clone();
    }

    /** Properties **/

    public int getLevelID() { return levelID; }
    public PointF getSize(){
        return levelSize;
    }
}
