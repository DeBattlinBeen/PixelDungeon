package net.team11.pixeldungeon.game.entitysystem;

import net.team11.pixeldungeon.game.entity.system.RenderSystem;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.game.tutorial.TutorialZone;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntityEngine {
    private EntityManager entityManager;
    private LinkedList<EntitySystem> systems;
    private ArrayList<Puzzle> puzzles;
    private ArrayList<TutorialZone> tutorials;

    private boolean finished;
    private boolean paused;

    public EntityEngine() {
        this.entityManager = new EntityManager();
        systems = new LinkedList<>();
        puzzles = new ArrayList<>();
        tutorials = new ArrayList<>();
    }

    public void update(float delta) {
        if (finished) {
            return;
        }
        if (paused) {
            for (EntitySystem entitySystem : systems) {
                if (entitySystem.getClass().equals(RenderSystem.class)) {
                    ((RenderSystem)entitySystem).updatePaused();
                    return;
                }
            }
        }

        for (EntitySystem entitySystem : systems) {
            entitySystem.update(delta);
        }
    }

    public void addEntity(Entity entity) {
        entityManager.addEntity(entity);
        updateSystems();
    }

    public void addPuzzle(Puzzle puzzle) {
        puzzles.add(puzzle);
        updateSystems();
    }

    public final Puzzle getPuzzle(String name) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getName().equals(name)) {
                return puzzle;
            }
        }
        return null;
    }

    public void addTutorial(TutorialZone tutorialZone) {
        tutorials.add(tutorialZone);
    }

    public final ArrayList<TutorialZone> getTutorials() {
        return tutorials;
    }

    public final List<Puzzle> getPuzzles() {
        return puzzles;
    }

    public List<Entity> getEntities(Class<? extends EntityComponent> componentType) {
        return this.entityManager.getEntities(componentType);
    }

    @SafeVarargs
    public final List<Entity> getEntities(Class<? extends EntityComponent>... componentType) {
        return this.entityManager.getEntities(componentType);
    }

    public void addSystem(EntitySystem system) {
        this.systems.add(system);
    }

    public <T extends EntitySystem> T getSystem(Class<T> system) {
        for (EntitySystem eComponent : this.systems) {
            if (system.isInstance(eComponent)) {
                return system.cast(eComponent);
            }
        }

        return null;
    }

    private void updateSystems() {
        for (EntitySystem system : this.systems) {
            system.init(this);
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public void finish() {
        this.finished = true;
    }

    public boolean isFinished() {
        return finished;
    }
}
