import java.awt.*;

public class FlaggedSpace extends BombSpace{

    public FlaggedSpace(int numBombsNear, int x, int y) {
        super(numBombsNear,x,y);
    }

    public String toString() {
        return "F";
    }
}
