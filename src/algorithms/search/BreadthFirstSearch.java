package algorithms.search;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class represents the Breadth First Search algorithm
 * @author Roee Sanker & Yossi Hekter
 */
public class BreadthFirstSearch extends ASearchingAlgorithm{

    /**
     * Constructor
     */
    public BreadthFirstSearch(){ super(); }

    /**
     * This function solves the problem according to the Breadth First Search algorithm
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
        Vector<AState> queue = new Vector<AState>();
        int counter = 0;
        domain.resetVisited();

        queue.add(startState);
        domain.updateVisit(startState);
        while(!queue.isEmpty()){
            AState node = queue.remove(0);
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
                    queue.add(state);
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
    public String getName() { return "BreadthFirstSearch"; }
}
