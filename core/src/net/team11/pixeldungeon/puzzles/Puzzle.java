package net.team11.pixeldungeon.puzzles;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entities.puzzle.PuzzleController;
import net.team11.pixeldungeon.entitysystem.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Puzzle {
    protected String name;
    private UUID uuid;

    protected boolean completed;
    protected boolean activated;
    protected float attempts;
    protected float maxAttempts;
    protected float timer;
    protected float timerReset;

    protected PuzzleController puzzleController;
    protected HashMap<Integer, PuzzleComponent> puzzleComponents = new HashMap<>();
    protected List<Entity> targetEntities = new ArrayList<>();
    private List<String> targets = new ArrayList<>();

    protected Puzzle(String name) {
        this.name = name;
        uuid = UUID.randomUUID();
    }

    public void activate() {
        this.activated = true;
        init();
    }

    protected void init() {}

    public boolean isActivated() {
        return this.activated;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void addComponent(PuzzleComponent puzzleComponent) {
        puzzleComponents.put(puzzleComponents.size(),puzzleComponent);
    }

    public void setController(PuzzleController controller) {
        this.puzzleController = controller;
    }

    public void setTargets() {
        targetEntities.addAll(puzzleController.getTargetEntities());
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
        return name;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public void deactivate() {
        activated = false;
    }

    public void update(float delta) {

    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
