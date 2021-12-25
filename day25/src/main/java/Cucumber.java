import java.util.List;

public enum Cucumber {
    SOUTH('v') {
        @Override
        public Coord findDestination(Coord origin, List<List<Cucumber>> grid) {
            int destination = origin.y() + 1;
            if (destination >= grid.size() ) {
                destination = 0;
            }
            return new Coord(origin.x(), destination);
        }
    }
    ,
    EAST('>') {
        @Override
        public Coord findDestination(Coord origin, List<List<Cucumber>> grid) {
            int destination = origin.x() + 1;
            if (destination >= grid.get(0).size() ) {
                destination = 0;
            }
            return new Coord(destination, origin.y());
        }
    };

    private final Character character;

    Cucumber(Character character) {
        this.character = character;
    }

    public static Cucumber fromChar(char charAt) {
        return switch (charAt) {
            case '>' -> EAST;
            case 'v' -> SOUTH;
            case '.' -> null;
            default -> throw new IllegalStateException();
        };
    }

    public char toChar() {
        return character;
    }

    public abstract Coord findDestination(Coord origin, List<List<Cucumber>> grid);
}
