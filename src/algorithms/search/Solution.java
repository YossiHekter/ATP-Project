package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a solution to a problem
 * @author Roee Sanker & Yossi Hekter
 */
public class Solution implements Serializable {
    /**
     * The path solution of the problem
     */
    private ArrayList<AState> path;

    /**
     * Constructor
     */
    public Solution(){path = null;}

    /**
     * This function returns the path of all states from the start state to the goal state
     * @return List all states from start to goal
     */
    public ArrayList<AState> getSolutionPath(){
        return path;
    }

    /**
     * This method update the path solution
     * @param goal Goal state for the problem
     */
    protected void setGoal(AState goal) {
        if(goal != null) {
            path = new ArrayList<AState>();
            AState tmp = goal;
            while (tmp != null) {
                path.add(0, tmp);
                tmp = tmp.getPrev();
            }
        }
    }
}
