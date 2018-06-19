package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.system.AnimationSystem;
import net.team11.pixeldungeon.entity.system.CameraSystem;
import net.team11.pixeldungeon.entity.system.HealthSystem;
import net.team11.pixeldungeon.entity.system.InteractionSystem;
import net.team11.pixeldungeon.entity.system.PlayerMovementSystem;
import net.team11.pixeldungeon.entity.system.PuzzleSystem;
import net.team11.pixeldungeon.entity.system.RenderSystem;
import net.team11.pixeldungeon.entity.system.TrapSystem;
import net.team11.pixeldungeon.entity.system.VelocitySystem;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.uicomponents.UIManager;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.tiled.TiledMapLayers;
import net.team11.pixeldungeon.utils.tiled.TiledMapObjectNames;
import net.team11.pixeldungeon.map.MapManager;

import box2dLight.RayHandler;

public class PlayScreen extends AbstractScreen {
    public static RayHandler rayHandler;
    public static UIManager uiManager;

    private MapManager mapManager;
    private String levelName;

    private EntityEngine engine;
    private PlayerMovementSystem playerMovementSystem;

    public static World world;
    private Box2DDebugRenderer b2dr;
    private Player player;

    private boolean paused;

    private float ambientLight = 0f;

    public PlayScreen(String levelName) {
        this.levelName = levelName;
        paused = false;
    }

    @Override
    public void buildStage() {
        StatsUtil.getInstance().initialiseCurrStats();
        setupCamera();
        setupViewport();
        setupEngine();
        setupLight();
        setupPlayer(levelName);
        uiManager = new UIManager(game.batch,engine,player);
        playerMovementSystem.setUIManager(uiManager);

        mapManager.loadMap(levelName);
        engine.resume();
    }

    @Override
    public void show() {
        if (!paused) {
            engine.resume();
        }
    }

    private void setupCamera() {
        gameCam.setToOrtho(false, PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        gameCam.zoom = 0.13f/(PixelDungeon.SCALAR);
        this.mapManager = MapManager.getInstance();
        this.mapManager.reset();
        gameCam.update();
    }

    private void setupViewport() {
        viewport.setScreenPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
    }

    private void setupEngine() {
        engine = new EntityEngine();
        engine.pause();
        engine.addSystem(new RenderSystem(game.batch));
        engine.addSystem(playerMovementSystem = new PlayerMovementSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new VelocitySystem());
        engine.addSystem(new CameraSystem());
        engine.addSystem(new HealthSystem());
        engine.addSystem(new InteractionSystem());
        engine.addSystem(new TrapSystem());
        engine.addSystem(new PuzzleSystem());

        mapManager.setEngine(engine);
    }

    private void setupPlayer(String mapName) {
        RectangleMapObject mapObject = mapManager.getMap(mapName).getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.SPAWN_POINT);
        float posX = mapObject.getRectangle().getX();
        float posY = mapObject.getRectangle().getY();
        engine.addEntity(player = new Player(posX, posY));
    }

    private void setupLight() {
        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();

        //*
        rayHandler = new RayHandler(world, (int)(PixelDungeon.V_WIDTH/24/PixelDungeon.SCALAR),
                (int)(PixelDungeon.V_HEIGHT/24/PixelDungeon.SCALAR));
        rayHandler.setCombinedMatrix(gameCam);
        rayHandler.setShadows(true);
        rayHandler.setAmbientLight(ambientLight);
        rayHandler.getLightMapTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        //*/
    }

    private void handleInput(float deltaTime){
        if (!paused) {
            playerMovementSystem.update(deltaTime);
        }
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        gameCam.update();
        rayHandler.update();
        rayHandler.setCombinedMatrix(gameCam);
        game.batch.setProjectionMatrix(gameCam.combined);
        world.step(1 / 60f, 6, 2);
        uiManager.update(deltaTime,paused);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(88,61,52,0);
        engine.update(delta);

        b2dr.render(world, gameCam.combined);
        if (ambientLight < 0.75f && !paused) {
            ambientLight += 0.01f;
            rayHandler.setAmbientLight(ambientLight);
        }
        rayHandler.render();
        uiManager.draw();
    }

    @Override
    public void resize(int width, int height) {
        //viewport.update(width, height);
        //uiManager.getHud().resize(width,height);
    }

    @Override
    public void pause() {
        paused = true;
        uiManager.showPauseMenu(false);
        engine.pause();
    }

    @Override
    public void resume() {
        if (paused && uiManager.getPauseMenu().isResumePressed()) {
            paused = false;
            uiManager.hidePauseMenu(false);
            engine.resume();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();
        rayHandler.dispose();
        uiManager.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return uiManager.getHud();
    }
}
