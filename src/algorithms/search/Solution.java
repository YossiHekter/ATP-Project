package algorithms.search;

import java.util.ArrayList;

/**
 * This class represents a solution to a problem
 * @author Roee Sanker & Yossi Hekter
 */
public class Solution {
    /**
     * The goal state of the problem
     */
    private AState goal;

    /**
     * Constructor
     */
    public Solution(){ goal =null; }

    /**
     * This function returns the goal state
     * @return The goal state of the solution or NULL if no solution is found
     */
    public AState getGoal() { return goal; }

    /**
     * This method update the goal state
     * @param goal Goal state to update
     */
    protected void setGoal(AState goal) { this.goal = goal; }

    /**
     * This function returns the path of all states from the start state to the goal state
     * @return List all states from start to goal
     */
    public ArrayList<AState> getSolutionPath(){
        ArrayList<AState> path = new ArrayList<AState>();
        if(this.goal != null) {
            AState tmp = goal;
            while (tmp != null) {
                path.add(0, tmp);
                tmp = tmp.getPrev();
            }
        }
        return path;
    }

}
