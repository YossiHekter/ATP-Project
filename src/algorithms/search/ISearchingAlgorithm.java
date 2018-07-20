package algorithms.search;

/**
 * The interface of search algorithms
 * @author Yossi Hekter
 */
public interface ISearchingAlgorithm {

    /**
     * This function solves the problem according to the algorithm
     * @param domain The object on which the search is being performed
     * @return domain solution
     */
    Solution solve(ISearchable domain);

    /**
     * This function returns the name of the search algorithm
     * @return String of search algorithm name
     */
    String getName();

    /**
     * This function returns the number of nodes developed during the solution
     * @return number of nodes developed
     */
    String getNumberOfNodesEvaluated();
}
