import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    // to fill bombs; go through 2D array with a chance to set bomb to every space -> if row has bombs / lower chance of bomb
    // if not enough bombs, traverse again (backwards or forwards)

    private Space[][] grid;
    private Space[][] displayGrid;
    private static int numBombs;
    private double bombChance; //0.00 - 1.00

    //no constructor

    public void play() {
        System.out.println("Welcome to Bootleg Minesweeper");
        System.out.println("What difficulty do you want\n> "); //medium 11x11 //stay on odd numbers

        numBombs = 10; //diffucult
        grid = new Space[11][11]; //diff
        displayGrid = new Space[11][11]; //diff

        setBombs();
        boolean won = false;
        while (!won) {
            printGrid();
            System.out.println("\n\nSelect a space");
            System.out.print("Enter the x and y coordinate with only a space in between: ");

            Scanner scan = new Scanner(System.in);

            String temp = scan.nextLine();
            while (true) {
                try { // https://stackoverflow.com/questions/19925047/how-to-check-the-input-is-an-integer-or-not-in-java
                    int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
                    int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
                    break;
                } catch (NumberFormatException e) { //crashes when out of bounds - > not space
                    System.out.println("\n\nEnter a valid coordinate");
                    System.out.print("Enter the x and y coordinate with only a space in between: ");
                    temp = scan.nextLine();
                }
            }

        }


    }

    public void printGrid(){
        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j<grid[0].length; j++){
                if(grid[i][j] instanceof BombSpace){
                    System.out.print("B");
                }else{
                    System.out.print("E ");
                }
            }
            System.out.println();
        }
    }



    private void setBombs() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (Math.random() < bombChance) {
                    grid[i][j] = new BombSpace(0,i,j);
                    bombChance -= 0.05;
                } else {
                    grid[i][j] = new EmptySpace(0,i,j);
                }
            }
        }
        for (Space[] spaces : grid) {
            for (Space space : spaces) {
                ArrayList<Space> neighborSpaces = new ArrayList<>();
                checkNeighbors(space, neighborSpaces);
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

    public ArrayList<Space> checkEmptyNeighbors(Space space){
        int currentX = space.getX();
        int currentY = space.getY();
        ArrayList<Space> list = new ArrayList<Space>();

        if (isValidCoord(currentX-1,currentY)) { //left
            if(grid[currentX-1][currentY] instanceof EmptySpace){
                list.add(grid[currentX-1][currentY]);
            }
        }
        if (isValidCoord(currentX-1,currentY+1)) { //top left
            if(grid[currentX-1][currentY+1] instanceof EmptySpace){
                list.add(grid[currentX-1][currentY+1]);
            }
        }
        if (isValidCoord(currentX,currentY+1)) { //top
            if(grid[currentX][currentY+1] instanceof EmptySpace){
                list.add(grid[currentX][currentY+1]);
            }
        }
        if (isValidCoord(currentX+1,currentY+1)) { //top right
            if(grid[currentX+1][currentY+1] instanceof EmptySpace){
                list.add(grid[currentX+1][currentY+1]);
            }
        }
        if (isValidCoord(currentX+1,currentY)) { //right
            if(grid[currentX+1][currentY] instanceof EmptySpace){
                list.add(grid[currentX+1][currentY]);
            }
        }
        if (isValidCoord(currentX-1,currentY+1)) { //bottom right
            if(grid[currentX-1][currentY+1] instanceof EmptySpace){
                list.add(grid[currentX+1][currentY-1]);
            }
        }
        if (isValidCoord(currentX,currentY+1)) { //bottom
            if(grid[currentX][currentY-1] instanceof EmptySpace){
                list.add(grid[currentX][currentY-1]);
            }
        }
        if (isValidCoord(currentX-1,currentY+1)) { //bottom left
            if(grid[currentX-1][currentY-1]instanceof EmptySpace){
                list.add(grid[currentX-1][currentY-1]);
            }
        }
        return list;
    }

    private void openSpace(Space space){
            if(space.getNumBombsNear()==0){
                //checkNeighbors(space,);

            }
        }
    }


