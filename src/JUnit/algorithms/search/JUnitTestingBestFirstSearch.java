package algorithms.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JUnitTestingBestFirstSearch {

    @Test
    void solve() throws Exception{
        BestFirstSearch bfs = new BestFirstSearch();
        SearchableMaze maze = null;
        assertEquals(0, bfs.solve(maze).getSolutionPath().size());
    }

    @Test
    void getName() throws Exception {
        BestFirstSearch bfs = new BestFirstSearch();
        assertEquals("BestFirstSearch", bfs.getName());
    }
}