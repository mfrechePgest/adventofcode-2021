public record Cell(int x, int y) {

    public Cell getFuturePositionAfterFolding(FoldingInstruction instruction) {
        return new Cell(
                instruction.isVertical() ? x - (2 * (x - instruction.coord()))
                        : x,
                instruction.isVertical() ? y
                        : y - (2 * (y - instruction.coord()))
        );
    }

    public boolean shouldMoveDuringFolding(FoldingInstruction instruction) {
        return instruction.isVertical() ? x > instruction.coord() : y > instruction.coord();
    }

}
