package net.team11.pixeldungeon.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

public class Coin extends Item {
    public Coin(int amount) {
        super("coin", Type.COIN,false);
        this.amount = amount;
        this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                .findRegion(AssetName.COIN));
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }
}