public class BombSpace extends Space{

    private boolean safe;

    public BombSpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x, y);
        this.safe = false;
    }


    public String toString(){
        return "B";
    }

    public void setToBomb(){

    }

    public boolean isSafe() {
        return safe;
    }


}
