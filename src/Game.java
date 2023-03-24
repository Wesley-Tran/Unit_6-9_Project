import java.util.ArrayList;

public class Game {
    // to fill bombs; go through 2D array with a chance to set bomb to every space -> if row has bombs / lower chance of bomb
    // if not enough bombs, traverse again (backwards or forwards)

    private Space[][] grid;
    private static int numBombs;
    private double bombChance; //0.00 - 1.00

    //no constructor

    public void play() {
        System.out.println("Welcome to Bootleg Minesweeper");
        System.out.println("What difficulty do you want\n> "); //medium 10x10

        grid = new Space[10][10];

        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){

            }
            System.out.println();
        }


    }




    public Space[][] getGrid(){
        return grid;
    }

    private void setBombs() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (Math.random() < bombChance) {
                    grid[i][j] = new BombSpace(0,i,j);
                } else {
                    grid[i][j] = new EmptySpace(0,i,j);
                }
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                ArrayList<Space> neighborSpaces = new ArrayList<>();
                checkNeighbors(grid[i][j],neighborSpaces);
            }
        }
    }

    private boolean isValidCoord(int x, int y) {
        if (y > -1 && y < grid.length) {
            return x > -1 && x < grid[0].length;
        }
        return false;
    }

    private void checkNeighbors(Space space, ArrayList<Space> list) {
        int currentX = space.getX();
        int currentY = space.getY();

        if (isValidCoord(currentX-1,currentY)) { //left
            list.add(grid[currentX-1][currentY]);
        }
        if (isValidCoord(currentX-1,currentY+1)) { //top left
            list.add(grid[currentX-1][currentY+1]);
        }
        if (isValidCoord(currentX,currentY+1)) { //top
            list.add(grid[currentX][currentY+1]);
        }
        if (isValidCoord(currentX+1,currentY+1)) { //top right
            list.add(grid[currentX+1][currentY+1]);
        }
        if (isValidCoord(currentX+1,currentY)) { //right
            list.add(grid[currentX+1][currentY]);
        }
        if (isValidCoord(currentX+1,currentY+1)) { //bottom right
            list.add(grid[currentX+1][currentY+1]);
        }
        if (isValidCoord(currentX,currentY+1)) { //bottom
            list.add(grid[currentX][currentY+1]);
        }
        if (isValidCoord(currentX-1,currentY+1)) { //bottom left
            list.add(grid[currentX-1][currentY+1]);
        }
        space.setNumBombsNear(list.size());
    }

}
