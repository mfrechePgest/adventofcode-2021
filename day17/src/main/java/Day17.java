public class Day17 {

    private final TargetArea targetArea;

    public static void main(String[] args) {
        Day17 day17 = new Day17("target area: x=277..318, y=-92..-53");

        System.out.println("day17.findBestThrow() = " + day17.findBestThrow());
        System.out.println("count throw in target = " + day17.findEveryThrowInTarget());
    }

    public Day17(String content) {
        String[] splitted = content.split(", ");
        String[] xArea = splitted[0].substring("target area: x=".length()).split("\\.\\.");
        String[] yArea = splitted[1].substring(2).split("\\.\\.");
        targetArea = new TargetArea(Integer.parseInt(xArea[0]), Integer.parseInt(xArea[1]),
                Integer.parseInt(yArea[0]), Integer.parseInt(yArea[1])
        );
    }

    public Integer findBestThrow() {
        int highestY = 0;
        Integer bestXVelocity = null;
        Integer bestYVelocity = null;
        for (int i = 0 ; i < 500 ; i++ ) { // TODO un max moins pifé ?
            for (int j = 0 ; j < 500 ; j++ ) {
                Integer maxY = simulateThrow(i, j);
                if ( maxY != null && maxY > highestY ) {
                    highestY = maxY;
                    bestXVelocity = i;
                    bestYVelocity = j;
                }
            }
        }
        System.out.println("Meilleur lancé =  " + bestXVelocity + ";" + bestYVelocity + "  = " +highestY);
        return highestY;
    }

    public Integer findEveryThrowInTarget() {
        int count = 0;
        for (int i = -500 ; i < 500 ; i++ ) { // TODO range moins pifé ?
            for (int j = -500 ; j < 500 ; j++ ) {
                Integer maxY = simulateThrow(i, j);
                if ( maxY != null ) {
                    System.out.println("Lancé valide : " + i +","+j);
                    count++;
                }
            }
        }
        return count;
    }

    public Integer simulateThrow(int xVelocity, int yVelocity) {
        Position probePosition = new Position(0, 0);
        int highestY = 0;
        for (int i = 0; i < 1000; i++) { // TODO condition vélocité m'emmène tjrs dans la bonne direction
            probePosition = probePosition.move(xVelocity, yVelocity);
            if (probePosition.y() > highestY ) {
                highestY = probePosition.y();
            }
            xVelocity += xVelocity > 0 ? -1 : 1;
            yVelocity -= 1;
            if (probePosition.isInTarget(targetArea)) {
                return highestY;
            }
        }
        return null;
    }

    public record TargetArea(int minX, int maxX, int minY, int maxY) {
    }

    public record Position(int x, int y) {
        public Position move(int xVelocity, int yVelocity) {
            return new Position(x+xVelocity, y+yVelocity);
        }

        public boolean isInTarget(TargetArea targetArea) {
            return x >= targetArea.minX() &&
                    x <= targetArea.maxX() &&
                    y >= targetArea.minY() &&
                    y <= targetArea.maxY();
        }
    }


}
