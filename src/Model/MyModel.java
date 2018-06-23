package Model;

import IO.*;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.*;

/**
 * This class is the class that responsible all the logic behind the app
 * @author Roee Sanker & Yossi Hekter
 */
public class MyModel extends Observable implements IModel {

    /**
     * The server that generate mazes
     */
    private Server mazeGeneratingServer;
    /**
     * The server that solve mazes
     */
    private Server solveSearchProblemServer;
    /**
     * The server that return hint to client
     */
    private Server hintServer;
    /**
     * The maze is currently playing
     */
    private Maze maze;
    /**
     * The row where the player is currently
     */
    private int characterPositionRow;
    /**
     * The column where the player is currently
     */
    private int characterPositionColumn;
    /**
     * The solution of the maze
     */
    private  ArrayList<AState> solution;
    /**
     * The hint to the user
     */
    private int[] hint = {0, 0};
    /**
     * The number of hint fro this maze currently
     */
    private byte numOfHints;

    /**
     * constructor
     */
    public MyModel(){
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        hintServer = new Server(5402, 1000, new ServerStrategySolveSearchProblem());
        characterPositionRow = 1;
        characterPositionColumn = 1;
        maze = null;
        numOfHints = 3;
    }

    /**
     * This function starts the servers
     */
    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
        hintServer.start();
    }

    /**
     * This function stops the servers
     */
    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
        hintServer.stop();
    }

    /**
     * This function sends a requirement to the server to produce a maze and restore the maze to view model
     * @param rows - the rows number
     * @param columns - the columns number
     */
    @Override
    public void generateMaze(int rows, int columns){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, columns};
                        toServer.writeObject(mazeDimensions); //send mazedimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed withMyCompressor)from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        int mazeRow = getNewMazeData(compressedMaze, 1);
                        int mazeCol = getNewMazeData(compressedMaze, 2);
                        int startData = getStartDataSpace(compressedMaze);
                        int size = mazeRow * mazeCol + startData;
                        byte[] decompressedMaze = new byte[size]; //allocating byte[] for the decompressedmaze -
                        is.read(decompressedMaze); //Fill decompressedMazewith bytes
                        maze = new Maze(decompressedMaze);
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        numOfHints = 3;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("mazeCreate");
    }

    /**
     * This function returns the initial data of the maze
     * @param maze - the maze
     * @param zero - the place of the data
     * @return - the data
     */
    private int getNewMazeData(byte[] maze, int zero){
        int ans = 0;
        int counter = 0;
        int index = 0;
        while (counter != zero-1){
            if (maze[index] == 0)
                counter++;
            index++;
        }
        while (counter != zero){
            if (maze[index] == 0)
                counter++;
            else {
                if (maze[index] > 0)
                    ans += maze[index];
                else
                    ans += maze[index] + 256;
            }
            index++;
        }
        return ans;
    }

    private int getStartDataSpace(byte[] maze){
        int index = 0;
        int counter = 0;
        while (counter != 4){
            if (maze[index] == 0)
                counter++;
            index++;
        }
        return index;
    }

    /**
     * This function sends a request to the server to solve the maze
     */
    @Override
    public void solveMaze(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor)from server
                        solution = mazeSolution.getSolutionPath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("mazeSolve");
    }

    /**
     * This function returns a hint to solve the maze
     */
    public void getHint(){
        numOfHints--;
        Position playerPos = new Position(characterPositionRow, characterPositionColumn);
        BestFirstSearch best = new BestFirstSearch();
        SearchableMaze mazeForHint = new SearchableMaze(new Maze(maze.getMazeMatrix(), playerPos, maze.getGoalPosition()));
        MazeState hintToUser = (MazeState) best.solve(mazeForHint).getSolutionPath().get(1);
        hint[0] = hintToUser.getRow();
        hint[1] = hintToUser.getColumn();
        setChanged();
        notifyObservers("getHint");
    }

    /**
     * This function moves the player if the request is valid
     * @param movement - the key that the user clicked on
     */
    @Override
    public void moveCharacter(KeyCode movement) {
        boolean hitWall = false;
        if (maze != null) {
            switch (movement) {
                case NUMPAD8://up
                    if (maze.isAvailable(characterPositionRow - 1, characterPositionColumn))
                        characterPositionRow--;
                    else
                        hitWall = true;
                    break;
                case NUMPAD2://down
                    if (maze.isAvailable(characterPositionRow + 1, characterPositionColumn))
                        characterPositionRow++;
                    else
                        hitWall = true;
                    break;
                case NUMPAD6://right
                    if (maze.isAvailable(characterPositionRow, characterPositionColumn + 1))
                        characterPositionColumn++;
                    else
                        hitWall = true;
                    break;
                case NUMPAD4://left
                    if (maze.isAvailable(characterPositionRow, characterPositionColumn - 1))
                        characterPositionColumn--;
                    else
                        hitWall = true;
                    break;
                case NUMPAD1://down left
                    if ((maze.isAvailable(characterPositionRow + 1, characterPositionColumn - 1))
                            && (maze.isAvailable(characterPositionRow + 1, characterPositionColumn) //down
                            || maze.isAvailable(characterPositionRow, characterPositionColumn - 1))) { //left
                        characterPositionColumn--;
                        characterPositionRow++;
                    }
                    else
                        hitWall = true;
                    break;
                case NUMPAD3://down right
                    if ((maze.isAvailable(characterPositionRow + 1, characterPositionColumn + 1))
                            && (maze.isAvailable(characterPositionRow + 1, characterPositionColumn) //down
                            || maze.isAvailable(characterPositionRow, characterPositionColumn + 1))) { //right
                        characterPositionColumn++;
                        characterPositionRow++;
                    }
                    else
                        hitWall = true;
                    break;
                case NUMPAD7://up left
                    if ((maze.isAvailable(characterPositionRow - 1, characterPositionColumn - 1))
                            && (maze.isAvailable(characterPositionRow - 1, characterPositionColumn) //up
                            || maze.isAvailable(characterPositionRow, characterPositionColumn - 1))) { //left
                        characterPositionColumn--;
                        characterPositionRow--;
                    }
                    else
                        hitWall = true;
                    break;
                case NUMPAD9://up right
                    if ((maze.isAvailable(characterPositionRow - 1, characterPositionColumn + 1))
                            && (maze.isAvailable(characterPositionRow - 1, characterPositionColumn) //up
                            || maze.isAvailable(characterPositionRow, characterPositionColumn + 1))) { //right
                        characterPositionColumn++;
                        characterPositionRow--;
                    }
                    else
                        hitWall = true;
                    break;
            }
        }
        setChanged();
        if (hitWall)
            notifyObservers("hitWall");
        else if(characterPositionColumn != maze.getGoalPosition().getColumnIndex())
            notifyObservers("playerMoved");
        else
            notifyObservers("playerWin");
    }

    /**
     * This function returns the position of the player row
     * @return - the player row
     */
    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    /**
     * This function returns the position of the player column
     * @return - the player column
     */
    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    /**
     * This function returns the number of hints
     * @return - the number of remain hints
     */
    @Override
    public byte getNumOfHints() {return numOfHints; }
    /**
     * This function returns the matrix of the maze
     * @return - the matrix of the maze
     */
    @Override
    public byte[][] getMaze() {
        if(maze == null)
            return null;
        else
            return maze.getMazeMatrix();
    }

    /**
     * This function returns the solution of the maze
     * @return - the solution of the maze
     */
    @Override
    public ArrayList<AState> getSolution(){
        return solution;
    }

    /**
     * This function returns the hint to the maze
     * @return - the hint to the maze
     */
    @Override
    public int[] ShowHint(){
        return hint;
    }

    /**
     * This function save the maze in the selected position
     * @param name - the selected location
     */
    public void saveMaze(String name){
        try {
            File file = new File(name);
            FileOutputStream output = new FileOutputStream(file);
            ObjectOutputStream objectOutput = new ObjectOutputStream(output);
            Object[] o = {maze, characterPositionRow, characterPositionColumn, numOfHints};
            objectOutput.writeObject(o);
            objectOutput.flush();
            objectOutput.close();
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function load the maze from selected position
     * @param name - the selected location
     */
    public void loadMaze(String name){
        try {
            File f = new File(name);
            if(f.exists()){
                FileInputStream in = new FileInputStream(f);
                ObjectInputStream mazeOnDisk = new ObjectInputStream(in);
                Object[] load = (Object[]) mazeOnDisk.readObject();
                maze = (Maze) load[0];
                characterPositionRow = (int) load[1];
                characterPositionColumn = (int) load[2];
                numOfHints = (byte) load[3];
                setChanged();
                notifyObservers("loadMaze");
            }
            else{

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void dragMove(int row, int column){
        if((Math.abs(characterPositionColumn - column) <= 1 && Math.abs(characterPositionRow - row) <= 1)
                && maze.isAvailable(row, column)) {
            if (row == characterPositionRow + 1 && column == characterPositionColumn - 1
                    && (maze.isAvailable(characterPositionRow + 1, characterPositionColumn - 1))
                    && (maze.isAvailable(characterPositionRow + 1, characterPositionColumn) //down
                    || maze.isAvailable(characterPositionRow, characterPositionColumn - 1))) { //left
                characterPositionColumn--;
                characterPositionRow++;
            } else if (row == characterPositionRow + 1 && column == characterPositionColumn + 1
                    && (maze.isAvailable(characterPositionRow + 1, characterPositionColumn + 1))
                    && (maze.isAvailable(characterPositionRow + 1, characterPositionColumn) //down
                    || maze.isAvailable(characterPositionRow, characterPositionColumn + 1))) { //right
                characterPositionColumn++;
                characterPositionRow++;
            } else if (row == characterPositionRow - 1 && column == characterPositionColumn - 1
                    && (maze.isAvailable(characterPositionRow - 1, characterPositionColumn - 1))
                    && (maze.isAvailable(characterPositionRow - 1, characterPositionColumn) //up
                    || maze.isAvailable(characterPositionRow, characterPositionColumn - 1))) { //left
                characterPositionColumn--;
                characterPositionRow--;
            } else if (row == characterPositionRow - 1 && column == characterPositionColumn + 1
                    && (maze.isAvailable(characterPositionRow - 1, characterPositionColumn + 1))
                    && (maze.isAvailable(characterPositionRow - 1, characterPositionColumn) //up
                    || maze.isAvailable(characterPositionRow, characterPositionColumn + 1))) { //right
                characterPositionColumn++;
                characterPositionRow--;
            }
            else if (row == characterPositionRow - 1 && maze.isAvailable(characterPositionRow - 1, characterPositionColumn))
                characterPositionRow--;
            else if (row == characterPositionRow + 1 && maze.isAvailable(characterPositionRow + 1, characterPositionColumn))
                characterPositionRow++;
            else if (column == characterPositionColumn + 1 && maze.isAvailable(characterPositionRow, characterPositionColumn + 1))
                characterPositionColumn++;
            else if (column == characterPositionColumn - 1 && maze.isAvailable(characterPositionRow, characterPositionColumn - 1))
                characterPositionColumn--;
            setChanged();
            if (characterPositionColumn == maze.getGoalPosition().getColumnIndex())
                notifyObservers("playerWin");
            else
                notifyObservers("dragSucceed");
        }
        else
        {
            setChanged();
            notifyObservers("dragFailed");
        }


        /*
        if((Math.abs(characterPositionColumn - column) <= 1 && Math.abs(characterPositionRow - row) <= 1)
                && maze.isAvailable(row, column)){
            characterPositionColumn = column;
            characterPositionRow = row;
            setChanged();
            if(characterPositionColumn == maze.getGoalPosition().getColumnIndex())
                notifyObservers("playerWin");
            else
                notifyObservers("dragSucceed");
        }
        else
        {
            setChanged();
            notifyObservers("dragFailed");
        }
        */
    }
}
