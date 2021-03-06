package net.team11.pixeldungeon.game.entitysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {
    protected String name;
    private UUID uuid;
    private List<EntityComponent> entityComponents = new ArrayList<>();

    protected List<Entity> targetEntities = new ArrayList<>();
    private List<String> targets = new ArrayList<>();
    private boolean trigger = false;

    protected Entity() {
        this.uuid = UUID.randomUUID();
    }

    protected Entity(String name){
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    protected void addComponent(EntityComponent component) {
        this.entityComponents.add(component);
    }

    public boolean hasComponent(Class<? extends EntityComponent> component) {
        for (EntityComponent entityComponent : entityComponents) {
            if (component.isInstance(entityComponent)) {
                return true;
            }
        }
        return false;
    }

    public <T extends EntityComponent> T getComponent(Class<T> component) {
        for (EntityComponent entityComponent : entityComponents) {
            if (component.isInstance(entityComponent)) {
                return component.cast(entityComponent);
            }
        }
        return null;
    }

    //  Void ; to be overridden by child if needs to know if player is the entity interacting
    public void doInteraction(boolean isPlayer){}

    //  Void ; to be overridden by child if necessary
    public void respawn() {}

    public boolean hasTrigger() {
        return this.trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public void setTargets(List<String> entities) {
        while (!entities.isEmpty()) {
            if (!targets.contains(entities.get(0))) {
                targets.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public List<String> getTargets() {
        return targets;
    }

    public List<Entity> getTargetEntities() {
        return targetEntities;
    }

    public void setTargetEntities(List<Entity> entities) {
        while (!entities.isEmpty()) {
            if (!targetEntities.contains(entities.get(0))) {
                targetEntities.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
