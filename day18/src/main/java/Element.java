public sealed abstract class Element permits Pair, SingleNumber {

    protected final int level;
    protected final Pair parent;

    Element(int level, Pair parent) {
        this.level = level;
        this.parent = parent;
    }

    public abstract int getMagnitude();

    public static Element parseContent(String content, int level, Pair parent) {
        Element element;
        if (content.startsWith("[")) {
            String subContent = content.substring(1, content.length() - 1);
            int cutIndex = 0;
            if (subContent.startsWith("[")) {
                int openBracesCount = 0;
                for (int i = 0; i < subContent.length(); i++) {
                    if (openBracesCount == 0 && subContent.charAt(i) == ',') {
                        cutIndex = i;
                        break;
                    } else if (subContent.charAt(i) == '[') {
                        openBracesCount++;
                    } else if (subContent.charAt(i) == ']') {
                        openBracesCount--;
                    }
                }
            } else {
                cutIndex = subContent.indexOf(',');
            }
            String[] cut = cut(subContent, cutIndex);
            Pair pair = new Pair(level, parent);
            pair.setX(parseContent(cut[0], level + 1, pair));
            pair.setY(parseContent(cut[1], level + 1, pair));
            element = pair;
        } else {
            element = new SingleNumber(level, Character.getNumericValue(content.charAt(0)), parent);
        }
        return element;
    }

    private static String[] cut(String s, int pos) {
        return new String[]{s.substring(0, pos), s.substring(pos + 1)};
    }

    public Element add(Element e) {
        return new Pair(level, this, e, null);
    }

    public boolean reduce() {
        if (!this.explode()) {
            return this.split();
        }
        return true;
    }

    public void reduceUntilFinished() {
        boolean reduced = false;
        while (!reduced) {
            reduced = !this.reduce();
        }
    }

    protected abstract boolean split();

    protected abstract boolean explode();

    protected abstract void propagateLevel(int i);

    protected abstract void addFromLeftExplosion(int val);

    protected abstract void addFromRightExplosion(int val);

    protected abstract Element duplicateWithNewLevel(int i, Pair parent);
}
