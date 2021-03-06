package net.team11.pixeldungeon.game.entities.puzzle;

import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.puzzles.Puzzle;

public class PuzzleComponent extends Entity {
    protected Puzzle parentPuzzle;

    protected PuzzleComponent(String name) {
        super(name);
    }

    public void setParentPuzzle(Puzzle parentPuzzle) {
        if (parentPuzzle != null) {
            this.parentPuzzle = parentPuzzle;
            parentPuzzle.addComponent(this);
        }
    }
}
