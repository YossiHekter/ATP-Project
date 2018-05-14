package algorithms.search;

/**
 * This class represents an abstract state of a ISsearchable class
 * @author Roee Sanker & Yossi Hekter
 */
public abstract class AState implements Comparable<AState> {

    /**
     * Representing the state using a string
     */
    private String state;

    /**
     * Cost to reach the current state
     */
    private double cost;

    /**
     * The previous state from which we have reached the current state
     */
    private AState prev;

    //<editor-fold desc="Constructors">

    /**
     * Constructor
     * @param newState The new state
     */
    public AState(String newState){
        this.state =newState;
        this.cost = 0;
        this.prev =null;
    }

    /**
     * Constructor
     * @param newState The new state
     * @param newCost The cost of the new state
     * @param prev The state from which we reached the new state
     */
    public AState(String newState, double newCost, AState prev){
        this.state =newState;
        this.cost = newCost;
        this.prev = prev;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">

    /**
     * This function returns the string representing the current state
     * @return A string representing the state
     */
    public String getState() { return state; }

    /**
     * This function returns the cost to reach the state
     * @return The cost to reach
     */
    public double getCost() { return cost; }

    /**
     * This function returns the state from which we reached the current state
     * @return AState from which we reached the current state
     */
    public AState getPrev() { return prev; }
    //</editor-fold>

    //<editor-fold desc="Setters">

    /**
     * This method updates the cost to reach the current state
     * @param cost The new cost to update
     */
    protected void setCost(double cost) { this.cost = cost; }

    /**
     * This method updates the string representing the current state
     * @param state The new string to update
     */
    protected void setState(String state) { this.state = state; }

    /**
     * This method updates the state from which we reached the new state
     * @param prev The new state from which we reached the current state
     */
    protected void setPrev(AState prev) { this.prev = prev; }
    //</editor-fold>

    /**
     * This function compares two states according to their cost
     * @param otherState The state of comparison
     * @return 0 if the costs are equal, 1 if the cost of the current state is greater, -1 If the cost of the current state is smaller
     */
    public int compareTo(AState otherState){
        if(this.cost == otherState.cost)
            return 0;
        else if(this.cost > otherState.cost)
            return 1;
        else
            return -1;
    }

    /**
     * This function checks whether two states are identical
     * @param obj Comparative object
     * @return True If the objects are identical otherwise return false
     */
    @Override
    public boolean equals(Object obj){
        if(this==obj)
            return true;
        if(obj==null || getClass()!=obj.getClass())
            return false;
        AState otherState  = (AState)obj;
        return state != null ? state.equals(otherState.state) : otherState.state==null; //to check if its needed to compere cost and prev
    }

    /**
     * This function override the method hashCode
     * @return hashCode of string function
     */
    @Override
    public int hashCode(){
        return state!=null ? state.hashCode() : 0;
    }
}
