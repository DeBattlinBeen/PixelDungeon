package net.team11.pixeldungeon.game.uicomponents.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.items.Item;
import net.team11.pixeldungeon.utils.assets.Assets;

import java.util.Locale;

public class InventoryItem extends Stack {
    private float size;
    private Item item;

    public InventoryItem(float size, Item item) {
        this.size = size;
        this.item = item;

        if (item != null) {
            setupItemSlot();
        } else {
            setupNullItem();
        }
    }

    private void setupItemSlot() {
        Image icon = item.getIcon();
        icon.setSize(size,size);
        Table iconTable = new Table();
        iconTable.add(icon).size(size,size);

        Table numberTable = new Table();
        numberTable.bottom().right();
        Label numberLabel = new Label("",Assets.getInstance().getSkin(Assets.UI_SKIN));
        if (item.getAmount() > 1) {
            numberLabel.setText(String.format(Locale.UK,"%d",item.getAmount()));
        }
        numberLabel.setFontScale(0.75f * PixelDungeon.SCALAR);

        if (item.getAmount() > 0){
            numberTable.add(numberLabel);
        }

        add(iconTable);
        add(numberTable);
    }

    private void setupNullItem(){
        Table nullTable = new Table();
        nullTable.add().size(size);
        add(nullTable);
    }
}
