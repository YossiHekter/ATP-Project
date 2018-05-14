package algorithms.search;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class represents the Depth First Search algorithm
 * @author Roee Sanker & Yossi Hekter
 */
public class DepthFirstSearch extends ASearchingAlgorithm {

    /**
     * Constructor
     */
    public DepthFirstSearch(){ super(); }

    /**
     * This function solves the problem according to the Depth First Search algorithm
     * @param domain The object on which the search is being performed
     * @return domain solution
     */
    @Override
    public Solution solve(ISearchable domain) {
        Solution sol = new Solution();
        if(domain == null)
            return sol;
        AState startState = domain.getStartState();
        startState.setCost(0);
        AState goalState = domain.getGoalState();
        Stack<AState> stack = new Stack<AState>();
        domain.resetVisited();

        int counter = 0;

        stack.push(startState);
        domain.updateVisit(startState);
        while(!stack.isEmpty()){
            AState node = stack.pop();
            counter ++;
            if(node.equals(goalState)){
                sol.setGoal(node);
                setNumberOfNode(counter);
                return sol;
            }
            ArrayList<AState> successors = domain.getAllPossibleStates(node);
            for (AState state : successors){
                if(!domain.isVisit(state)){
                    domain.updateVisit(state);
                    stack.push(state);
                }
            }
        }
        setNumberOfNode(counter);
        return sol;
    }

    /**
     * This function returns the name of the search
     * @return String of search algorithm name
     */
    @Override
    public String getName() { return "DepthFirstSearch"; }


}
