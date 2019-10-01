package com.example.duckgame;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

public class GameWorld {

    private LinkedList<GameObject> objects = new LinkedList<>(); // All the objects in the level
    private LinkedList<ActiveGameObject> activeObjects = new LinkedList<>(); // All objects that need updating
    private Player playerObject; // The player object
    private Goal primaryGoal; // The main goal object
    private PointF size; // The dimensions of the level in game units
    private int score = 0; // The current score of the level
    private float scoreMult = 50000;
    private int miniGoalsCollected = 0; // How many minor goals have been collected

    GameWorld(PointF levelSize) {
        size = levelSize;
    }

    public void aimPlayer(PointF coords){
        if(coords.x < 0) coords.x = 0;
        if(coords.x > size.x) coords.x = size.x;
        if(coords.y < 0) coords.y = 0;
        if(coords.y > size.y) coords.y = size.y;
        playerObject.aim(coords);
    }
    public void launchPlayer(PointF coords){
        if(coords.x < 0) coords.x = 0;
        if(coords.x > size.x) coords.x = size.x;
        if(coords.y < 0) coords.y = 0;
        if(coords.y > size.y) coords.y = size.y;
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
        if (obj instanceof Goal) primaryGoal = (Goal) obj;
        if (obj instanceof ActiveGameObject) activeObjects.add((ActiveGameObject)obj);
        objects.add(obj);
    }

    public void playerStopped() {
        float playerdistx = playerObject.getPosition().x - primaryGoal.getPosition().x;
        float playerdisty = playerObject.getPosition().y - primaryGoal.getPosition().y;
        float playerDist = (float)Math.sqrt(playerdistx * playerdistx + playerdisty * playerdisty);
        float maxdist = (float)Math.sqrt(size.x * size.x + size.y * size.y);
        score = Math.round(scoreMult * (1 + miniGoalsCollected) * (1 - playerDist / maxdist));
        Log.i("SCORE", ": " + score);

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
