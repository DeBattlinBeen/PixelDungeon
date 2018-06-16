package net.team11.pixeldungeon.utils;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CollisionUtil {
    public static final Byte BOUNDARY       = 0x0001;
    public static final Byte ENTITY         = 0x0002;
    public static final Byte TRAP           = 0x0004;
    public static final Byte PUZZLE_AREA    = 0x0008;

    public static boolean isOverlapping(Polygon polygon, Polygon rectangle) {
        float[] vertices = rectangle.getVertices();
        for (int i = 0 ; i < vertices.length/2 ; i++) {
            Vector2 vector2 = new Vector2(vertices[i*2], vertices[i * 2 + 1]);
            if (polygon.contains(vector2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSubmerged(Polygon polygon, Polygon rectangle) {
        ArrayList<Boolean> booleans = new ArrayList<>();
        float[] vertices = rectangle.getVertices();
        for (int i = 0 ; i < vertices.length/2 ; i++) {
            Vector2 vector2 = new Vector2(vertices[i*2], vertices[i * 2 + 1]);
            if (polygon.contains(vector2)) {
                booleans.add(true);
            }
        }
        return booleans.size() == vertices.length/2;
    }

    public static Polygon createRectangle(float x, float y, float width, float height) {
        return new Polygon(new float[] {
                x - width/2, y - height/2,
                x          , y - height/2,
                x + width/2, y - height/2,
                x + width/2, y,
                x + width/2, y + height/2,
                x          , y + height/2,
                x - width/2, y + height/2,
                x - width/2, y
        });
    }
}
