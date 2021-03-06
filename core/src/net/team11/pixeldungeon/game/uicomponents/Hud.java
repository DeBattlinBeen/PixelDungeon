package net.team11.pixeldungeon.game.uicomponents;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.utils.RoundTo;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

import java.util.Locale;

public class Hud extends Stage {
    private Viewport viewport;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean interactPressed, inventoryPressed, pausePressed;
    private boolean visible;

    private float timer = 0;
    private Label timeLabel;
    private Label fps;

    public Hud(SpriteBatch batch){
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), batch);
        viewport = super.getViewport();
        visible = true;

        setupHud();
        Gdx.input.setInputProcessor(this);
    }

    private void setupHud() {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.HUD);
        Image texture = new Image(textureAtlas.findRegion(AssetName.UP_BUTTON));
        float width = texture.getWidth()*10*PixelDungeon.SCALAR;
        float height = texture.getHeight()*10*PixelDungeon.SCALAR;

        //  Bottom Left
        setupController(textureAtlas, width, height);
        //  Bottom Right
        setupInteract(textureAtlas, width, height);
        //  Top Right
        setupPause(textureAtlas, width, height);
        //  Top Left
        setupTimer(width, height);
    }

    private void setupController(TextureAtlas textureAtlas, float width, float height) {
        Image upImg = new Image(textureAtlas.findRegion(AssetName.UP_BUTTON));
        upImg.setSize(width, height);
        upImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return upPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image downImg = new Image(textureAtlas.findRegion(AssetName.DOWN_BUTTON));
        downImg.setSize(width, height);
        downImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return downPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        Image rightImg = new Image(textureAtlas.findRegion(AssetName.RIGHT_BUTTON));
        rightImg.setSize(width, height);
        rightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return rightPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(textureAtlas.findRegion(AssetName.LEFT_BUTTON));
        leftImg.setSize(width, height);
        leftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return leftPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });


        Table controllerTable = new Table();
        controllerTable.left().padLeft(width/4).bottom().padBottom(height/4);
        controllerTable.add();
        controllerTable.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        controllerTable.add();
        controllerTable.row().pad(5, 5, 5, 5);
        controllerTable.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        controllerTable.add();
        controllerTable.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        controllerTable.row().padBottom(5);
        controllerTable.add();
        controllerTable.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        controllerTable.add();

        addActor(controllerTable);
    }

    private void setupInteract(TextureAtlas textureAtlas, float width, float height) {
        //
        //  INTERACTION TABLE
        //
        Image interactImg = new Image(textureAtlas.findRegion(AssetName.INTERACT_BUTTON));
        interactImg.setSize(width,height);
        interactImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return interactPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                interactPressed = false;
            }
        });
        final Image inventoryImg = new Image(textureAtlas.findRegion(AssetName.INVENTORY_BUTTON));
        inventoryImg.setSize(width,height);
        inventoryImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return inventoryPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                inventoryPressed = false;
            }
        });
        Table interactionTable = new Table();
        interactionTable.setPosition(PixelDungeon.V_WIDTH,0);
        interactionTable.right().padRight(width/4).bottom().padBottom(height/4);
        interactionTable.add();
        interactionTable.add(inventoryImg).size(width,height);
        interactionTable.row();
        interactionTable.add(interactImg).size(width,height);
        interactionTable.add();
        interactionTable.row();
        addActor(interactionTable);
    }

    private void setupPause(TextureAtlas textureAtlas, float width, float height) {
        Image pauseImg = new Image(textureAtlas.findRegion(AssetName.PAUSE_BUTTON));
        pauseImg.setSize(width,height);
        pauseImg.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return pausePressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pausePressed = false;
            }
        });

        fps = new Label("",Assets.getInstance().getSkin(Assets.UI_SKIN));
        fps.setFontScale(1.2f * PixelDungeon.SCALAR);

        Table pauseTable = new Table();
        pauseTable.setPosition(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        pauseTable.right().padRight(width/4).top().padTop(height/4);
        pauseTable.add(fps).padRight(width/4);
        pauseTable.add(pauseImg).size(width,height);
        addActor(pauseTable);}

    private void setupTimer(float width, float height) {
        Util.getInstance().getStatsUtil().startTimer();
        timeLabel = new Label(Util.getInstance().getStatsUtil().getTimerString(),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        timeLabel.setFontScale(1.2f * PixelDungeon.SCALAR);

        Table timerTable = new Table();
        timerTable.setPosition(0,PixelDungeon.V_HEIGHT);
        timerTable.left().padLeft(width/4).top().padTop(height/4);
        timerTable.add(timeLabel).size(width,height);

        addActor(timerTable);
    }

    public void update (float delta) {
        fps.setText(String.format(Locale.UK,"%02.0f", RoundTo.RoundToNearest(1/delta,1)));
        timer += delta;
        if (timer >= 1) {
            Util.getInstance().getStatsUtil().incrementTimer();
            timer = 0;
            timeLabel.setText(Util.getInstance().getStatsUtil().getTimerString());
        }
        if (interactPressed) {
            interactPressed = false;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        setPressed(false);
        if (visible) {
            Gdx.input.setInputProcessor(this);
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isInteractPressed() {
        return interactPressed;
    }

    public boolean isInventoryPressed() {
        return inventoryPressed;
    }

    public boolean isPausePressed() {
        return pausePressed;
    }

    public void setPressed(boolean pressed) {
        upPressed = pressed;
        downPressed = pressed;
        leftPressed = pressed;
        rightPressed = pressed;
        interactPressed = pressed;
        inventoryPressed = pressed;
        pausePressed = pressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void dispose() {
        super.dispose();
    }
}