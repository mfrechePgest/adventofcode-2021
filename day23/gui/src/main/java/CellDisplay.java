import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public final class CellDisplay {
    public static final float DISPLAY_SIZE = 0.9f;
    public static final float MARGE = 0.1f;
    private final Amphipod amphipod;
    private final float largeurCase;
    private final float originX;
    private final float originY;
    private final Chamber chamber;
    private final Integer parkingId;
    private final Integer idxInChamber;

    public CellDisplay(Amphipod dot, Chamber chamber, int idxInChamber, float largeurCase) {
        this.amphipod = dot;
        this.chamber = chamber;
        this.idxInChamber = idxInChamber;
        Coord coord = getChamberCoord(chamber, idxInChamber);
        originX = ((coord.x() * largeurCase) - DISPLAY_SIZE) + MARGE * largeurCase;
        originY = (((coord.y() * largeurCase) - DISPLAY_SIZE) * -1) + MARGE * largeurCase;
        this.largeurCase = largeurCase - ((MARGE * largeurCase) * 2);
        parkingId = null;
    }

    public CellDisplay(Amphipod dot, Chamber chamber, float largeurCase, float originX, float originY) {
        this.amphipod = dot;
        this.largeurCase = largeurCase;
        this.chamber = chamber;
        this.idxInChamber = null;
        this.originX = originX;
        this.originY = originY;
        parkingId = null;
    }

    public CellDisplay(Amphipod amphipod, Parkings parkings, int parkingId, float largeurCase) {
        this.amphipod = amphipod;
        this.chamber = null;
        this.parkingId = parkingId;
        this.idxInChamber = null;
        Coord coord = getParkingCoord(parkingId);
        originX = ((coord.x() * largeurCase) - DISPLAY_SIZE) + MARGE * largeurCase;
        originY = (((coord.y() * largeurCase) - DISPLAY_SIZE) * -1) + MARGE * largeurCase;
        this.largeurCase = largeurCase - ((MARGE * largeurCase) * 2);
    }

    public CellDisplay(int x, int y, float largeurCase) {
        this.amphipod = null;
        this.chamber = null;
        this.idxInChamber = null;
        this.parkingId = null;
        originX = ((x * largeurCase) - DISPLAY_SIZE) + MARGE * largeurCase;
        originY = (((y * largeurCase) - DISPLAY_SIZE) * -1) + MARGE * largeurCase;
        this.largeurCase = largeurCase - ((MARGE * largeurCase) * 2);
    }


    public static CellDisplay fromMovement(CellDisplay pod, Movement nextMovement, Situation currentSituation, float largeurCase) {
        if (nextMovement.type() == MovementType.CHAMBER_TO_PARKING) {
            return new CellDisplay(pod.getAmphipod(), currentSituation.parkings(), nextMovement.destination(), largeurCase);
        } else {
            return new CellDisplay(pod.getAmphipod(),
                    currentSituation.lstChambers().get(nextMovement.destination()),
                    currentSituation.lstChambers().get(nextMovement.destination()).size(),
                    largeurCase);
        }
    }


    public Coord getChamberCoord(Chamber chamber, int idxInChamber) {
        return new Coord(
                3 + (chamber.getIdx() * 2),
                1 + chamber.getFullCapacity() - idxInChamber
        );
    }

    private Coord getParkingCoord(int parkingId) {
        int y = 1;
        int x;
        if (parkingId <= 1) {
            x = parkingId + 1;
        } else if (parkingId <= 5) {
            x = parkingId * 2;
        } else {
            x = 11;
        }
        return new Coord(x,y);

    }


    public void draw() {
        color();
        glBegin(GL_QUADS);
        {
            glVertex2f(originX, originY);
            glVertex2f(originX + largeurCase, originY);
            glVertex2f(originX + largeurCase, originY - largeurCase);
            glVertex2f(originX, originY - largeurCase);
        }
        glEnd();
    }

    private void color() {
        if (amphipod != null ) {
            switch (amphipod) {
                case A -> glColor3f(1f, 0f, 0f);
                case B -> glColor3f(0f, 1f, 0f);
                case C -> glColor3f(0f, 0f, 1f);
                case D -> glColor3f(1f, 1f, 0f);
            }
        } else {
            glColor3f(1f, 1f, 1f);
        }
    }

    public Amphipod getAmphipod() {
        return amphipod;
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
        return Objects.equals(this.amphipod, that.amphipod) &&
                Float.floatToIntBits(this.largeurCase) == Float.floatToIntBits(that.largeurCase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amphipod, largeurCase);
    }

    @Override
    public String toString() {
        return "CellDisplay[" +
                "pod=" + amphipod + ", " +
                "largeurCase=" + largeurCase + ']';
    }


    public Chamber getChamber() {
        return chamber;
    }

    public boolean shouldMove(Movement nextMovement) {
        if (nextMovement.type() == MovementType.CHAMBER_TO_PARKING) {
            return chamber != null && chamber.getIdx() == nextMovement.origin() && idxInChamber == chamber.size() - 1;
        } else {
            return parkingId != null && parkingId == nextMovement.origin();
        }
    }
}
