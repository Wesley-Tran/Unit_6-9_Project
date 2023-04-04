import java.util.ArrayList;
import java.util.Scanner;

public class   Game {
    private Space[][] grid;
    private Space[][] displayGrid;
    private int numBombs;
    private final Scanner scan;

    public Game() {
        scan = new Scanner(System.in);
        grid = null;
        displayGrid = null;
        numBombs = 0;
    }

    public void play() {
        System.out.println("Welcome to Bootleg Minesweeper");
        System.out.print("What difficulty do you want\nEasy (7x7), Medium (11x11), Hard (13x13)\n> ");
        while (true) {
            String diff = scan.nextLine();
            if (!diff.equalsIgnoreCase("easy") &&
                !diff.equalsIgnoreCase("medium") &&
                !diff.equalsIgnoreCase("hard") &&
                !diff.equalsIgnoreCase("test")) {
                System.out.print("Please enter a valid difficulty:\nEasy (7x7), Medium (11x11), Hard (13x13)\n> ");
                continue;
            }
            switch (diff.toLowerCase()) {
                case "easy" -> {
                    grid = new Space[7][7];
                    displayGrid = new Space[7][7];
                }
                case "medium" -> {
                    grid = new Space[11][11];
                    displayGrid = new Space[11][11];
                }
                case "hard" -> {
                    grid = new Space[13][13];
                    displayGrid = new Space[13][13];
                }
                case "test" -> {
                    grid = new Space[4][4];
                    displayGrid = new Space[4][4];
                }
            }
            numBombs = (int) (grid.length * grid[0].length * ((double) 35 / 121));
            break;
        }
        boolean first = true;
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

            String temp = scan.nextLine();
            String[] inputList = getUserInputs(temp);
            int x = Integer.parseInt(inputList[0]);
            int y = Integer.parseInt(inputList[1]);
            temp = inputList[2];


            if (first) { //if its the first one we don't want them picking a bomb
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (isValidCoord(i, j)) {
                            grid[i][j] = new EmptySpace(0, i, j);
                            displayGrid[i][j] = grid[i][j];
                            displayGrid[i][j].setChosenTrue();
                        }
                    }
                }
                setGridNums();
                openSpace(displayGrid[x][y]);
                first = false;

            }
            if (!userChoice(x,y,temp)) {
                endGame(false);
                break;
            }
            won = determineWin();
            if (won) {
                endGame(won);
            }
        }
    }

    /**
     * prints out the displayGrid that the user will be seeing
     */
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

    /**
     * Prints out the behind-the-scenes grid that the is used for bug-testing
     */
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

    /**
     * Helper variable to get the user's coordinate and what they want to do
     * @param temp The first user response that is passed in
     * @return A string of the 3 responses, x coordinate, y coordinate, and the action
     */
    private String[] getUserInputs(String temp) {
        int x;
        int y;
        while (true) { //to get the user input
            try { // https://stackoverflow.com/questions/19925047/how-to-check-the-input-is-an-integer-or-not-in-java
                y = Integer.parseInt(temp.substring(0, temp.indexOf(" "))) - 1; //minus 1 to adhere to indexing
                x = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1)) - 1;
                if (x > -1 && x < grid.length) {
                    if (y > -1 && y < grid[0].length) {
                        break;
                    }
                }
                System.out.println("Enter a coordinate within bounds");
            } catch (Exception e) { //crashes when out of bounds - > not space
                System.out.println("\n\nEnter a valid coordinate");
                System.out.print("Enter the x and y coordinate with only a space in between x y: ");
                temp = scan.nextLine();
            }
        }

        System.out.print("Do you want to flag the space or open it?\n> ");
        temp = scan.nextLine();
        while (!temp.equalsIgnoreCase("flag") &&
                !temp.equalsIgnoreCase("open") &&
                !temp.equalsIgnoreCase("unflag")) {
            System.out.print("Enter either \"space\" or \"open\"\n> ");
            temp = scan.nextLine();
        }
        return new String[]{"" + x, "" + y, temp};
    }

    /**
     * Determines the following actions after the user picks a coordinate
     *
     * @param x The column that the user chose
     * @param y The row that the user chose
     * @param choice Whether the user wants to flag or open the chosen space
     * @return Whether their open was successful (they didn't see a bomb)
     */
    private boolean userChoice(int x, int y, String choice) {
        if (choice.equalsIgnoreCase("open")) {
            if (grid[x][y] instanceof BombSpace) {
                displayGrid[x][y].setChosenTrue();
                return false;
            }
            openSpace(grid[x][y]);
        } else if (choice.equalsIgnoreCase("flag")){
            displayGrid[x][y] = new FlaggedSpace(displayGrid[x][y].getNumBombsNear(),x,y,displayGrid[x][y]);
        } else if (choice.equalsIgnoreCase("unflag")) {
            displayGrid[x][y] = ((FlaggedSpace) displayGrid[x][y]).getSpace();
        }
        return true;
    }

    /**
     * Checks the grid for to see if all the spaces are open
     * @return Whether the user won or lost
     */
    private boolean determineWin() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j <grid[i].length; j++) {
                if (grid[i][j] instanceof EmptySpace && !displayGrid[i][j].isChosen()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines what happens when the game ends
     * <p>
     * Uses if statements to determine if the game ended due to a win or loss
     *
     * @param won Whether the game ended with a win or loss
     */
    private void endGame(boolean won) {
        if (!won) {
            System.out.println("YOU CHOSE A BOMB SPACE, YOU LOST");
            printGrid();
        } else {
            printGrid();
            System.out.println("You found all of the bombs!\nCongratulations!");
        }

    }

    /**
     * Puts bombs into the grid at random until there are a certain amount of bombs in the grid
     * Fills in all the remaining spots with empty spaces
     */
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
                    grid[i][j] = new EmptySpace(0,i,j);
                }
            }
        }

    }

    /**
     * Checks to see if a coordinate is within the bounds of the grid
     *
     * @param x The column of the grid
     * @param y The row of the grid
     * @return If the row and columns are within bounds of the grid
     */
    private boolean isValidCoord(int x, int y) {
        if (x > -1 && x < grid.length) {
            return y > -1 && y < grid[0].length;
        }
        return false;
    }


    /**
     * Goes through the list of spaces and if it is a Bomb,
     * update surrounding spaces to have +1 bombs
     */
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

    /**
     * Obtains a list of all the surrounding spaces
     * @param space Space to be checked
     * @return A list of all the spaces that surround the space
     */
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

    /**
     * Makes the space that the user chose, visible to the user
     * @param space Space that the user chose
     */
    private void openSpace(Space space) { //makes the space visible & surrounding spaces
        displayGrid[space.getX()][space.getY()] = grid[space.getX()][space.getY()];
        displayGrid[space.getX()][space.getY()].setChosenTrue();
        if (space.getNumBombsNear() == 0) {
            emptyOpen(displayGrid[space.getX()][space.getY()]);
        }
    }


    /**
     * Opens all the surrounding spaces of spaces that have no bombs
     * @param space The space that is being checked to see if it has bombs near it or not
     */
    private void emptyOpen (Space space){ //inspiration from https://stackoverflow.com/questions/19106911/recursive-minesweeper-0-fill
        ArrayList<Space> list = allNeighbors(space);
        for (Space obj : list) {
            if (!displayGrid[obj.getX()][obj.getY()].isChosen()) {
                openSpace(grid[obj.getX()][obj.getY()]);
            }
        }
    }
}

