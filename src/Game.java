import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class   Game {
    // to fill bombs; go through 2D array with a chance to set bomb to every space -> if row has bombs / lower chance of bomb
    // if not enough bombs, traverse again (backwards or forwards)

    private Space[][] grid;
    private Space[][] displayGrid;
    private static int numBombs;

    //no constructor

    public void play() {
        System.out.println("Welcome to Bootleg Minesweeper");
        System.out.println("What difficulty do you want\n> "); //medium 11x11 //stay on odd numbers

        boolean first = true;
        numBombs = 25; //diffuculty
        grid = new Space[11][11]; //diff
        displayGrid = new Space[11][11]; //diff
        for (int i = 0; i < displayGrid.length; i++) { // initialize displayGrid
            for (int j = 0; j < displayGrid[i].length;j++) {
                displayGrid[i][j] = new EmptySpace(0,i,j);
            }
        }

        setBombs();
        boolean won = false;
        //test code to be deleted
        for (Space[] i : grid) {
            for (Space j : i) {
                j.setChosenTrue();
            }
        }
        //test code to be deleted
        while (!won) {
            //test code to be deleted
            testPrintGrid();
            System.out.println();
            //test code to be deleted
            printGrid();
            System.out.println("\n\nSelect a space");
            System.out.print("Enter the x and y coordinate with only a space in between: ");

            Scanner scan = new Scanner(System.in);

            String temp = scan.nextLine();
            int x;
            int y;
            while (true) { //to get the user input
                try { // https://stackoverflow.com/questions/19925047/how-to-check-the-input-is-an-integer-or-not-in-java
                    y = Integer.parseInt(temp.substring(0, temp.indexOf(" "))) - 1; //minus 1 to adhere to indexing
                    x = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1)) - 1;
                    break;
                } catch (NumberFormatException e) { //crashes when out of bounds - > not space
                    System.out.println("\n\nEnter a valid coordinate");
                    System.out.print("Enter the x and y coordinate with only a space in between (x y): ");
                    temp = scan.nextLine();
                }
            }



            System.out.print("Do you want to flag the space or open it?\n> ");
            temp = scan.nextLine();
            while (!temp.equals("flag") && !temp.equals("open")) {
                System.out.println("Enter either \"space\" or \"open\"\n> ");
                temp = scan.nextLine();
            }
            if (first) { //if its the first one we don't want them picking a bomb
                for (int i = x-1; i < x + 2; i++) {
                    for (int j = y-1; j < y + 2; j++) {
                        if (isValidCoord(i,j)) {
                            grid[i][j] = new EmptySpace(0, i, j);
                            grid[i][j].setChosenTrue();
                            openSpace(grid[i][j],checkEmptyNeighbors(grid[i][j]));
                            displayGrid[i][j] = grid[i][j];
                        }
                    }
                }
                System.out.println("before setting #");
                setGridNums();

            }

            if (!userChoice(x,y,temp)) {
                endGame(false);
                break;
            }
            //if bomb end the game

                //go through the grid and any space that is a bomb, set displaygrid to it
            //check the surroudings and open any spaces that have 0 bombs

        }


    }

    private void printGrid(){ //for user view
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
    private void testPrintGrid(){ //for developer view
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

    private boolean userChoice(int x, int y, String choice) {
        if (choice.equalsIgnoreCase("open")) {
            if (grid[x][y] instanceof BombSpace) {
                return false;
            }
            openSpace(grid[x][y], checkEmptyNeighbors(grid[x][y]));
        } else {
            displayGrid[x][y] = new FlaggedSpace(displayGrid[x][y].getNumBombsNear(),x,y);
        }
        return true;
    }

    private void endGame(boolean won) {
        System.out.println("YOU CHOSE A BOMB SPACE, YOU LOST");
        printGrid();

    }


    private void setBombs() {
        while (numBombs != 0) {
            int row = (int) (Math.random() * (grid.length));
            int column = (int) (Math.random() * (grid[0].length));

            if (!(grid[row][column] instanceof BombSpace)) {
                grid[row][column] = new BombSpace(0,column,row);
                numBombs--;
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!(grid[i][j] instanceof BombSpace)) {
                    grid[i][j] = new EmptySpace(0,j,i);
                }
            }
        }

    }

    private boolean isValidCoord(int x, int y) {
        if (x > -1 && x < grid.length) {
            return y > -1 && y < grid[0].length;
        }

        return false;
    } //check for bounds

    private void checkNeighbors(Space space) {
        int currentX = space.getX();
        int currentY = space.getY();
        ArrayList<Space> list = new ArrayList<>();

        if (isValidCoord(currentX-1,currentY)) { //left
            if (grid[currentX-1][currentY] instanceof BombSpace) {list.add(grid[currentX - 1][currentY]);}
        }
        if (isValidCoord(currentX-1,currentY-1)) { //top left
            if (grid[currentX-1][currentY-1] instanceof BombSpace) {list.add(grid[currentX - 1][currentY - 1]);}
        }
        if (isValidCoord(currentX,currentY-1)) { //top
            if (grid[currentX][currentY-1] instanceof BombSpace) {list.add(grid[currentX][currentY - 1]);}
        }
        if (isValidCoord(currentX+1,currentY-1)) { //top right
            if (grid[currentX+1][currentY-1] instanceof BombSpace) {list.add(grid[currentX+1][currentY-1]);}
        }
        if (isValidCoord(currentX+1,currentY)) { //right
            if (grid[currentX+1][currentY] instanceof BombSpace) {list.add(grid[currentX+1][currentY]);}
        }
        if (isValidCoord(currentX+1,currentY+1)) { //bottom right
            if (grid[currentX+1][currentY+1] instanceof BombSpace) {list.add(grid[currentX+1][currentY+1]);}
        }
        if (isValidCoord(currentX,currentY+1)) { //bottom
            if (grid[currentX][currentY+1] instanceof BombSpace) {list.add(grid[currentX][currentY+1]);}
        }
        if (isValidCoord(currentX-1,currentY+1)) { //bottom left
            if (grid[currentX-1][currentY+1] instanceof BombSpace) {list.add(grid[currentX-1][currentY+1]);}
        }
        space.setNumBombsNear(list.size());
    } //sets the number of bombs near a space

    public ArrayList<Space> checkEmptyNeighbors(Space space){
        int currentX = space.getX();
        int currentY = space.getY();
        ArrayList<Space> list = new ArrayList<Space>();

        if (isValidCoord(currentX-1,currentY)) { //left
            if(grid[currentX-1][currentY] instanceof EmptySpace){
                list.add(grid[currentX-1][currentY]);
            }
        }
        if (isValidCoord(currentX-1,currentY-1)) { //top left
            if(grid[currentX-1][currentY-1] instanceof EmptySpace){
                list.add(grid[currentX-1][currentY-1]);
            }
        }
        if (isValidCoord(currentX,currentY-1)) { //top
            if(grid[currentX][currentY-1] instanceof EmptySpace){
                list.add(grid[currentX][currentY-1]);
            }
        }
        if (isValidCoord(currentX+1,currentY-1)) { //top right
            if(grid[currentX+1][currentY-1] instanceof EmptySpace){
                list.add(grid[currentX+1][currentY-1]);
            }
        }
        if (isValidCoord(currentX+1,currentY)) { //right
            if(grid[currentX+1][currentY] instanceof EmptySpace){
                list.add(grid[currentX+1][currentY]);
            }
        }
        return list;
    } //returns list of emptySpace around


    public void setGridNums(){
        for(int i = 0; i< grid.length; i++){
            for(int j =0; j< grid[0].length; j++){
                if(grid[i][j] instanceof BombSpace){
                    if (isValidCoord(i-1,j)) { //left
                        if(grid[i-1][j] instanceof EmptySpace){
                            grid[i-1][j].addNumBombsNear();
                        }
                    }
                    if (isValidCoord(i+1,j)) { //right
                        if(grid[i+1][j] instanceof EmptySpace){
                            grid[i+1][j].addNumBombsNear();
                        }
                    }
                    if (isValidCoord(i+1,j+1)) { // bottom right
                        if(grid[i+1][j+1] instanceof EmptySpace){
                            grid[i+1][j+1].addNumBombsNear();
                        }
                    }
                    if (isValidCoord(i,j+1)) { // bottom
                        if(grid[i][j+1] instanceof EmptySpace){
                            grid[i][j+1].addNumBombsNear();
                        }
                    }
                    if (isValidCoord(i-1,j+1)) { // bottom left
                        if(grid[i-1][j+1] instanceof EmptySpace){
                            grid[i-1][j+1].addNumBombsNear();
                        }
                    }

                    if (isValidCoord(i-1,j-1)) { //top left
                        if(grid[i-1][j-1] instanceof EmptySpace){
                            grid[i-1][j-1].addNumBombsNear();
                        }
                    }
                    if (isValidCoord(i,j-1)) { //top
                        if(grid[i][j-1] instanceof EmptySpace){
                            grid[i][j-1].addNumBombsNear();
                        }
                    }
                    if (isValidCoord(i+1,j-1)) { //top right
                        if(grid[i+1][j-1] instanceof EmptySpace){
                            grid[i+1][j-1].addNumBombsNear();
                        }
                    }

                }
            }
        }
    }
    public ArrayList<Space> allNeighbors(Space space){
        int currentX = space.getX();
        int currentY = space.getY();
        ArrayList<Space> list = new ArrayList<Space>();

        if (isValidCoord(currentX-1,currentY)) { //left
                list.add(grid[currentX-1][currentY]);
        }
        if (isValidCoord(currentX-1,currentY-1)) { //top left
                list.add(grid[currentX-1][currentY-1]);
        }
        if (isValidCoord(currentX,currentY-1)) { //top
                list.add(grid[currentX][currentY-1]);
        }
        if (isValidCoord(currentX+1,currentY-1)) { //top right
            list.add(grid[currentX+1][currentY-1]);
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
        return list;
    }

    private void openSpace(Space space,ArrayList<Space> list){ //makes the space visible & surrounding spaces
        if(space instanceof BombSpace){
            displayGrid[space.getX()][space.getY()] = grid[space.getX()][space.getY()];
            displayGrid[space.getX()][space.getY()].setChosenTrue();
        }
        for (Space value : list) {
            if (value.getNumBombsNear() == 0) {
                for (Space i : checkEmptyNeighbors(value)) {
                    i.setChosenTrue();
                }
            }
        }
//
    }

    private void emptyOpen (Space space){
        ArrayList<Space> extra = allNeighbors(space);
        for(int i = 0; i<extra.size();i++){
            if(!(extra.get(i).isChosen())){
                extra.get(i).setChosenTrue();
                if(extra.get(i) instanceof EmptySpace){
                    emptyOpen(extra.get(i));
                }
            }
        }
    }


//        public void open(Space space){
//            grid[space.getX(), space.getY()].setChosenTrue();
//            displayGrid[space.getX(), space.getY()];
//        }
}


