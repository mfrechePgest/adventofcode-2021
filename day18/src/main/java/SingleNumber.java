import java.util.Objects;

public final class SingleNumber extends Element {
    private int value;

    public SingleNumber(int level, int value, Pair parent) {
        super(level, parent);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleNumber that = (SingleNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int getMagnitude() {
        return value;
    }

    @Override
    public boolean split() {
        if (value >= 10) {
            parent.split(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean explode() {
        return false;
    }

    @Override
    protected void propagateLevel(int i) {
    }

    @Override
    protected void addFromLeftExplosion(int val) {
        value += val;
    }

    @Override
    protected void addFromRightExplosion(int val) {
        value += val;
    }

    @Override
    protected Element duplicateWithNewLevel(int i, Pair parent) {
        return new SingleNumber(i, value, parent);
    }


}
