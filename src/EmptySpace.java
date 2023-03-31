import java.awt.*;

public class EmptySpace extends Space {
    private boolean safe;

    public EmptySpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x, y);
        this.safe = true;
    }

    public String toString(){
        if (getChosen()) {
            return "" + getNumBombsNear();
        }
        return "_";
    }

    public boolean isSafe() {
        return safe;
    }
}
