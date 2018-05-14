package algorithms.mazeGenerators;

/**
 * This class represents a maze object
 * @author Roee Sanker & Yossi Hekter
 */
public class Maze {

    /**
     * The start position of the maze
     */
    private Position start;

    /**
     * The goal position of the maze
     */
    private Position goal;

    /**
     * A matrix that represents the maze
     */
    private byte [][] mazeMatrix;

    /**
     * Constructor
     * @param row - the size of rows in the new maze
     * @param column - the size of columns in the new maze
     */
    public Maze(int row, int column){
        if(row<3 || column<3){
            row = 10;
            column = 10;
        }
        mazeMatrix = new byte[row][column];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++)
                mazeMatrix[i][j]=1;
        }
        start = new Position();
        goal = new Position();
    }

    //<editor-fold desc="Getters">
    /**
     * @return The start position of the maze
     */
    public Position getStartPosition() {
        return start;
    }

    /**
     * @return The goal position of the maze
     */
    public Position getGoalPosition() {
        return goal;
    }

    /**
     * @return The maze matrix
     */
    public byte[][] getMazeMatrix() {
        return mazeMatrix;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    /**
     * @param start - the maze's start position
     */
    protected void setStartPosition(Position start) { this.start = start; }

    /**
     * @param goal - the maze's goal position
     */
    protected void setGoalPosition(Position goal) { this.goal = goal; }

    /**
     * @param maze - the maze's matrix
     */
    protected void setMazeMatrix(byte[][] maze) { this.mazeMatrix = maze; }
    //</editor-fold>

    /**
     * A print method, print the maze matrix row by row
     */
    public void print() {
        int startRow = start.getRowIndex();
        int startColumn = start.getColumnIndex();
        int goalRow = goal.getRowIndex();
        int goalColumn = goal.getColumnIndex();
        for(int i =0; i < mazeMatrix.length; i++){
            for(int j =0; j < mazeMatrix[i].length;j++){
                if(i==startRow && j==startColumn)
                    System.out.print("S");
                else if(i==goalRow && j==goalColumn)
                    System.out.print("E");
                else
                    System.out.print(mazeMatrix[i][j]);
                if(!(j==mazeMatrix[i].length -1))
                    System.out.print(", ");
            }
            System.out.println();
        }
    }

    /**
     * This function check if  the given position is on the maze's matrix
     * @param row - the size of the position's row
     * @param column - the size of position's column
     * @return If the position is on the matrix
     */
    public boolean onMatrix(int row, int column){
        return (row< mazeMatrix.length && row >=0 && column< mazeMatrix[0].length && column >=0);
    }

    /**
     * This function check if  the given position is on the maze's matrix without the frame
     * @param row - the size of the position's row
     * @param column - the size of position's column
     * @return If the position is on the matrix without the frame
     */
    public boolean onMatrixLimit(int row, int column){
        return (row == mazeMatrix.length - 1 || row == 0 || column ==  mazeMatrix[0].length - 1  || column == 0);
    }

    /**
     * This function check if the given position is available
     * @param row - the size of the position's row
     * @param column - the size of position's column
     * @return If the position is available
     */
    public boolean isAvailable(int row, int column){
        return (onMatrix(row, column) && mazeMatrix[row][column] == 0);
    }

    public byte[] toByteArray(){
        byte[] tmpByteMaze = new byte[100];
        byte[] byteMaze;
        int row = mazeMatrix.length;
        int column = mazeMatrix[0].length;
        int start = getStartPosition().getRowIndex();
        int goal = getGoalPosition().getRowIndex();
        int counter = 0;

        //set the number of rows
        setValue(tmpByteMaze, row, counter);

        //set the number of rows
        setValue(tmpByteMaze, column, counter);

        //set the start position
        setValue(tmpByteMaze, start, counter);

        //set the goal position
        setValue(tmpByteMaze, goal, counter);

        //set the size of the final byte array
        byteMaze = new byte[row*column + counter];

        //copy from the tmp array to the final array
        copyToByteMaze(byteMaze, tmpByteMaze);

        //copy the maze's matrix to the byte array
        createByteMaze(byteMaze, counter);
        return byteMaze;
    }


    void copyToByteMaze(byte[] target, byte[] sourse){
        for(int i=0; i<sourse.length;i++){
            target[i]=sourse[i];
        }
    }

    void createByteMaze(byte[] byteMaze, int index){
        for(int i=0; i<mazeMatrix.length;i++){
            for(int j=0; j<mazeMatrix[0].length;j++){
                byteMaze[index]=mazeMatrix[i][j];
            }
        }
    }

    void setValue (byte[] array, int position, int index )
    {
        while(position>255){
            array[index] = -1;
            position-=255;
            index++;
        }
        if(position>0){
            array[index] = (byte)position;
            index++;
        }
        array[index]=0;
        index++;
    }


}
