public record CellAnimation (CellDisplay origin, CellDisplay destination) {

    CellDisplay getCurrentCellDisplay(float timeElapsedProportion) {
        float stepX = (destination.getOriginX() - origin.getOriginX()) * timeElapsedProportion;
        float stepY = (destination.getOriginY() - origin.getOriginY()) * timeElapsedProportion;
        return new CellDisplay(
                origin.getDot(),
                origin.getOriginX() + stepX,
                origin.getOriginY() + stepY,
                origin.getLargeurCase()
        );
    }


}
