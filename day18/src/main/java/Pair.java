import java.util.Objects;

public final class Pair extends Element {
    private Element x;
    private Element y;


    public Pair(int level, Pair parent) {
        super(level, parent);
    }

    public Pair(int level, Element x, Element y, Pair parent) {
        super(level, parent);
        this.x = x;
        this.y = y;
        this.propagateLevel(level);
    }

    public Element x() {
        return x;
    }

    public Element y() {
        return y;
    }

    public void setX(Element x) {
        this.x = x;
    }

    public void setY(Element y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pair) obj;
        return this.x == that.x &&
                this.y == that.y &&
                this.level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, level);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    @Override
    public int getMagnitude() {
        return 3 * x.getMagnitude() + 2 * y.getMagnitude();
    }

    @Override
    protected boolean split() {
        return x.split() || y.split();
    }

    @Override
    public boolean explode() {
        if (x instanceof SingleNumber && y instanceof SingleNumber && level >= 4) {
            parent.explode(this);
            return true;
        }
        boolean resultX = x.explode();
        if (!resultX) {
            return y.explode();
        }
        return true;
    }

    private void explode(Pair pair) {
        int explosionToLeft = ((SingleNumber) pair.x).getValue();
        int explosionToRight = ((SingleNumber) pair.y).getValue();
        if (x == pair) {
            x = new SingleNumber(pair.level, 0, this);
            y.addFromRightExplosion(explosionToRight);
            parent.propagateExplosionFromRight(this, explosionToLeft);
        } else if (y == pair) {
            y = new SingleNumber(pair.level, 0, this);
            x.addFromLeftExplosion(explosionToLeft);
            parent.propagateExplosionFromLeft(this, explosionToRight);
        }
    }

    private void propagateExplosionFromLeft(Pair pair, int val) {
        if (y == pair && parent != null) {
            parent.propagateExplosionFromLeft(this, val);
        } else {
            if (x == pair) {
                y.addFromRightExplosion(val);
            }
        }
    }

    private void propagateExplosionFromRight(Pair pair, int val) {
        if (x == pair && parent != null) {
            parent.propagateExplosionFromRight(this, val);
        } else {
            if (y == pair) {
                x.addFromLeftExplosion(val);
            }
        }
    }

    @Override
    protected void propagateLevel(int i) {
        if (x.level != i + 1 || x.parent != this) {
            x = x.duplicateWithNewLevel(i + 1, this);
        }
        if (y.level != i + 1 || x.parent != this) {
            y = y.duplicateWithNewLevel(i + 1, this);
        }
    }

    @Override
    protected void addFromLeftExplosion(int val) {
        y.addFromLeftExplosion(val);
    }

    @Override
    protected void addFromRightExplosion(int val) {
        x.addFromRightExplosion(val);
    }

    @Override
    protected Element duplicateWithNewLevel(int i, Pair parent) {
        Pair clone = new Pair(i, x, y, parent);
        x.propagateLevel(level + 1);
        y.propagateLevel(level + 1);
        return clone;
    }

    public void split(SingleNumber singleNumber) {
        double divideByTwo = singleNumber.getValue() / 2d;
        Pair replacement = new Pair(level + 1, this);
        replacement.setX(new SingleNumber(level + 2, (int) Math.floor(divideByTwo), replacement));
        replacement.setY(new SingleNumber(level + 2, (int) Math.ceil(divideByTwo), replacement));
        if (x == singleNumber) {
            x = replacement;
        } else {
            y = replacement;
        }

    }
}
