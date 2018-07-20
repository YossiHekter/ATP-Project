package algorithms.mazeGenerators;

/**
 * The interface of a Maze creator
 * @author Yossi Hekter
 */
public interface IMazeGenerator {
    /**
     * This function create the maze
     * @param row - the size of rows in the new maze
     * @param column - the size of columns in the new maze
     * @return a new maze in size row * column
     */
    Maze generate(int row, int column);

    /**
     * This function measure the time that takes to create the new maze
     * @param row - the size of rows in the new maze
     * @param column - the size of columns in the new maze
     * @return the time that takes to generate the maze
     */
    long measureAlgorithmTimeMillis(int row, int column);
}
