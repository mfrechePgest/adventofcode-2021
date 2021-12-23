public record CellAnimation(CellDisplay origin, CellDisplay destination) {

    CellDisplay getCurrentCellDisplay(float timeElapsedProportion) {
        float stepX;// = (destination.getOriginX() - origin.getOriginX()) * timeElapsedProportion;
        float stepY;// = (destination.getOriginY() - origin.getOriginY()) * timeElapsedProportion;

        if (origin.getChamber() != null) {
            if (timeElapsedProportion >= 0.5f) {
                stepX = (destination.getOriginX() - origin.getOriginX()) * Math.min(1f,((timeElapsedProportion - 0.5f) * 2f));
            } else {
                stepX = 0;
            }
            stepY = (destination.getOriginY() - origin.getOriginY()) * Math.min(1f,timeElapsedProportion * 2f);
        } else {
            if (timeElapsedProportion >= 0.5f) {
                stepY = (destination.getOriginY() - origin.getOriginY()) * Math.min(1f,((timeElapsedProportion - 0.5f) * 2f));
            } else {
                stepY = 0;
            }
            stepX = (destination.getOriginX() - origin.getOriginX()) * Math.min(1f,timeElapsedProportion * 2f);
        }

        return new CellDisplay(
                origin.getAmphipod(),
                origin.getChamber(),
                origin.getLargeurCase(),
                origin.getOriginX() + stepX,
                origin.getOriginY() + stepY
        );
    }


}
