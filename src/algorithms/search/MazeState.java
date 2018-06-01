package algorithms.search;
import algorithms.mazeGenerators.Position;

import java.io.Serializable;

/**
 * This class represents a state in the maze
 * Maze state is represented by position class
 * @author Roee Sanker & Yossi Hekter
 */
public class MazeState extends AState {

    /**
     * The Position coordinates of the state
     */
    private Position pos;

    //<editor-fold desc="Constructors">
    /**
     * Constructor
     * @param state A string representing the coordinates
     */
    public MazeState(String state) {
        super(state);
        String[] tmp = getState().split(",");
        pos = new Position(Integer.parseInt(tmp[0].substring(1)), Integer.parseInt(tmp[1].substring(0,tmp[1].length()-1)));
    }

    /**
     * Constructor
     * @param state A string representing the coordinates
     * @param cost The cost of the new maze state
     * @param prev The maze state from which we reached the new state
     */
    public MazeState(String state, double cost, MazeState prev) {
        super(state, cost, prev);
        String[] tmp = getState().split(",");
        pos = new Position(Integer.parseInt(tmp[0].substring(1)), Integer.parseInt(tmp[1].substring(0,tmp[1].length()-1)));
    }
    //</editor-fold>

    //<editor-fold desc="Getters">

    /**
     * This function returns the row coordinate
     * @return Row index of the current state
     */
    public int getRow() { return pos.getRowIndex(); }

    /**
     * This function returns the column coordinate
     * @return Column index of the current state
     */
    public int getColumn() { return pos.getColumnIndex(); }
    //</editor-fold>

    //<editor-fold desc="Setters">

    /**
     *
     * @param row
     */
    //protected void setRow(int row) { this.row = row; }

    /**
     *
     * @param column
     */
    //protected void setColumn(int column) { this.column = column; }
    //</editor-fold>

    /**
     * This function returns a string representing the coordinates of the current state
     * @return String in the following format "{row_coordinate,column_coordinate}"
     */
    @Override
    public String toString(){ return pos.toString(); }
}
