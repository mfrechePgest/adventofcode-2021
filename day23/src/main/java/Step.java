public enum Step {

    STEP_1(2), STEP_2(4);

    private final int chamberCapacity;

    Step(int chamberCapacity) {
        this.chamberCapacity = chamberCapacity;
    }

    public int getChamberCapacity() {
        return chamberCapacity;
    }
}
