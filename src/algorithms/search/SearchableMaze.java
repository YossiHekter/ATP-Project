package algorithms.search;

import java.util.ArrayList;
import algorithms.mazeGenerators.Maze;

/**
 * This class represents a maze that can be searched by search algorithms
 * @author Roee Sanker & Yossi Hekter
 */
public class SearchableMaze implements ISearchable {
    /**
     * The maze
     */
    private Maze adeptMaze;
    /**
     * The start state in the maze
     */
    private MazeState startState;
    /**
     * The goal state in the maze
     */
    private MazeState goalState;
    /**
     * A two-dimensional array the size of the maze that keeps all the nodes we have developed
     */
    private boolean[][] visited;

    /**
     * Constructor
     * @param maze The maze on which we will perform the search
     */
    public SearchableMaze (Maze maze){
        adeptMaze = maze;
        startState = new MazeState(adeptMaze.getStartPosition().toString());
        goalState = new MazeState(adeptMaze.getGoalPosition().toString());
        visited = new boolean[adeptMaze.getMazeMatrix().length][maze.getMazeMatrix()[0].length];
        resetVisited();
    }

    /**
     * This function checks whether we have already developed the maze state
     * @param state The maze state examined
     * @return True if the maze state is develop or false if not
     */
    @Override
    public boolean isVisit(AState state) {
        MazeState m = (MazeState)state;
        return visited[m.getRow()][m.getColumn()];
    }

    /**
     * This method update a maze state as developed
     * @param state The maze state to update
     */
    @Override
    public void updateVisit(AState state){
        MazeState m = (MazeState)state;
        visited[m.getRow()][m.getColumn()] = true;
    }

    /**
     * This method initializes all maze states as undeveloped states
     */
    @Override
    public void resetVisited(){
        for (int i = 0;i < visited.length;i++){
            for (int j = 0;j < visited[0].length;j++){
                visited[i][j] = false;
            }
        }
    }

    /**
     * This function returns the start maze state
     * @return State of the start position
     */
    @Override
    public AState getStartState() {
        return startState;
    }

    /**
     * This function returns the goal maze state
     * @return State of the goal position
     */
    @Override
    public AState getGoalState() {
        return goalState;
    }

    /**
     * This function returns all maze states that can be reached from the input maze state
     * @param state The maze state from which we will examine
     * @return A list of all the maze states you can reach
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {

        ArrayList<AState> sol =new ArrayList<AState>();

        MazeState mState = (MazeState)state;
        int m_Row = mState.getRow();
        int m_Column = mState.getColumn();
        double m_Cost = mState.getCost();

        if(adeptMaze.onMatrix(m_Row - 1, m_Column)//up
                && adeptMaze.isAvailable(m_Row - 1, m_Column)){
            MazeState n = new MazeState(createStateString(m_Row - 1 ,m_Column),m_Cost + 1, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row - 1, m_Column + 1)//upper right diagonal
                && adeptMaze.isAvailable(m_Row - 1, m_Column + 1)
                && diagonalPath(m_Row, m_Column, m_Row - 1, m_Column + 1)){
            MazeState n = new MazeState(createStateString(m_Row - 1 ,m_Column + 1),m_Cost + 1.5, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row, m_Column + 1)//right
                && adeptMaze.isAvailable(m_Row,m_Column + 1)){
            MazeState n = new MazeState(createStateString(m_Row ,m_Column + 1),m_Cost + 1, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row + 1, m_Column + 1)//lower right diagonal
                && adeptMaze.isAvailable(m_Row + 1, m_Column + 1)
                && diagonalPath(m_Row, m_Column, m_Row + 1, m_Column + 1)){
            MazeState n = new MazeState(createStateString(m_Row + 1 ,m_Column + 1),m_Cost + 1.5, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row + 1, m_Column)//down
                && adeptMaze.isAvailable(m_Row + 1, m_Column)){
            MazeState n = new MazeState(createStateString(m_Row + 1, m_Column), m_Cost + 1, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row + 1, m_Column - 1)//lower left diagonal
                && adeptMaze.isAvailable(m_Row + 1, m_Column - 1)
                && diagonalPath(m_Row, m_Column, m_Row + 1, m_Column - 1)){
            MazeState n = new MazeState(createStateString(m_Row + 1 ,m_Column - 1),m_Cost + 1.5, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row, m_Column - 1)//left
                && adeptMaze.isAvailable(m_Row, m_Column - 1)){
            MazeState n = new MazeState(createStateString(m_Row ,m_Column - 1), m_Cost + 1, mState);
            sol.add(n);
        }

        if(adeptMaze.onMatrix(m_Row - 1, m_Column - 1)//upper left diagonal
                && adeptMaze.isAvailable(m_Row - 1, m_Column - 1)
                && diagonalPath(m_Row, m_Column, m_Row - 1, m_Column - 1)){
            MazeState n = new MazeState(createStateString(m_Row - 1 ,m_Column - 1),m_Cost + 1.5, mState);
            sol.add(n);
        }

        return sol;
    }

    /**
     * This function create a string from the position coordinate
     * @param row - the index of the position's row
     * @param column - the index of position's column
     * @return A position in string format
     */
    private String createStateString(int row, int column){
        return "{" + row+ "," + column+ "}";
    }

    /**
     * This function checks whether there is a valid diagonal path between two states
     * @param rowS - The row index of the initial state
     * @param columnS - The column index of the initial state
     * @param rowG - The row index of the target state
     * @param columnG - The column index of the target state
     * @return true if there is a valid diagonal path between the states or false if not
     */
    private boolean diagonalPath(int rowS, int columnS, int rowG, int columnG){
        if(rowS - 1 == rowG && columnS + 1 == columnG) {
            if(adeptMaze.onMatrix(rowS - 1, columnS) && adeptMaze.isAvailable(rowS - 1, columnS))
                return true;
            if(adeptMaze.onMatrix(rowS, columnS + 1) && adeptMaze.isAvailable(rowS, columnS + 1))
                return true;
        }
        else if(rowS + 1 == rowG && columnS + 1 == columnG) {
            if(adeptMaze.onMatrix(rowS, columnS + 1) && adeptMaze.isAvailable(rowS, columnS + 1))
                return true;
            if(adeptMaze.onMatrix(rowS + 1, columnS) && adeptMaze.isAvailable(rowS + 1, columnS))
                return true;
        }
        else if(rowS + 1 == rowG && columnS - 1 == columnG) {
            if(adeptMaze.onMatrix(rowS + 1, columnS) && adeptMaze.isAvailable(rowS + 1, columnS))
                return true;
            if(adeptMaze.onMatrix(rowS, columnS - 1) && adeptMaze.isAvailable(rowS, columnS - 1))
                return true;
        }
        else if(rowS - 1 == rowG && columnS - 1 == columnG) {
            if(adeptMaze.onMatrix(rowS - 1, columnS) && adeptMaze.isAvailable(rowS - 1, columnS))
                return true;
            if(adeptMaze.onMatrix(rowS, columnS - 1) && adeptMaze.isAvailable(rowS, columnS - 1))
                return true;
        }
        return false;
    }

}
