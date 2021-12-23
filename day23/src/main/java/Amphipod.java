public enum Amphipod {

    A(1),
    B(10),
    C(100),
    D(1000);

    private int energyCost;

    private Amphipod(int energyCost) {
        this.energyCost = energyCost;
    }

    int energyCost(int steps) {
        return energyCost * steps;
    }

}
