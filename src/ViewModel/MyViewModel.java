package ViewModel;

import Model.IModel;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

/**
 * This class connect between the view class and the model class
 * @author Yossi Hekter
 */
public class MyViewModel  extends Observable implements Observer {

    //The model of the maze
    private IModel model;

    //The player row size
    private int playerPositionRowIndex;

    //The player column size
    private int playerPositionColumnIndex;

    //For Binding
    public StringProperty playerPositionRow = new SimpleStringProperty("1");
    public StringProperty playerPositionColumn = new SimpleStringProperty("1");

    //constructor
    public MyViewModel(IModel model){
        this.model = model;
    }

    /**
     * This function update this class according to his observer
     * @param o The class that we observed
     * @param arg The argument we received from the class
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o == model){
            switch((String)arg){

                //If the user create new maze
                case "mazeCreate":
                    playerPositionRowIndex = model.getCharacterPositionRow();
                    playerPositionRow.set(playerPositionRowIndex + "");
                    playerPositionColumnIndex = model.getCharacterPositionColumn();
                    playerPositionColumn.set(playerPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("mazeCreate");
                    break;

                //If the user move his player
                case "playerMoved":
                    playerPositionRowIndex = model.getCharacterPositionRow();
                    playerPositionRow.set(playerPositionRowIndex + "");
                    playerPositionColumnIndex = model.getCharacterPositionColumn();
                    playerPositionColumn.set(playerPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("playerMoved");
                    break;

                //If the user hit a wall while moved his player
                case "hitWall":
                    setChanged();
                    notifyObservers("hitWall");
                    break;

                //If the user ask for the solution of the maze
                case "mazeSolve":
                    setChanged();
                    notifyObservers("mazeSolve");
                    break;

                //If the user solved the maze
                case "playerWin":
                    setChanged();
                    notifyObservers("playerWin");
                    break;

                //If the user ask for a hint to the maze
                case "getHint":
                    setChanged();
                    notifyObservers("getHint");
                    break;

                //If the user load a old maze
                case "loadMaze":
                    playerPositionRowIndex = model.getCharacterPositionRow();
                    playerPositionRow.set(playerPositionRowIndex + "");
                    playerPositionColumnIndex = model.getCharacterPositionColumn();
                    playerPositionColumn.set(playerPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("loadMaze");
                    break;
                case "dragSucceed":
                    playerPositionRowIndex = model.getCharacterPositionRow();
                    playerPositionRow.set(playerPositionRowIndex + "");
                    playerPositionColumnIndex = model.getCharacterPositionColumn();
                    playerPositionColumn.set(playerPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("dragSucceed");
                    break;
                case "dragFailed":
                    setChanged();
                    notifyObservers("dragFailed");
                    break;
            }
        }
    }


    /**
     * This function stop the server
     */
    public void stopServe(){
        model.stopServers();
    }

    /**
     * This functions connect between the view and the model
     */
    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }
    public void solveMaze(){
        model.solveMaze();
    }
    public void saveMaze(String name){ model.saveMaze(name); }
    public void loadMaze(String name){
        model.loadMaze(name);
    }
    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }
    public void getHint(){ model.getHint();}

    /**
     * This functions returning the view info after the model calculate it
     */
    public int getPlayerPositionRow() {
        return playerPositionRowIndex;
    }
    public int getPlayerPositionColumn() {
        return playerPositionColumnIndex;
    }
    public byte[][] getMaze() { return model.getMaze(); }
    public ArrayList<AState> getSolution(){ return model.getSolution(); }
    public int[] ShowHint(){return model.ShowHint();}
    public byte getNumOfHints() { return model.getNumOfHints(); }
    public void dragMove(int row, int column){ model.dragMove(row, column);}
}
