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
        objects.add(obj);
    }

    public void addActiveObject(ActiveGameObject obj) {
        objects.add(obj);
        activeObjects.add(obj);
    }

    public void addPlayerObject(Player obj) {
        objects.add(obj);
        activeObjects.add(obj);
        playerObject = obj;
    }

    public void removeObject(GameObject obj) {
        objects.remove(obj);
    }

    public void removeActiveObject(ActiveGameObject obj) {
        objects.remove(obj);
        activeObjects.remove(obj);
    }

    public GameWorld clone(){
        GameWorld r = new GameWorld(size);
        for (GameObject obj : objects) {
            if (obj instanceof Player) r.addPlayerObject((Player)obj.clone(r, obj.sprite, obj.position, obj.rotation, obj.scale));
            else if(obj instanceof ActiveGameObject) r.addActiveObject((ActiveGameObject)obj.clone(r, obj.sprite, obj.position, obj.rotation, obj.scale));
            else r.addObject(obj.clone(r, obj.sprite, obj.position, obj.rotation, obj.scale));
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
