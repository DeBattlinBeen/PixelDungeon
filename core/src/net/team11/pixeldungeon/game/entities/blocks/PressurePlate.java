package net.team11.pixeldungeon.game.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.TrapComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

/**
 * Class to handle pressure plates in game
 * When making in Tiled set the autoClose boolean to true for entity to re-activate after a
 * specified amount of time, activeTime.
 */
public class PressurePlate extends Trap {
    private boolean activated;
    private boolean autoClose;

    public PressurePlate(Rectangle bounds, String name, float activeTime, boolean autoClose) {
        super(name, true);
        this.activated = false;
        this.autoClose = autoClose;
        enabled = true;
        timed = true;
        timerReset = activeTime;
        timer = 0;
        triggered = false;

        float posX = bounds.getX() + bounds.getWidth() / 2;
        float posY = bounds.getY() + bounds.getHeight() / 2;
        this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.PRESSUREPLATE_DOWN,textureAtlas,1, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PRESSUREPLATE_UP,textureAtlas,1, Animation.PlayMode.LOOP);
        if (triggered) {
            animationComponent.setAnimation(AssetName.PRESSUREPLATE_DOWN);
        } else {
            animationComponent.setAnimation(AssetName.PRESSUREPLATE_UP);
        }
    }

    @Override
    public void setTimer(float timer) {
        if (contactEntity == null && !activated) {
            if (triggered) {
                super.setTimer(timer);
                if (timer <= 0) {
                    callTargets();
                    triggered = false;
                }
            }
        }
    }

    @Override
    public void trigger() {
        if (contactEntity == null && activated) {
            if (autoClose) {
                activated = false;
                resetTimer();
                getComponent(AnimationComponent.class).setAnimation(AssetName.PRESSUREPLATE_UP);
            } else {
                getComponent(AnimationComponent.class).setAnimation(AssetName.PRESSUREPLATE_UP);
            }
        } else if (contactEntity != null && !activated) {
            activated = true;
            getComponent(AnimationComponent.class).setAnimation(AssetName.PRESSUREPLATE_DOWN);
            if (!triggered) {
                triggered = true;
                callTargets();
            }
        }
    }

    private void callTargets() {
        for (Entity entity : targetEntities) {
            entity.doInteraction(false);
        }
    }

    @Override
    public void setContactingEntity(Entity entity) {
        super.setContactingEntity(entity);
        if (contactEntity != null) {
            trigger();
        } else {
            trigger();
        }
    }
}