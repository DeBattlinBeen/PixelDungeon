package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorFrameComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

public class DoorFrame extends Entity {
    private Rectangle bounds;

    public DoorFrame(Rectangle rectangle, String name, String animation) {
        super(name);
        this.bounds = new Rectangle(rectangle);

        AnimationComponent animationComponent;
        PositionComponent positionComponent;
        this.addComponent(new DoorFrameComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight()));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(positionComponent = new PositionComponent());

        setupAnimations(animationComponent, animation);
        setupPosition(positionComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent, String animation) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(animation, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(animation);
        System.err.println("Setup Animations " + animation + "for " + name);
    }

    private void setupPosition(PositionComponent positionComponent) {
        positionComponent.setX(bounds.getX());
        positionComponent.setY(bounds.getY());
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
