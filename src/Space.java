public class Space{

    private int numBombsNear;
    private int x;
    private int y;
    private boolean chosen;



    public Space(int numBombsNear, int x, int y) {
        this.numBombsNear = numBombsNear;
        this.x = x;
        this.y = y;
        chosen = false;
    }

    public boolean getChosen(){
        return chosen;
    }

    public void setChosenTrue(){
        chosen = true;
    }



    public int getNumBombsNear() {
        return numBombsNear;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setNumBombsNear(int numBombsNear) {this.numBombsNear = numBombsNear;}

    @Override
    public String toString() {
        return "The space at " + x + ", " + y;
    }


}
