package net.team11.pixeldungeon.game.items.keys;

import net.team11.pixeldungeon.game.items.Item;

public class Key extends Item {
    protected static final String chestKeyName = "Chest Key";
    protected static final String doorKeyName = "Door Key";
    protected static final String endKeyName = "Dungeon Key";
    public Key(String name) {
        super(name,true);
    }
}
