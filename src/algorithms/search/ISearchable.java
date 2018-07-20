package algorithms.search;

import java.util.ArrayList;

/**
 * The interface of a searchable object
 * @author Yossi Hekter
 */
public interface ISearchable {

    /**
     * This function returns the start state
     * @return State of the starting position
     */
    AState getStartState();

    /**
     * This function returns the goal state
     * @return State of the goal position
     */
    AState getGoalState();

    /**
     * This function returns all states that can be reached from the input state
     * @param state The state from which we will examine
     * @return A list of all the states you can reach
     */
    ArrayList<AState> getAllPossibleStates(AState state);

    /**
     * This function checks whether we have already developed the state
     * @param state The state examined
     * @return True if the state is develop or false if not
     */
    boolean isVisit(AState state);

    /**
     * This function update a state as developed
     * @param state The state to update
     */
    void updateVisit(AState state);

    /**
     * This function initializes all states as undeveloped states
     */
    void resetVisited();
}
