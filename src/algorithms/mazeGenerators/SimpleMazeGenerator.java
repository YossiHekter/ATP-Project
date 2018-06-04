package algorithms.mazeGenerators;

/**
 * This class represents a simple maze
 * @author Roee Sanker & Yossi Hekter
 */
public class SimpleMazeGenerator extends AMazeGenerator {

    /**
     * Constructor
     */
    public SimpleMazeGenerator(){super();}

    /**
     * Function that create a new simple maze in size row * column
     * @param row - the size of rows in the new maze
     * @param column - the size of columns in the new maze
     * @return The new maze
     */
    public Maze generate(int row, int column){
        if(row <3 || column <3)
            setMaze(new Maze(10, 10));
        else
            setMaze(new Maze(row, column));

        //choose start position
        Position start = new Position( randomRow(row), 0);
        getMaze().setStartPosition(start);
        getMaze().getMazeMatrix()[start.getRowIndex()][0] = 0;

        //choose goal position
        Position finish = new Position( randomRow(row), column -1);
        getMaze().setGoalPosition(finish);
        getMaze().getMazeMatrix()[finish.getRowIndex()][column - 1] = 0;

        //random wall building
        ranWallBuilding();

        //create a valid route
        ranValidRoute();

        return getMaze();
    }

    /**
     * Method that build randoms wall in the maze
     * 70% of the maze's position be walls
     */
    private void ranWallBuilding(){

        int row = getMaze().getMazeMatrix().length;
        int column = getMaze().getMazeMatrix()[0].length;
        int rowStart = getMaze().getStartPosition().getRowIndex();
        int columnStart = getMaze().getStartPosition().getColumnIndex();
        int rowGoal = getMaze().getGoalPosition().getRowIndex();
        int columnGoal = getMaze().getGoalPosition().getColumnIndex();

        for(int i = 1; i < row - 1; i++){
            for(int j = 1; j < column - 1; j++) {
                double ran = Math.random();
                if( (i == rowStart && j == columnStart) || (i == rowGoal && j == columnGoal))
                    continue;
                if (0.3 > ran)
                    getMaze().getMazeMatrix()[i][j] = 0;
            }
        }
    }

    /**
     * Method that create a route between the start and the goal position
     * in the maze
     */
    private void ranValidRoute(){
        byte[][] matrix = getMaze().getMazeMatrix();
        int row = matrix.length;
        int column = matrix[0].length;
        int rowStart = getMaze().getStartPosition().getRowIndex();
        int rowGoal = getMaze().getGoalPosition().getRowIndex();
        int columnStart = 1;
        matrix[rowStart][1] = 0;

        int flag=0;
        if(rowStart > rowGoal)
            flag =1;
        else if(rowStart < rowGoal)
            flag =-1;
        while(rowStart != rowGoal || columnStart != column -1){
            if(columnStart == column-1){
                if(flag==1)
                    rowStart--;
                else
                    rowStart++;
            }
            else if(rowStart == rowGoal)
                columnStart++;
            else{
                double ran = Math.random();
                if (0.5 > ran)
                    columnStart++;
                else{
                    if(flag ==1)
                        rowStart--;
                    else
                        rowStart++;
                }
            }
            matrix[rowStart][columnStart]=0;
        }
        getMaze().setMazeMatrix(matrix);
    }

}
