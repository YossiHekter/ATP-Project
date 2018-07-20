package algorithms.mazeGenerators;

/**
 * This class represents an abstract maze
 * @author Yossi Hekter
 */
public abstract class AMazeGenerator implements IMazeGenerator{
    private Maze maze;

    /**
     * Constructor
     */
    public AMazeGenerator(){maze = null;}

    /**
     * This function measure the time that takes to create the new maze
     * @param row - the size of rows in the new maze
     * @param column - the size of columns in the new maze
     * @return the time that takes to generate the maze
     */
    public long measureAlgorithmTimeMillis(int row, int column){
        long startTime = System.currentTimeMillis();
        this.generate(row, column);
        long finishTime = System.currentTimeMillis();
        return finishTime - startTime;
    }

    /**
     * Getter
     * @return The current maze
     */
    protected Maze getMaze() {
        return maze;
    }

    /**
     * Setter
     * @param maze - A new maze
     */
    protected void setMaze(Maze maze) {
        this.maze = maze;
    }

    /**
     * @param size - The size of the row, the limit of the random number
     * @return A random number in the range 1 until size-1
     */
    protected int randomRow(int size){
        int ranRow = (int)(Math.random() * size);
        while (ranRow == 0 || ranRow == size-1)
            ranRow = (int)(Math.random() * size);
        return ranRow;
    }
}
