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
    public boolean isChosen(){
        return chosen;
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

    public void setChosenTrue(){
        chosen = true;
    }

    public void addNumBombsNear(){
        numBombsNear++;
    }

    public String toString(){
        return "x: " + x + " y: " + y;
    }

}
