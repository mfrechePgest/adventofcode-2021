import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public final class CellDisplay {
    public static final float DISPLAY_SIZE = 0.9f;
    private final Cell dot;
    private final float largeurCase;
    private final float originX;
    private final float originY;

    public CellDisplay(Cell dot, float largeurCase) {
        this.dot = dot;
        this.largeurCase = largeurCase;
        originX = (dot.x() * largeurCase) - DISPLAY_SIZE;
        originY = ((dot.y() * largeurCase) - DISPLAY_SIZE) * -1;
    }

    public CellDisplay(Cell dot, float originX, float originY, float largeurCase) {
        this.dot = dot;
        this.largeurCase = largeurCase;
        this.originX = originX;
        this.originY = originY;
    }


    public void draw() {
        glBegin(GL_QUADS);
        {
            glVertex2f(originX, originY);
            glVertex2f(originX + largeurCase, originY);
            glVertex2f(originX + largeurCase, originY - largeurCase);
            glVertex2f(originX, originY - largeurCase);
        }
        glEnd();
    }

    public Cell getDot() {
        return dot;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public float getLargeurCase() {
        return largeurCase;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CellDisplay) obj;
        return Objects.equals(this.dot, that.dot) &&
                Float.floatToIntBits(this.largeurCase) == Float.floatToIntBits(that.largeurCase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dot, largeurCase);
    }

    @Override
    public String toString() {
        return "CellDisplay[" +
                "dot=" + dot + ", " +
                "largeurCase=" + largeurCase + ']';
    }


}
