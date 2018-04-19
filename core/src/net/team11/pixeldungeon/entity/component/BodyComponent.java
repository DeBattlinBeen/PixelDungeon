package net.team11.pixeldungeon.entity.component;

import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class BodyComponent implements EntityComponent {

    private float width;
    private float height;

    public BodyComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
