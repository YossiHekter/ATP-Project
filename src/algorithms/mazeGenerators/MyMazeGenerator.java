package algorithms.mazeGenerators;
import java.util.Vector;

/**
 * This class represents a maze that generated using prim's algorithm
 * @author Roee Sanker & Yossi Hekter
 */
public class MyMazeGenerator extends AMazeGenerator {

    /**
     * Constructor
     */
    public MyMazeGenerator(){super();}

    /**
     * This function create a new maze in size row * column using prim's algorithm
     * @param row - the size of rows in the new maze
     * @param column - the size of columns in the new maze
     * @return The new maze
     */
    public Maze generate(int row, int column){
        setMaze(new Maze(row, column));

        //choose start position
        Position start = new Position( randomRow(row), 0);
        getMaze().setStartPosition(start);
        getMaze().getMazeMatrix()[start.getRowIndex()][start.getColumnIndex()] = 0;

        //run Randomized Prim's algorithm
        RandomizedPrim();

        return getMaze();
    }

    /**
     *  Method that run prim's algorithm to build random
     *  walls in the maze
     */
    private void RandomizedPrim(){
        Maze m = getMaze();
        byte [][] matrix = m.getMazeMatrix();
        Vector<Position> walls = new Vector<Position>();
        addWalls(walls, matrix, m.getStartPosition());

        while(!walls.isEmpty()){
            int index = (int)(Math.random() * walls.size());
            Position wall = walls.remove(index);
            if(onlyOnePath(wall)){
                matrix[wall.getRowIndex()][wall.getColumnIndex()]=0;
                addWalls(walls, matrix, wall);
            }
        }
        int endRow = randomRow(matrix.length);
        while(matrix[endRow][matrix[0].length - 2] == 1)
            endRow = randomRow(matrix.length);
        Position end = new Position( endRow, matrix[0].length-1);
        getMaze().setGoalPosition(end);
        matrix[endRow][matrix[0].length - 1] = 0;
        getMaze().setMazeMatrix(matrix);
    }

    /**
     * Method that add walls around a specific position
     * @param myVector - data structure that contains the walls position
     * @param matrix - the maze
     * @param pos - a position on the maze
     */
    private void addWalls(Vector<Position> myVector, byte [][] matrix, Position pos){
        int row = pos.getRowIndex();
        int column = pos.getColumnIndex();
        Maze m = getMaze();
        if(m.onMatrix(row + 1, column) && !m.onMatrixLimit(row + 1, column) && matrix[row + 1][column] == 1){
            Position tmpPos = new Position(row + 1, column);
            myVector.add(tmpPos);
        }

        if(m.onMatrix(row - 1, column) && !m.onMatrixLimit(row - 1, column) && matrix[row - 1][column] == 1){
            Position tmpPos = new Position(row - 1, column);
            myVector.add(tmpPos);
        }

        if(m.onMatrix(row, column + 1) && !m.onMatrixLimit(row, column + 1) && matrix[row][column + 1] == 1){
            Position tmpPos = new Position(row, column + 1);
            myVector.add(tmpPos);
        }

        if(m.onMatrix(row, column - 1) && !m.onMatrixLimit(row, column - 1) && matrix[row][column - 1] == 1){
            Position tmpPos = new Position(row, column - 1);
            myVector.add(tmpPos);
        }
    }

    /**
     * This function check if there is only one path to the given position
     * @param pos - position on the maze
     * @return If there is nly one path to the position
     */
    private boolean onlyOnePath(Position pos){
        int row = pos.getRowIndex();
        int column = pos.getColumnIndex();
        int flag = 0;
        Maze maze = getMaze();
        if(maze.onMatrix(row + 1, column)&& maze.isAvailable(row + 1, column)){
            flag++;
        }

        if(maze.onMatrix(row - 1, column)&& maze.isAvailable(row - 1, column)){
            flag++;
        }

        if(maze.onMatrix(row, column + 1) && maze.isAvailable(row, column + 1)){
            flag++;
        }

        if(maze.onMatrix(row, column-1)&& maze.isAvailable(row, column-1)){
            flag++;
        }
        return flag ==1;
    }

}
