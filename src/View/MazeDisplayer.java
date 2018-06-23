package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import static java.lang.Thread.sleep;

/**
 * This class represents a server that produces mazes
 * @author Roee Sanker & Yossi Hekter
 */
public class MazeDisplayer extends Canvas{

    //Maze matrix
    private byte[][] maze;

    //Player row size
    private int playerPositionRow;

    //Player column size
    private int playerPositionColumn;

    //The height of a cell in the maze
    private double cellHeight;

    //The width of a cell in the maze
    private double cellWidth;

    //If the player won
    private boolean won;

    //Image of the wall, road, player and every other image that the maze use
    Image wall;
    Image freeWay;
    Image player;
    Image solWay;
    Image goalImage;
    Image winImage;
    Image hintImage;

    /**
     * This function init the maze
     * @param newMaze The maze matrix
     * @param playerPosRow the player start position
     */
    public void setFields(byte[][] newMaze, int playerPosRow, int playerPosColumn){
        maze = newMaze;
        playerPositionRow = playerPosRow;
        playerPositionColumn = playerPosColumn;
        won = false;
        try {
            wall = new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("wall3.jpg"));
            freeWay =  new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("Free_way.jpg"));
            player = new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("player1.jpg"));
            solWay = new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("sol_way3.jpg"));
            goalImage = new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("goal_image.jpg"));
            winImage = new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("win_image.jpg"));
            hintImage = new Image(MazeDisplayer.class.getClassLoader().getResourceAsStream("hint_image2.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function draw the maze on the canvas
     */
    public void drawMaze() {
        if (maze != null) {
            won = false;
            setHeightAndWidth();
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            gc.drawImage(wall, j*cellHeight,i*cellWidth,  cellHeight, cellWidth);
                        }
                        else{
                            if(j == maze[0].length -1 )//goal
                                gc.drawImage(goalImage, j*cellHeight,i*cellWidth,  cellHeight, cellWidth);
                            else
                                gc.drawImage(freeWay, j*cellHeight,i*cellWidth,  cellHeight, cellWidth);
                        }
                    }
                }
                //Draw Character
                gc.drawImage(player, playerPositionColumn * cellHeight, playerPositionRow * cellWidth, cellHeight, cellWidth);
        }
    }

    /**
     * This function draw the player after he moved
     * @param newRow the new  row position
     * @param newColumn the new  column position
     */
    public void movePlayer(int newRow, int newColumn){
        setHeightAndWidth();
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(freeWay, playerPositionColumn*cellHeight,playerPositionRow*cellWidth,  cellHeight, cellWidth);
        gc.drawImage(player, newColumn*cellHeight,newRow*cellWidth,  cellHeight, cellWidth);
        playerPositionColumn = newColumn;
        playerPositionRow = newRow;
    }

    /**
     * This function draw solution
     * @param sol The position of all the cell in the solution
     */
    public void persentSolution(ArrayList<AState> sol){
        setHeightAndWidth();
        drawMaze();
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(freeWay, playerPositionColumn * cellHeight,
                playerPositionRow * cellWidth, cellHeight, cellWidth);
        if(sol != null) {
            for (AState state : sol) {
                gc.drawImage(solWay, ((MazeState) state).getColumn() * cellHeight,
                        ((MazeState) state).getRow() * cellWidth, cellHeight, cellWidth);
            }
        }
    }

    /**
     * This function draw a win image on the screen
     */
    public void drawWin(){
        if(won){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(winImage, 0,0,   canvasWidth, canvasHeight);
        }
    }

    /**
     * This function draw a hint to the user
     * @param location The position of the hint
     */
    public void showHint(int[] location){
        setHeightAndWidth();
        GraphicsContext gc = getGraphicsContext2D();
        for(int i = 0; i<4 ;i++)
        {
            gc.drawImage(hintImage, location[1] * cellHeight,
                    location[0] * cellWidth, cellHeight, cellWidth);
        }
    }

    /**
     * This function calculate the size of the cell for the maze
     */
    private void setHeightAndWidth(){
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        cellHeight = canvasWidth / maze[0].length;
        cellWidth = canvasHeight / maze.length;
    }

    /**
     * This function draw a free way instead of the hint
     * @param lastHint The position of the hint
     */
    public void deleteHint(int[] lastHint){
        setHeightAndWidth();
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(freeWay, lastHint[1] * cellHeight,
                lastHint[0] * cellWidth, cellHeight, cellWidth);
    }

    //setter
    public void isWon(boolean bool){ won = bool;}

    //getter

    public double getCellHeight(){
        setHeightAndWidth();
        return cellHeight;
    }

    public double getCellWidth(){
        setHeightAndWidth();
        return cellWidth;
    }
}
