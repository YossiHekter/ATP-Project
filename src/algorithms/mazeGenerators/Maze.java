package algorithms.mazeGenerators;

import java.io.Serializable;

/**
 * This class represents a maze object
 * @author Roee Sanker & Yossi Hekter
 */
public class Maze implements Serializable {

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

    /**
     * a constructor that receives a one-dimensional array of byte
     * @param maze - a one-dimensional array of byte representing a maze
     */
    public Maze(byte[] maze){
        int i = 0;
        int row = byteToInt(maze, i);
        i = nextZero(maze, i);

        int column = byteToInt(maze, i);
        i = nextZero(maze, i);

        int startRow = byteToInt(maze, i);
        i = nextZero(maze, i);

        int goalRow = byteToInt(maze, i);
        i = nextZero(maze, i);

        start = new Position(startRow, 0);
        goal = new Position(goalRow, column - 1);

        mazeMatrix = new byte[row][column];
        create2DByteMaze(maze, mazeMatrix, i);
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
        int row = getMazeMatrix().length;
        int column = getMazeMatrix()[0].length;
        int start = getStartPosition().getRowIndex();
        int goal = getGoalPosition().getRowIndex();
        int counter = 0;

        //size of 1D byte array represent all data of maze
        int size = digitsInByte(row) + digitsInByte(column) + digitsInByte(start) + digitsInByte(goal) + 4 + row*column;

        byte[] byteMaze = new byte[size];
        //set the number of rows
        counter = intToByte(byteMaze, row, counter);

        //set the number of rows
        counter = intToByte(byteMaze, column, counter);

        //set the start position
        counter = intToByte(byteMaze, start, counter);

        //set the goal posiotion
        counter = intToByte(byteMaze, goal, counter);

        //copy the maze's matrix to the byte array
        create1DByteMaze(byteMaze, mazeMatrix, counter);

        return byteMaze;
    }

    //create 1D array from 2D array
    private void create1DByteMaze(byte[] byteMazeD1, byte[][] byteMazeD2, int index){
        for(int i=0; i<byteMazeD2.length;i++){
            for(int j=0; j<byteMazeD2[0].length;j++, index++){
                byteMazeD1[index] = byteMazeD2[i][j];
            }
        }
    }

    //create 2D array from 1D array
    private void create2DByteMaze(byte[] byteMazeD1, byte[][] byteMazeD2, int index){
        for(int i=0; i<byteMazeD2.length;i++){
            for(int j=0; j<byteMazeD2[0].length;j++, index++){
                byteMazeD2[i][j] = byteMazeD1[index];
            }
        }
    }

    //convert int to byte and put on array, statr at index
    private int intToByte (byte[] array, int num, int index ){
        while(num>255){
            array[index] = -1;
            num-=255;
            index++;
        }
        if(num>0){
            array[index] = (byte)num;
            index++;
        }
        array[index]=0;
        index++;
        return index;
    }

    //return a int that represent by byte antil see zero in array
    private int byteToInt(byte[] arr, int i){
        int tmp = 0;
        while (arr[i] != 0){
            if(arr[i] < 0)
                tmp += arr[i] + 256;
            else
                tmp += arr[i];
            i++;
        }
        return tmp;
    }

    //return the number of byte that need to present int
    private int digitsInByte(int num){
        return (int)Math.ceil((double)num/255);
    }

    //return the index of next zero in array
    private int nextZero(byte[] arr, int index){
        while (arr[index] != 0)
            index++;
        index++;
        return index;
    }
}
