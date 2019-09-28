package com.example.duckgame;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.LinkedList;

public class GameWorld {

    private LinkedList<GameObject> Objects; // All the objects in the level
    private LinkedList<GameObject> activeObjects; // All objects that need updating
    private PointF size; // The dimensions of the level in game units

    GameWorld(LinkedList<GameObject> levelObjects, PointF levelSize) {
        Objects = levelObjects;
        //TODO: add only certain types of GameObjects to activeObjects
        // i.e. objects that change position, rotation, scale, sprite,
        // or objects with their own collision checking
        activeObjects = levelObjects;

        // 🚧🚧🚧 Testing Zone 🚧🚧🚧 //
        size = new PointF(16, 8);
        addActiveObject(new PlayerProjectile(this, R.drawable.player, new PointF( 1F, 4F), 0, 1, new PointF(0.2F,0.1F)));
    }

    public void tick(double deltaTime) {
        deltaTime /= 1000000000; // Convert deltaTime from nanoseconds to seconds
        for(GameObject obj : activeObjects){
            obj.tick(deltaTime);
        }
    }

    public void draw(GraphicsView graphicsView, Canvas canvas) {
        graphicsView.setGameObjects(Objects);
        graphicsView.draw(canvas);
    }

    public void addObject(GameObject obj) {
        Objects.add(obj);
    }

    public void addActiveObject(GameObject obj) {
        Objects.add(obj);
        activeObjects.add(obj);
    }

    public void removeObject(GameObject obj) {
        Objects.remove(obj);
    }

    public void removeActiveObject(GameObject obj) {
        Objects.remove(obj);
        activeObjects.remove(obj);
    }

    /** Properties **/

    public LinkedList<GameObject> getObjects() {
        return Objects;
    }
    public PointF getSize() {
        return size;
    }
}
