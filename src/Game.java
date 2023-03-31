import java.awt.*;
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

        numBombs = 50; //diffuculty
        grid = new Space[11][11]; //diff
        displayGrid = new Space[11][11]; //diff
        //intialize displayGrid
        for (int i = 0; i < displayGrid.length; i++) {
            for (int j = 0; j < displayGrid[i].length;j++) {
                displayGrid[i][j] = new EmptySpace(0,i,j);
            }
        }
        bombChance = 0.35; //default value diff

        setBombs();
        boolean won = false;
        while (!won) {
            testPrintGrid();
            System.out.println();
            printGrid();
            System.out.println("\n\nSelect a space");
            System.out.print("Enter the x and y coordinate with only a space in between: ");

            Scanner scan = new Scanner(System.in);

            String temp = scan.nextLine();
            int x;
            int y;
            while (true) { //to get the user input
                try { // https://stackoverflow.com/questions/19925047/how-to-check-the-input-is-an-integer-or-not-in-java
                    x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
                    y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
                    break;
                } catch (NumberFormatException e) { //crashes when out of bounds - > not space
                    System.out.println("\n\nEnter a valid coordinate");
                    System.out.print("Enter the x and y coordinate with only a space in between (x y): ");
                    temp = scan.nextLine();
                }
            }
            
            openSpace(displayGrid[x][y]); //open the space
            //if bomb end the game
                //go through the grid and any space that is a bomb, set displaygrid to it
            //check the surroudings and open any spaces that have 0 bombs


        }


    }

    public void printGrid(){ //for user view
        System.out.print("   ");
        for(int k = 1; k<displayGrid.length+1; k++){
            if(k<10){
                System.out.print(k + " ");
            }else{
                System.out.print(k);
            }

        }
        System.out.println();
        for(int i = 0; i < displayGrid.length; i ++){
            if(i<9){
                System.out.print(i+1 + "  ");
            }else{
                System.out.print(i+1 + " ");
            }

            for(int j = 0; j< displayGrid[0].length; j++){
                System.out.print(displayGrid[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void testPrintGrid(){ //for developer view
        System.out.print("   ");
        for(int k = 1; k<grid.length+1; k++){
            if(k<10){
                System.out.print(k + " ");
            }else{
                System.out.print(k);
            }

        }
        System.out.println();
        for(int i = 0; i < grid.length; i ++){
            if(i<9){
                System.out.print(i+1 + "  ");
            }else{
                System.out.print(i+1 + " ");
            }

            for(int j = 0; j< grid[0].length; j++){
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }



    private void setBombs() {
        while (numBombs > 0) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (Math.random() < bombChance && numBombs > 0) {
                        grid[i][j] = new BombSpace(0, i, j);
                        bombChance -= 0.05;
                        numBombs--;
                    } else if (grid[i][j] == null){
                        grid[i][j] = new EmptySpace(0, i, j);
                    }
                }
                bombChance = 0.35;
            }

        }
        System.out.println(numBombs);
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
        if (isValidCoord(currentX+1,currentY-1)) { //bottom right
            list.add(grid[currentX+1][currentY-1]);
        }
        if (isValidCoord(currentX,currentY-1)) { //bottom
            list.add(grid[currentX][currentY-1]);
        }
        if (isValidCoord(currentX-1,currentY-1)) { //bottom left
            list.add(grid[currentX-1][currentY-1]);
        }
        int count = 0;
        for (Space value : list) {
            if (value instanceof BombSpace) {
                count++;
            }
        }
        space.setNumBombsNear(count);
        System.out.println(list);
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

        for(int i = 0; i<checkEmptyNeighbors(space).size(); i++) {

        }

    }


}


