import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class EnlightenRegion {
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;
    private final int minZ;
    private final int maxZ;
    private final boolean on;

    private final List<EnlightenRegion> minusRegion = new ArrayList<>();

    public EnlightenRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, boolean on) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.on = on;
    }

    public void turnOffLightsInRegion(EnlightenRegion region) {
        minusRegion.add(region);
    }

    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ &&
                minusRegion.stream().noneMatch(subRegion -> subRegion.contains(x,y,z));
    }


    public int minX() {
        return minX;
    }

    public int maxX() {
        return maxX;
    }

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    public int minZ() {
        return minZ;
    }

    public int maxZ() {
        return maxZ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EnlightenRegion) obj;
        return this.minX == that.minX &&
                this.maxX == that.maxX &&
                this.minY == that.minY &&
                this.maxY == that.maxY &&
                this.minZ == that.minZ &&
                this.maxZ == that.maxZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, maxX, minY, maxY, minZ, maxZ);
    }

    @Override
    public String toString() {
        return "EnlightenRegion[" +
                "minX=" + minX + ", " +
                "maxX=" + maxX + ", " +
                "minY=" + minY + ", " +
                "maxY=" + maxY + ", " +
                "minZ=" + minZ + ", " +
                "maxZ=" + maxZ + ']';
    }


    public long countCubesOn() {
        return countCubesOn(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public long countCubesOn(int subMinX, int subMaxX, int subMinY, int subMaxY, int subMinZ, int subMaxZ) {
        int effectiveMinX = Math.max(minX, subMinX);
        int effectiveMaxX = Math.min(maxX, subMaxX);

        int effectiveMinY = Math.max(minY, subMinY);
        int effectiveMaxY = Math.min(maxY, subMaxY);

        int effectiveMinZ = Math.max(minZ, subMinZ);
        int effectiveMaxZ = Math.min(maxZ, subMaxZ);

        if (effectiveMaxZ < effectiveMinZ || effectiveMaxY < effectiveMinY || effectiveMaxX < effectiveMinX ) {
            return 0;
        }

        long size = (long) (1 + effectiveMaxX - effectiveMinX) * (1 + effectiveMaxY - effectiveMinY) * (1 + effectiveMaxZ - effectiveMinZ);
        if ( size == 0 ) {
            return size;
        } else {
            long sum = minusRegion.stream()
                    .mapToLong(enlightenRegion -> enlightenRegion.countCubesOn(effectiveMinX, effectiveMaxX, effectiveMinY, effectiveMaxY, effectiveMinZ, effectiveMaxZ))
                    .sum();
            long result = size - sum;
            return result;
        }
    }

    public boolean isOn() {
        return on;
    }
}
