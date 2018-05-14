package algorithms.mazeGenerators;

/**
 * This class represents a position on the maze
 * @author Roee Sanker & Yossi Hekter
 */
public class Position {

    /**
     * The position's row
     */
    private int row;

    /**
     * The position's column
     */
    private int column;

    /**
     * Default Constructor
     */
    protected Position() {
        this.row = 0;
        this.column = 0;
    }

    /**
     * Constructor
     * @param row - the position new row
     * @param column - the position new row
     */
    public Position(int row, int column) {
        if(row < 0 || column < 0){
            this.row = 0;
            this.column = 0;
        }
        else {
            this.row = row;
            this.column = column;
        }

    }

    //<editor-fold desc="Getters">
    /**
     * @return The position's row index
     */
    public int getRowIndex() {
        return row;
    }

    /**
     * @return The position's column index
     */
    public int getColumnIndex() {
        return column;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    /**
     * @param row - new row index
     */
    protected void setRowIndex(int row) {
        this.row = row;
    }

    /**
     * @param column - new column index
     */
    protected void setColumnIndex(int column) {
        this.column = column;
    }
    //</editor-fold>

    /**
     * @return The position in string format
     */
    @Override
    public String toString(){
        return("{" + row + "," + column +"}");
    }

}
