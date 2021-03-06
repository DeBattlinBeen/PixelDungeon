package net.team11.pixeldungeon.game.entities.beams;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.system.BeamSystem;
import net.team11.pixeldungeon.game.entitysystem.Entity;

import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;


public class Beam extends Trap {
    private BeamGenerator generator;
    private Direction beamDirection;

    private final int DAMAGE = 100;
    private float originX;
    private float originY;

    public static final int DEPTH = 4;
    public static final int WIDTH = 4;
    private Entity currentClosest = null;

    private boolean on;

    public Beam(Rectangle bounds, String name, boolean enabled, String direction, boolean on){
        super(name, enabled);
        this.enabled = true;
        this.on = on;
        this.beamDirection = Direction.parseInput(direction);

        this.damage = DAMAGE;

        originX = bounds.getX() + bounds.getWidth()/2;
        originY = bounds.getY() + bounds.getHeight()/2;

        this.addComponent(new BodyComponent(WIDTH, DEPTH, originX, originY, 0,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);

    }

    public Entity getCurrentClosest() {
        return currentClosest;
    }

    public Direction getBeamDirection() {
        return beamDirection;
    }

    public void setBeamDirection(Direction beamDirection) {
        this.beamDirection = beamDirection;
    }

    public boolean hasCurrentClosest() {
        return currentClosest!=null;
    }

    public void setCurrentClosest(Entity currentClosest) {
        this.currentClosest = currentClosest;
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.BEAM_VERTICAL, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BEAM_HORIZONTAL, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        if (beamDirection == Direction.RIGHT || beamDirection == Direction.LEFT) {
            animationComponent.setAnimation(AssetName.BEAM_HORIZONTAL);
        } else {
            animationComponent.setAnimation(AssetName.BEAM_VERTICAL);
        }
    }

    public boolean isOn() {
        return this.on;
    }

    public void setOn(boolean on){
        this.on = on;
        BodyComponent body = getComponent(BodyComponent.class);
        body.setWidth(WIDTH);
        body.setHeight(DEPTH);

        AnimationComponent animationComponent = getComponent(AnimationComponent.class);
        if (beamDirection == Direction.RIGHT || beamDirection == Direction.LEFT) {
            animationComponent.setAnimation(AssetName.BEAM_HORIZONTAL);
        } else {
            animationComponent.setAnimation(AssetName.BEAM_VERTICAL);
        }
    }

    @Override
    public String toString(){
        return this.getName() + " --> " + this.getBeamDirection()
                + " : " + getComponent(BodyComponent.class).getWidth()
                + "," + getComponent(BodyComponent.class).getHeight();
    }

    public void setParent(BeamGenerator generator) {
        this.generator = generator;
    }
}
