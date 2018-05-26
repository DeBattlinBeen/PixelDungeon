package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSlice;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSlide;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSplit;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.TiledMapNames;

public class MainMenuScreen extends AbstractScreen {
    private Image image;
    private float panH = .2f * PixelDungeon.SCALAR;
    private float panV = .15f * PixelDungeon.SCALAR;

    @Override
    public void buildStage() {
        float padding = 25 * PixelDungeon.SCALAR;

        addActor(setupBackground());

        addActor(setupMainTable(padding));
        addActor(setupTitleTable(padding));
        addActor(setupTopRightTable(padding));
        addActor(setupBottomRightTable(padding));
        addActor(setupBottomLeftTable(padding));
        addActor(setupTopLeftTable(padding));
    }

    private Table setupMainTable(float padding) {
        Table mainTable = new Table();
        mainTable.center().pad(padding);

        TextButton playButton = new TextButton("PLAY",Assets.getInstance().getSkin(Assets.UI_SKIN));
        playButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionSplit.init(2f,ScreenTransitionSplit.HORIZONTAL,Interpolation.pow2),
                        TiledMapNames.TEST_LEVEL);
                return super.touchDown(event, x, y, pointer, button);
            }
        });



        TextButton skinButton = new TextButton("SKIN SELECTION", Assets.getInstance().getSkin(Assets.UI_SKIN));
        skinButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);


        skinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.GAME,
                        null,//ScreenTransitionSplit.init(2f,ScreenTransitionSplit.HORIZONTAL,Interpolation.pow2),
                        TiledMapNames.TEST_LEVEL);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton helpButton = new TextButton("HOW TO PLAY", Assets.getInstance().getSkin(Assets.UI_SKIN));
        helpButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);
        helpButton.setDisabled(true);

        mainTable.add(playButton).pad(padding);
        mainTable.row();
        mainTable.add(skinButton).pad(padding);
        mainTable.row();
        mainTable.add(helpButton).pad(padding);

        mainTable.setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        mainTable.setDebug(true);
        return mainTable;
    }

    private Table setupTitleTable(float padding) {
        Table titleTable = new Table();
        titleTable.top().padTop(padding);

        Label label = new Label("PIXEL DUNGEON",Assets.getInstance().getSkin(Assets.UI_SKIN), "title");
        label.setFontScale(2f * PixelDungeon.SCALAR);
        titleTable.add(label);

        titleTable.setPosition(PixelDungeon.V_WIDTH/2,
                PixelDungeon.V_HEIGHT);

        titleTable.setDebug(true);
        return titleTable;
    }

    private Table setupTopRightTable(float padding) {
        Table trTable = new Table();
        trTable.top().padTop(padding).right().padRight(padding);


        Label label = new Label("TR", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        trTable.add(label);

        trTable.setPosition(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        trTable.setDebug(true);
        return trTable;
    }

    private Table setupBottomRightTable(float padding) {
        Table brTable = new Table();
        brTable.bottom().padBottom(padding).right().padRight(padding);

        Label label = new Label("BR", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        brTable.add(label);

        brTable.setPosition(PixelDungeon.V_WIDTH,0);
        brTable.setDebug(true);
        return brTable;
    }

    private Table setupBottomLeftTable(float padding) {
        Table blTable = new Table();
        blTable.bottom().padBottom(padding).left().padLeft(padding);

        Label label = new Label("BL", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        blTable.add(label);

        blTable.setDebug(true);
        return blTable;
    }

    private Table setupTopLeftTable(float padding) {
        Table tlTable = new Table();
        tlTable.top().padTop(padding).left().padLeft(padding);

        Label label = new Label("TL", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        tlTable.add(label);

        tlTable.setPosition(0,PixelDungeon.V_HEIGHT);
        tlTable.setDebug(true);
        return tlTable;
    }

    private Image setupBackground() {
        image = new Image(
                new Texture(Gdx.files.internal("Background2.png"))
        );
        image.setSize(PixelDungeon.V_WIDTH * 1.f, PixelDungeon.V_HEIGHT * 1.f);
        //image.setPosition(0 - image.getWidth()/3, 0 - image.getHeight() / 3);
        return image;
    }

    private void update() {
        if (image.getX() > 0 || image.getX() + image.getWidth() < PixelDungeon.V_WIDTH) {
            panH *= -1;
        }
        if (image.getY() > 0 || image.getY() + image.getHeight() < PixelDungeon.V_HEIGHT) {
            panV *= -1;
        }
        image.setPosition(image.getX() + panH, image.getY() + panV);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,1);

        //update();
        draw();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
