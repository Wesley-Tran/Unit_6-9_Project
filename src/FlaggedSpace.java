import java.awt.*;

public class FlaggedSpace extends Space{

    public FlaggedSpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x,y);
    }

    public String toString() {return "" + Color.RED;}
}
