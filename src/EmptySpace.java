public class EmptySpace extends Space {

    public EmptySpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x, y);
    }

    @Override
    public String toString(){
        if (isChosen()) {
            return "" + getNumBombsNear();
        } else {return "_";}


    }

}
