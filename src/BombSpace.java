public class BombSpace extends Space{


    public BombSpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x, y);
    }


    public String toString(){
        if (isChosen()) {
            return "B";
        } else {return "_";}
    }

    public void setToBomb(){

    }


}
