public class BombSpace extends Space{

    private boolean safe;

    public BombSpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x, y);
        this.safe = false;
    }

    public boolean isSafe() {
        return safe;
    }


}
