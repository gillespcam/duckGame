package com.example.duckgame;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import java.util.LinkedList;

public class GameWorld {
    // the dimensions of the level
    private PointF size;

    // all the objects in the level
    private LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
    private LinkedList<GameObject> activeObjects = new LinkedList<GameObject>();

    public void doGameTick() {
        for(GameObject obj : activeObjects){
            obj.doGameTick();
        }
    }

    public LinkedList<GameObject> getGameObjects(){
        return gameObjects;
    }

    public void addObject(GameObject obj){
        gameObjects.add(obj);
    }

    public void addActiveObject(GameObject obj){
        gameObjects.add(obj);
        activeObjects.add(obj);
    }

    public void removeObject(GameObject obj){
        gameObjects.remove(obj);
    }

    public void removeActiveObject(GameObject obj){
        gameObjects.remove(obj);
        activeObjects.remove(obj);
    }

    public PointF getSize() {
        return size;
    }

    GameWorld(){
        // initial size and objects, for testing
        size = new PointF(10, 20);

    }

}
