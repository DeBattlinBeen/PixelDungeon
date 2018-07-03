package net.team11.pixeldungeon.screens.components.coin;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sun.nio.sctp.PeerAddressChangeNotification;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.inventory.CoinAwarder;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

public class CoinAwarderDisplay extends Table {
    private CoinAwarder coinAwarder;

    private int yourCoins;
    private CoinDisplay foundCoins;
    private CoinDisplay timedCoins;
    private CoinDisplay deathCoins;
    private CoinDisplay firstTimeCoins;
    private CoinDisplay totalCoins;
    private CoinDisplay yourTotalCoins;
    private float labelSize;
    private float fontSize;

    public CoinAwarderDisplay(InventoryComponent inventory) {
        super();
        fontSize = 0.75f * PixelDungeon.SCALAR;

        coinAwarder = CoinAwarder.getInstance();
        coinAwarder.init(inventory);
        yourCoins = StatsUtil.getInstance().getGlobalStats().getCurrentCoins() -
            coinAwarder.getTotalCoinCount();

        buildTable();
    }

    private void buildTable() {
        float padding = 25f * PixelDungeon.SCALAR;

        add(setupFoundLabel()).left();
        add(setupFoundValue()).right();
        row().padTop(padding);
        add(setupTimerLabel()).left();
        add(setupTimerValue()).right();
        row().padTop(padding);
        add(setupDeathBonusLabel()).left();
        add(setupDeathBonusValue()).right();
        row().padTop(padding);
        if (coinAwarder.isFirstTime()) {
            add(setupFirstTimeLabel()).left();
            add(setupFirstTimeValue()).right();
            row().padTop(padding);
        }
        add(setupTotalLabel()).left();
        add(setupTotalValue()).right();
        row().padTop(padding*2);
        add(setupYourTotalLabel()).left();
        add(setupYourTotalValue()).right().expandX();
    }

    private Label setupTimerLabel() {
        Label label = new Label(
                Messages.COINS_TIME_COMPLETED,
                Assets.getInstance().getSkin(Assets.UI_SKIN)
        );
        label.setFontScale(fontSize);
        return label;
    }

    private Table setupTimerValue() {
        timedCoins = new CoinDisplay(labelSize,0);
        return timedCoins;
    }

    private Label setupDeathBonusLabel() {
        Label label = new Label(
                Messages.COINS_NO_DEATH_BONUS,
                Assets.getInstance().getSkin(Assets.UI_SKIN)
        );
        label.setFontScale(fontSize);
        return label;
    }

    private Table setupDeathBonusValue() {
        deathCoins = new CoinDisplay(labelSize,0);
        return deathCoins;
    }

    private Label setupFoundLabel() {
        Label label = new Label(
                Messages.COINS_FOUND_TOTAL,
                Assets.getInstance().getSkin(Assets.UI_SKIN)
        );
        label.setFontScale(fontSize);
        labelSize = label.getHeight();
        return label;
    }

    private Table setupFoundValue() {
        foundCoins = new CoinDisplay(labelSize,0);
        return foundCoins;
    }

    private Label setupTotalLabel() {
        Label label = new Label(
                Messages.COINS_TOTAL,
                Assets.getInstance().getSkin(Assets.UI_SKIN)
        );
        label.setFontScale(fontSize);
        return label;
    }

    private Table setupTotalValue() {
        totalCoins = new CoinDisplay(labelSize,0);
        return totalCoins;
    }

    private Label setupYourTotalLabel() {
        Label label = new Label(
                Messages.COINS_YOUR_TOTAL,
                Assets.getInstance().getSkin(Assets.UI_SKIN)
        );
        label.setFontScale(fontSize);
        return label;
    }

    private Table setupYourTotalValue() {
        yourTotalCoins = new CoinDisplay(labelSize,yourCoins);
        return yourTotalCoins;
    }

    private Label setupFirstTimeLabel() {
        Label label = new Label(
                Messages.COINS_FIRST_TIME,
                Assets.getInstance().getSkin(Assets.UI_SKIN)
        );
        label.setFontScale(fontSize);
        return label;
    }

    private Table setupFirstTimeValue() {
        firstTimeCoins = new CoinDisplay(labelSize,0);
        return firstTimeCoins;
    }

    public void update() {
        if (foundCoins.getValue() < coinAwarder.getFoundCoins()) {
            foundCoins.setValue(foundCoins.getValue() + 10);
            totalCoins.setValue(totalCoins.getValue() + 10);
        } else if (timedCoins.getValue() < coinAwarder.getTimeCoins()) {
            timedCoins.setValue(timedCoins.getValue() + 10);
            totalCoins.setValue(totalCoins.getValue() + 10);
        } else if (deathCoins.getValue() < coinAwarder.getNoDeathValue()) {
            deathCoins.setValue(deathCoins.getValue() + 10);
            totalCoins.setValue(totalCoins.getValue() + 10);
        } else if (coinAwarder.isFirstTime() &&
                (firstTimeCoins.getValue() < coinAwarder.getFirstTimeCoins())) {
            firstTimeCoins.setValue(firstTimeCoins.getValue() + 10);
            totalCoins.setValue(totalCoins.getValue() + 10);
        } else if (totalCoins.getValue() > 0) {
            totalCoins.setValue(totalCoins.getValue() - 10);
            yourTotalCoins.setValue(yourTotalCoins.getValue() + 10);
        }
    }
}
