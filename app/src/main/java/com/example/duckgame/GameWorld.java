package com.example.duckgame;

import android.graphics.PointF;
import android.util.Log;

import java.util.LinkedList;

public class GameWorld {

    private final String TAG = "GameWorld";

    private final float MAX_SCORE = 50000; // The max score of the world

    private PointF size; // The dimensions of the world in game units
    private LinkedList<GameObject> objects = new LinkedList<>(); // All the objects in the level
    private LinkedList<ActiveGameObject> activeObjects = new LinkedList<>(); // All objects that need updating

    private Player player; // The main player object
    private Goal goal; // The main goal object

    private int score = 0; // The current score of the world
    private int bonuses = 0; // Amount of bonuses collected

    GameWorld(PointF levelSize) {
        size = levelSize;
    }

    public void aimPlayer(PointF coords){
        if(coords.x < 0) coords.x = 0;
        if(coords.x > size.x) coords.x = size.x;
        if(coords.y < 0) coords.y = 0;
        if(coords.y > size.y) coords.y = size.y;
        player.aim(coords);
    }
    public void launchPlayer(PointF coords){
        if(coords.x < 0) coords.x = 0;
        if(coords.x > size.x) coords.x = size.x;
        if(coords.y < 0) coords.y = 0;
        if(coords.y > size.y) coords.y = size.y;
        player.launch(coords);
    }

    public void tick(double deltaTime) {
        deltaTime /= 1000000000; // Convert deltaTime from nanoseconds to seconds
        for(ActiveGameObject obj : activeObjects){
            obj.tick(deltaTime);
        }
    }

    public void addObject(GameObject obj) {
        if (obj instanceof Player) player = (Player)obj;
        if (obj instanceof Goal) goal = (Goal)obj;
        if (obj instanceof ActiveGameObject) activeObjects.add((ActiveGameObject)obj);
        objects.add(obj);
    }

    public void playerStopped() {
        float playerdistx = player.getPosition().x - goal.getPosition().x;
        float playerdisty = player.getPosition().y - goal.getPosition().y;
        float playerDist = (float)Math.sqrt(playerdistx * playerdistx + playerdisty * playerdisty);
        float maxdist = (float)Math.sqrt(size.x * size.x + size.y * size.y);
        score = Math.round(MAX_SCORE * (1 + bonuses) * (1 - playerDist / maxdist));
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
    public Player getPlayer() { return player; }
}
