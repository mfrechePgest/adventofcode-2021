public enum Amphipod {

    A(1, 0),
    B(10, 1),
    C(100, 2),
    D(1000, 3);

    private int energyCost;
    private int targetChamber;

    private Amphipod(int energyCost, int targetChamber) {
        this.energyCost = energyCost; this.targetChamber = targetChamber;
    }

    int energyCost(int steps) {
        return energyCost * steps;
    }

    int getTargetChamber() {
        return targetChamber;
    }
}
