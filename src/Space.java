public class Space {

    private int numBombsNear;
    private boolean safe;
    private int x;
    private int y;

    public Space(int numBombsNear, boolean safe, int x, int y) {
        this.numBombsNear = numBombsNear;
        this.safe = safe;
        this.x = x;
        this.y = y;
    }

    public boolean isSafe() {
        return safe;
    }

    public int getNumBombsNear() {
        return numBombsNear;
    }

    public void setNumBombsNear(int numBombsNear) {this.numBombsNear = numBombsNear;}

    @Override
    public String toString() {
        return "The space at " + x + ", " + y + "is " + safe;
    }


}
