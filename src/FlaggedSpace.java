import java.awt.*;

public class FlaggedSpace extends Space {

    private Space space;

    public FlaggedSpace(int numBombsNear, int x, int y, Space space) {
        super(numBombsNear,x,y);
        this.space = space;
    }

    public Space getSpace() {
        return space;
    }

    @Override
    public String toString() {
        return "F";
    }
}
