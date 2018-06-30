package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.components.levelselector.LevelInfo;
import net.team11.pixeldungeon.screens.components.levelselector.LevelSelector;
import net.team11.pixeldungeon.screens.components.skinselector.SkinSelector;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionPush;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class SkinSelectScreen extends AbstractScreen {
    private SkinSelector selector;

    @Override
    public void buildStage() {
        float padding = 25 * PixelDungeon.SCALAR;
        addActor(setupBackground());
//        addActor(setupMainTable(padding));
        addActor(setupLeft());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.SKIN_SELECT.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH * 1.f, PixelDungeon.V_HEIGHT * 1.f);
        //image.setPosition(0 - image.getWidth()/3, 0 - image.getHeight() / 3);
        return backgroundImage;
    }

    private Table setupMainTable(float padding) {
        Table mainTable = new Table();
        mainTable.center().pad(padding);

        TextButton backButton = new TextButton(Messages.BACK_UPPER,Assets.getInstance().getSkin(Assets.UI_SKIN));
        backButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionPush.init(1.5f,ScreenTransitionPush.RIGHT,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        mainTable.add(backButton).pad(padding);

        mainTable.setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        mainTable.setDebug(true);
        return mainTable;
    }


    private WidgetGroup setupLeft() {
        selector = new SkinSelector(PixelDungeon.V_WIDTH / 5 * 3);
        selector.setBounds(PixelDungeon.V_WIDTH/5*2,0,PixelDungeon.V_WIDTH/5*3, PixelDungeon.V_HEIGHT);
        return selector;
    }

    private WidgetGroup setupRight() {
        //info = new LevelInfo(selector);
        //info.setBounds(selector.getWidth(),0,PixelDungeon.V_WIDTH/3, PixelDungeon.V_HEIGHT);
        return null;
    }



    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}

