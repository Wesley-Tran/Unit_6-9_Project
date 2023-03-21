public class EmptySpace extends Space {
    private boolean safe;

    public EmptySpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x, y);
        this.safe = true;
    }

    public boolean isSafe() {
        return safe;
    }
}
