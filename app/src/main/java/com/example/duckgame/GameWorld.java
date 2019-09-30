package com.example.duckgame;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

public class GameWorld {

    private LinkedList<GameObject> objects = new LinkedList<>(); // All the objects in the level
    private LinkedList<ActiveGameObject> activeObjects = new LinkedList<>(); // All objects that need updating
    private Player playerObject; // The player object
    private PointF size; // The dimensions of the level in game units

    GameWorld(PointF levelSize) {
        size = levelSize;
    }

    public void aimPlayer(PointF coords){
        playerObject.aim(coords);
    }
    public void launchPlayer(PointF coords){
        playerObject.launch(coords);
    }

    public void tick(double deltaTime) {
        deltaTime /= 1000000000; // Convert deltaTime from nanoseconds to seconds
        for(ActiveGameObject obj : activeObjects){
            obj.tick(deltaTime);
        }
    }

    public void draw(GraphicsView graphicsView, Canvas canvas) {
        graphicsView.setGameObjects(objects);
        graphicsView.draw(canvas);
    }

    public void addObject(GameObject obj) {
        if (obj instanceof Player) playerObject = (Player)obj;
        if (obj instanceof ActiveGameObject) activeObjects.add((ActiveGameObject)obj);
        objects.add(obj);
    }

    public GameWorld clone(){
        GameWorld r = new GameWorld(size);
        for (GameObject obj : objects) {
            r.addObject(obj.clone(r, obj.sprite, obj.position, obj.rotation, obj.scale));
        }
        return r;
    }

    /** Properties **/

    public LinkedList<GameObject> getObjects() {
        return objects;
    }
    public PointF getSize() {
        return size;
    }
}
