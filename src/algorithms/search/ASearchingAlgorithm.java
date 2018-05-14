package algorithms.search;

/**
 * This class represents an abstract search algorithm
 * @author Roee Sanker & Yossi Hekter
 */
public abstract class ASearchingAlgorithm implements  ISearchingAlgorithm{

    /**
     * The number of nodes developed during the solution
     */
    private int numberOfNode;

    /**
     * The solution of the search algorithm
     */
    private Solution sol;

    /**
     * Constructor
     */
    public ASearchingAlgorithm(){};

    /**
     * This function returns the number of nodes developed during the solution
     * @return number of nodes developed
     */
    public String getNumberOfNodesEvaluated(){
        return "" + numberOfNode;
    }

    /**
     * This method updates the number of nodes developed during the solution
     * @param numberOfNode The updated number of nodes developed
     */
    protected void setNumberOfNode(int numberOfNode) {
        this.numberOfNode = numberOfNode;
    }
}