//        public void open(Space space){
//            grid[space.getX(), space.getY()].setChosenTrue();
//            displayGrid[space.getX(), space.getY()];
//        }

//    private void checkNeighbors(Space space) {
//        int currentX = space.getX();
//        int currentY = space.getY();
//        ArrayList<Space> list = new ArrayList<>();
//
//        if (isValidCoord(currentX-1,currentY)) { //left
//            if (grid[currentX-1][currentY] instanceof BombSpace) {list.add(grid[currentX - 1][currentY]);}
//        }
//        if (isValidCoord(currentX-1,currentY-1)) { //top left
//            if (grid[currentX-1][currentY-1] instanceof BombSpace) {list.add(grid[currentX - 1][currentY - 1]);}
//        }
//        if (isValidCoord(currentX,currentY-1)) { //top
//            if (grid[currentX][currentY-1] instanceof BombSpace) {list.add(grid[currentX][currentY - 1]);}
//        }
//        if (isValidCoord(currentX+1,currentY-1)) { //top right
//            if (grid[currentX+1][currentY-1] instanceof BombSpace) {list.add(grid[currentX+1][currentY-1]);}
//        }
//        if (isValidCoord(currentX+1,currentY)) { //right
//            if (grid[currentX+1][currentY] instanceof BombSpace) {list.add(grid[currentX+1][currentY]);}
//        }
//        if (isValidCoord(currentX+1,currentY+1)) { //bottom right
//            if (grid[currentX+1][currentY+1] instanceof BombSpace) {list.add(grid[currentX+1][currentY+1]);}
//        }
//        if (isValidCoord(currentX,currentY+1)) { //bottom
//            if (grid[currentX][currentY+1] instanceof BombSpace) {list.add(grid[currentX][currentY+1]);}
//        }
//        if (isValidCoord(currentX-1,currentY+1)) { //bottom left
//            if (grid[currentX-1][currentY+1] instanceof BombSpace) {list.add(grid[currentX-1][currentY+1]);}
//        }
//        space.setNumBombsNear(list.size());
//    } //sets the number of bombs near a space


