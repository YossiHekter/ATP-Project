package Model;

import algorithms.search.AState;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

/**
 * The interface of the model layer
 * @author Yossi Hekter
 */
public interface IModel {

    void generateMaze(int width, int height);
    void solveMaze();
    void moveCharacter(KeyCode movement);
    byte[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    byte getNumOfHints();
    void getHint();
    int[] ShowHint();
    ArrayList<AState> getSolution();
    void stopServers();
    void startServers();
    void saveMaze(String name);
    void loadMaze(String name);
    void dragMove(int row, int column);
}
