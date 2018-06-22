package net.team11.pixeldungeon.puzzles.levelpuzzle;

import net.team11.pixeldungeon.entities.puzzle.CompletedIndicator;
import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

import java.util.Locale;

public class LevelPuzzle extends Puzzle {
    private int maxParts;
    private int completedParts;

    public LevelPuzzle(String name) {
        super(name);
        maxParts = 0;
        completedParts = 0;
        activate();
    }

    @Override
    public void addComponent(PuzzleComponent puzzleComponent) {
        super.addComponent(puzzleComponent);
        if (puzzleComponent instanceof CompletedIndicator) {
            maxParts++;
        }
    }

    @Override
    public void notifyPressed(PuzzleComponent puzzleComponent) {
        super.notifyPressed(puzzleComponent);
        if (puzzleComponent instanceof CompletedIndicator) {
            if (!((CompletedIndicator) puzzleComponent).isOn()) {
                completedParts++;
                String message = Messages.LEVEL_PART_COMPLETE;
                if (completedParts == maxParts) {
                    message += ".\n" + Messages.LEVEL_PUZZLE_COMPLETE;
                    onComplete();
                } else {
                    message += ".\n" + String.format(Locale.UK, Messages.LEVEL_PART_REMAINING,
                            (maxParts - completedParts));
                }
                PlayScreen.uiManager.initTextBox(message);
            }
        }
    }

    private void onComplete() {
        completed = true;
        activated = false;
        trigger();
    }
}
