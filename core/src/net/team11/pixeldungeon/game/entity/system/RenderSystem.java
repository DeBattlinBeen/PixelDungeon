package net.team11.pixeldungeon.game.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.beams.Beam;

import net.team11.pixeldungeon.game.entities.blocks.PressurePlate;

import net.team11.pixeldungeon.game.entities.blocks.Torch;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;

import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
import net.team11.pixeldungeon.game.map.MapManager;

import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RenderSystem extends EntitySystem {
    public static float FRAME_SPEED = 15;

    private SpriteBatch spriteBatch;
    private Player player;
    private List<Entity> entities;
    private List<Entity> drawList;
    private MapManager mapManager;

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();

        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
        entities = new ArrayList<>();
        entities = entityEngine.getEntities(AnimationComponent.class);
        drawList = new ArrayList<>();
    }

    @Override
    public void update(float delta) {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        drawList = new ArrayList<>();
        ArrayList<Entity> restOfList = sortDrawList();

        spriteBatch.begin();
        for (Entity entity : drawList) {
            if (entity instanceof Torch && ((int)(delta*100000))%6 == 0) {
                ((Torch) entity).setLightSize(new Random().nextInt(10)+40f);
            }
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            float width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            if (!(entity instanceof Beam)) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - width/2,
                        bodyComponent.getY() - bodyComponent.getHeight()/2,
                        width,
                        height);
            } else {
                if (((Beam) entity).isOn()) {
                    spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                            bodyComponent.getX() - bodyComponent.getWidth()/2,
                            bodyComponent.getY() - bodyComponent.getHeight()/2,
                            bodyComponent.getWidth(),
                            bodyComponent.getHeight());
                }
            }

            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * FRAME_SPEED));
            if (animationComponent.getCurrentAnimation().getPlayMode() == Animation.PlayMode.NORMAL) {
                if (animationComponent.getCurrentAnimation().isAnimationFinished(animationComponent.getStateTime())) {
                    animationComponent.setAnimation(animationComponent.getPreviousAnimation());
                }
            }
        }
        spriteBatch.end();

        for (Entity entity : restOfList) {
            AnimationComponent animComp = entity.getComponent(AnimationComponent.class);
            animComp.setStateTime(animComp.getStateTime() + (delta * FRAME_SPEED));
            if (animComp.getCurrentAnimation().getPlayMode() == Animation.PlayMode.NORMAL) {
                if (animComp.getCurrentAnimation().isAnimationFinished(animComp.getStateTime())) {
                    animComp.setAnimation(animComp.getPreviousAnimation());
                }
            }
        }

        mapManager.renderWallTop();
    }

    public void updatePaused() {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();
        spriteBatch.begin();
        for (Entity entity : drawList) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            float width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            if (!(entity instanceof Beam)) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - width/2,
                        bodyComponent.getY() - bodyComponent.getHeight()/2,
                        width,
                        height);
            } else if (((Beam) entity).isOn()) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - bodyComponent.getWidth() / 2,
                        bodyComponent.getY() - bodyComponent.getHeight() / 2,
                        bodyComponent.getWidth(),
                        bodyComponent.getHeight());
            }
        }
        spriteBatch.end();
        mapManager.renderWallTop();
    }

    private ArrayList<Entity> sortDrawList() {
        float bleed = 64 * PixelDungeon.SCALAR;
        Polygon cameraBox = CollisionUtil.createRectangle(
                PlayScreen.gameCam.position.x,
                PlayScreen.gameCam.position.y,
                PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);

        ArrayList<Entity> restOfList = new ArrayList<>();
        ArrayList<Entity> alwaysBottom = new ArrayList<>();
        drawList.add(player);

        for (int i = 0 ; i < entities.size() ; i++) {
            Polygon entityBox = entities.get(i).getComponent(BodyComponent.class).getPolygon();
            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                if (entities.get(i).equals(player)) {
                    continue;
                }
                if (entities.get(i) instanceof PressurePlate) {
                    alwaysBottom.add(entities.get(i));
                } else if (entities.get(i) instanceof Beam){
                    float beamY = entities.get(i).getComponent(BodyComponent.class).getY();
                    float playerY = player.getComponent(BodyComponent.class).getY() - BeamSystem.yOffset;
                    if (beamY > playerY){
                        boolean added = false;
                        int j = drawList.indexOf(player);
                        while (j > 0){
                            if (beamY <= drawList.get(j).getComponent(BodyComponent.class).getY() - BeamSystem.yOffset){
                                drawList.add(j+1, entities.get(i));
                                added = true;
                                break;
                            } else{
                                j--;
                            }
                        }

                        if (!added){
                            drawList.add(0, entities.get(i));
                        }
                    } else {
                        boolean added = false;
                        int j = drawList.size()-1;
                        while (j > drawList.indexOf(player)){
                            if (beamY <= drawList.get(j).getComponent(BodyComponent.class).getY() - BeamSystem.yOffset){
                                drawList.add(j+1, entities.get(i));
                                added = true;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (!added){
                            drawList.add(j+1, entities.get(i));
                        }
                    }
                } else {
                    float entityY = entities.get(i).getComponent(BodyComponent.class).getY();
                    if (entityY > player.getComponent(BodyComponent.class).getY()) {
                        // FILTER LEFT SIDE OF ARRAY
                        boolean added = false;
                        int j = drawList.indexOf(player) - 1;
                        while (j > 0) {
                            if (entityY <= drawList.get(j).getComponent(BodyComponent.class).getY()) {
                                drawList.add(j+1,entities.get(i));
                                added = true;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (!added) {
                            drawList.add(0, entities.get(i));
                        }
                    } else {
                        // FILTER RIGHT SIDE OF ARRAY
                        boolean added = false;
                        int j = drawList.size()-1;
                        while (j > drawList.indexOf(player)) {
                            if (entityY <= drawList.get(j).getComponent(BodyComponent.class).getY()) {
                                drawList.add(j+1,entities.get(i));
                                added = true;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (!added) {
                            drawList.add(j+1, entities.get(i));
                        }
                    }
                }
            } else {
                restOfList.add(entities.get(i));
            }
        }
        for (Entity entity : alwaysBottom) {
            drawList.add(0,entity);
        }
        return restOfList;
    }
}