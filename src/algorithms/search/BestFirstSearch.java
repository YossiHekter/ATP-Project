package algorithms.search;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class represents the Best First Search algorithm
 * @author Roee Sanker & Yossi Hekter
 */
public class BestFirstSearch extends ASearchingAlgorithm {

    /**
     * Constructor
     */
    public BestFirstSearch(){ super(); }

    /**
     * This function solves the problem according to the Best First Search algorithm
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
        PriorityQueue<AState> queue = new PriorityQueue<>();
        domain.resetVisited();

        int counter = 0;

        queue.add(startState);
        domain.updateVisit(startState);
        while(!queue.isEmpty()){
            AState node = queue.remove();
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
    public String getName() {
        return "BestFirstSearch";
    }
}
