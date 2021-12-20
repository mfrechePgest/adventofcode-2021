public record Point3d(int x, int y, int z) {

    public static Point3d fromString(String content) {
        String[] splitted = content.split(",");
        return new Point3d(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
    }

    public double dist2(Point3d otherPoint) {
        return Math.pow(x - otherPoint.x(), 2) +
                Math.pow(y - otherPoint.y(), 2) +
                Math.pow(z - otherPoint.z(), 2);
    }

    public double dist(Point3d otherPoint) {
        return Math.sqrt(dist2(otherPoint));
    }

    public String toString() {
        return x + "," + y + "," + z;
    }

    public Point3d add(Point3d toAdd) {
        return new Point3d(x + toAdd.x, y + toAdd.y, z + toAdd.z());
    }

    public Point3d diff(Point3d toSub) {
        return new Point3d(x - toSub.x(), y - toSub.y(), z - toSub.z());
    }


}
