package net.team11.pixeldungeon.map;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Map {
    private TiledMap map;
    private String mapName;
    private boolean loaded;

    public Map(String mapString) {
        this.map = new TmxMapLoader().load(mapString);
        this.loaded = false;

        //  Taking the map name from after the directory, to the extension
        //  eg: levels/level_0_0.tmx -> level_0_0
        mapName = mapString.substring(7, mapString.length()-4);
    }

    public MapObjects getObjects(String layer) {
        return map.getLayers().get(layer).getObjects();
    }

    public RectangleMapObject getRectangleObject (String layer, String objectName) {
        return (RectangleMapObject) map.getLayers().get(layer).getObjects().get(objectName);
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public TiledMap getMap() {
        return map;
    }

    public String getMapName() {
        return mapName;
    }
}