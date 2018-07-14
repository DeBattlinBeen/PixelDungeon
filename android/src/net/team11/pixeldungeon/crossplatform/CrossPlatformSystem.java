package net.team11.pixeldungeon.crossplatform;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.R;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;
import net.team11.pixeldungeon.utils.stats.GlobalStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

public class CrossPlatformSystem implements AndroidInterface {
    private String TAG = "PixelDungeon";
    private AndroidLauncher mActivity; // This is the main android activity
    private boolean earnAchievements = false;

    private Snapshot currentSnapshot;

    public CrossPlatformSystem(AndroidLauncher mActivity){
        this.mActivity = mActivity;
    }

    @Override
    public String getUserName() {
        return mActivity.getUserName();
    }

    @Override
    public String getUserEmail() {
        return null;
    }

    @Override
    public boolean isSignedIn() {
        return mActivity.isSignedIn();
    }

    @Override
    public void signIn() {
        mActivity.startSignInIntent();
    }

    @Override
    public void signInSilently() {
        mActivity.signInSilently();
    }

    @Override
    public void signOut() {
        mActivity.signOut();
    }

    @Override
    public void openAchievements() {
        if (mActivity.getmSignedInAccount() != null) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            mActivity.startActivityForResult(intent, AndroidLauncher.RC_ACHIEVEMENT_UI);
                        }
                    });
        }
    }

    @Override
    public void openLeaderboards() {
        if (mActivity.getmSignedInAccount() != null) {
            Games.getLeaderboardsClient(mActivity, mActivity.getmSignedInAccount())
                    .getAllLeaderboardsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            mActivity.startActivityForResult(intent, AndroidLauncher.RC_LEADERBOARD_UI);
                        }
                    });
        }
    }

    @Override
    public void earnNewAdventurer() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_new_adventurer));
        }
    }

    @Override
    public void earnLetsTryAgain() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_lets_try_again__));
        }
    }

    @Override
    public void earn10Attempts() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 10 Attempts!");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_10_dungeons), 1);
        }
    }

    @Override
    public void earn100Attempts() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 100 Attempts!");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_100_dungeons), 1);
        }
    }

    @Override
    public void earn500Attempts() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 500 Attempts!");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_500_dungeons), 1);
        }
    }

    @Override
    public void earn1000Attempts() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 1000 Attempts!");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_1000_dungeons), 1);
        }
    }

    @Override
    public void earnCompleteDungeon1() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_1));
        }
    }

    @Override
    public void earnCompleteDungeon5() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_5));
        }
    }

    @Override
    public void earnCompleteDungeon10() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_10));
        }
    }

    @Override
    public void earnCompleteDungeon15() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_15));
        }
    }

    @Override
    public void earnCompleteDungeon20() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_20));
        }
    }

    @Override
    public void earnCompleteDungeon25() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_25));
        }
    }

    @Override
    public void earnCompletePuzzle1000() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 1000 Puzzle!");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_puzzle_master), 1);
        }
    }

    @Override
    public void earnSimonSays() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_simon_says));
        }
    }

    @Override
    public void earnColoursAligned() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_the_colours_have_aligned));
        }
    }

    @Override
    public void earnKeyMaster(int amount) {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Key master by " + amount + ".");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_key_master), amount);
        }
    }

    @Override
    public void earnLootJunkie(int amount) {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Loot junkie by " + amount + ".");
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_loot_junkie), amount);
        }
    }

    @Override
    public void earnFullInventory() {
        if (mActivity.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_i_cant_carry_any_more));
        }
    }

    @Override
    public void saveGame(SaveGame saveGame){

    }

    @Override
    public SaveGame loadSaveGame(){
            return null;
    }

}
