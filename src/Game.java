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



    }

    private void setBombs() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (Math.random() < bombChance) {
                    grid[i][j] = new BombSpace(0,i,j);
                } else {
                    grid[i][j] = new Space(0,false,i,j);
                }
            }
        }
    }


}
