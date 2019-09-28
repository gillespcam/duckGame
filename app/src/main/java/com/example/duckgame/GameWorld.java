package com.example.duckgame;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import java.util.LinkedList;

public class GameWorld {
    // the dimensions of the level
    private PointF size;

    // all the objects in the level
    private LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
    private LinkedList<GameObject> activeObjects = new LinkedList<GameObject>();

    public void doGameTick(float deltat) {
        for(GameObject obj : activeObjects){
            obj.doGameTick(deltat);
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

    public void draw(GraphicsView graphicsView, Canvas canvas, float deltat) {
        graphicsView.setGameObjects(gameObjects);
        doGameTick(deltat);
        graphicsView.draw(canvas);
    }

    GameWorld(PointF size){
        // initial size and objects, for testing
        this.size = size;
        PlayerProjectile proj = new PlayerProjectile(this, new PointF(0.0000000002F,0.0000000001F));
        proj.setPosition(new PointF( 1F, 4F));
        addActiveObject(proj);
    }

}
