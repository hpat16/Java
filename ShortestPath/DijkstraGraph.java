import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     * @param map the map that the graph uses to map a data object to the node
     *        object it is stored in
     */
    public DijkstraGraph(MapADT<NodeType, Node> map) {
        super(map);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // if start or end do not correspond to a graph node, throw NoSuchElementException
        if (!containsNode(start) || !containsNode(end)) {
            throw new NoSuchElementException(
                start + " or " +  end + " data does not correspond to existing graph nodes.");
        }
        // create a priority queue to store shorter path possibilities
        PriorityQueue<SearchNode> runningPaths = new PriorityQueue<>();
        // keeps track of the nodes that have already been visited (and found the shortest path
        // reaching them from the start node)
        PlaceholderMap<Node, SearchNode> visitedNodes = new PlaceholderMap<>();
        // insert the start node into priority queue
        runningPaths.add(new SearchNode(nodes.get(start), 0.0, null));
        while (!runningPaths.isEmpty()) {
            SearchNode current = runningPaths.poll();
            // if the node polled from pqueue is the end node, then no need to continue, so return
            // the SearchNode of it because that is the shortest path
            if (current.node.data.equals(end)) {
                return current;
            }
            // if the current node hasn't already been visited, then add current to visitedNodes map
            else if (!visitedNodes.containsKey(current.node)) {
                visitedNodes.put(current.node, current);
                // for each successor that this node is directed to, add that successor to the
                // pqueue if it hasn't already been visited (found shortest path for)
                for (Edge edge : current.node.edgesLeaving) {
                    // calculate cost to include previous cost as well
                    double currentPathCost = edge.data.doubleValue() + current.cost;
                    SearchNode successor = new SearchNode(edge.successor, currentPathCost, current);
                    if (!visitedNodes.containsKey(successor.node)) {
                        runningPaths.add(successor);
                    }
                }
            }
        }
        // if program reaches this point, there is no path from start to end
        throw new NoSuchElementException("No path from " + start + " to " + end + " was found.");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        List<NodeType> data = new LinkedList<>();
        SearchNode current = computeShortestPath(start, end);
        // since this is adding to the front each time, the reverse SongNodes will be arranged in
        // the correct positions ultimately
        while (current != null) {
            data.add(0, current.node.data);
            current = current.predecessor;
        }

        return data;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        return computeShortestPath(start, end).cost;
    }

    //: implement 3+ tests in step 4.1
    /**
     * This test makes use of an example that we traced through in lecture, and confirms that the
     * results of my implementation match what we previously computed by hand. Specially, it checks
     * the shortest path from Node D to other nodes.
     */
    @Test
    public void checkLectureShortestPaths() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertNode("I");
        graph.insertNode("L");
        graph.insertNode("M");
        // adding edges with same weights as example from lecture
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "H", 8);
        graph.insertEdge("A", "M", 5);
        graph.insertEdge("B", "M", 3);
        graph.insertEdge("D", "A", 7);
        graph.insertEdge("D", "G", 2);
        graph.insertEdge("F", "G", 9);
        graph.insertEdge("G", "L", 7);
        graph.insertEdge("H", "B", 6);
        graph.insertEdge("H", "I", 2);
        graph.insertEdge("I", "D", 1);
        graph.insertEdge("I", "L", 5);
        graph.insertEdge("I", "H", 2);
        graph.insertEdge("M", "E", 3);
        graph.insertEdge("M", "F", 4);
        // checks that the shortest paths from D to all other nodes, and their costs are as expected
        Assertions.assertEquals("[D]", graph.shortestPathData("D", "D").toString());
        Assertions.assertEquals(0, graph.shortestPathCost("D", "D"));
        Assertions.assertEquals("[D, G]", graph.shortestPathData("D", "G").toString());
        Assertions.assertEquals(2, graph.shortestPathCost("D", "G"));
        Assertions.assertEquals("[D, A]", graph.shortestPathData("D", "A").toString());
        Assertions.assertEquals(7, graph.shortestPathCost("D", "A"));
        Assertions.assertEquals("[D, A, B]", graph.shortestPathData("D", "B").toString());
        Assertions.assertEquals(8, graph.shortestPathCost("D", "B"));
        Assertions.assertEquals("[D, G, L]", graph.shortestPathData("D", "L").toString());
        Assertions.assertEquals(9, graph.shortestPathCost("D", "L"));
        Assertions.assertEquals("[D, A, B, M]", graph.shortestPathData("D", "M").toString());
        Assertions.assertEquals(11, graph.shortestPathCost("D", "M"));
        Assertions.assertEquals("[D, A, B, M, E]", graph.shortestPathData("D", "E").toString());
        Assertions.assertEquals(14, graph.shortestPathCost("D", "E"));
        Assertions.assertEquals("[D, A, B, M, F]", graph.shortestPathData("D", "F").toString());
        Assertions.assertEquals(15, graph.shortestPathCost("D", "F"));
        Assertions.assertEquals("[D, A, H]", graph.shortestPathData("D", "H").toString());
        Assertions.assertEquals(15, graph.shortestPathCost("D", "H"));
        Assertions.assertEquals("[D, A, H, I]", graph.shortestPathData("D", "I").toString());
        Assertions.assertEquals(17, graph.shortestPathCost("D", "I"));
    }

    /**
     * This test uses the same graph used in the previous testers, but checks the cost and sequence
     * of data along the shortest path between a different start and end node. Specially, it checks
     * the shortest path/cost from Node A to other nodes.
     */
    @Test
    public void checkOtherShortestPaths() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertNode("I");
        graph.insertNode("L");
        graph.insertNode("M");
        // adding edges with same weights as example from lecture
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "H", 8);
        graph.insertEdge("A", "M", 5);
        graph.insertEdge("B", "M", 3);
        graph.insertEdge("D", "A", 7);
        graph.insertEdge("D", "G", 2);
        graph.insertEdge("F", "G", 9);
        graph.insertEdge("G", "L", 7);
        graph.insertEdge("H", "B", 6);
        graph.insertEdge("H", "I", 2);
        graph.insertEdge("I", "D", 1);
        graph.insertEdge("I", "L", 5);
        graph.insertEdge("I", "H", 2);
        graph.insertEdge("M", "E", 3);
        graph.insertEdge("M", "F", 4);
        // checks that the shortest paths from A to all other nodes, and their costs are as expected
        Assertions.assertEquals("[A, B]", graph.shortestPathData("A", "B").toString());
        Assertions.assertEquals(1, graph.shortestPathCost("A", "B"));
        Assertions.assertEquals("[A, B, M]", graph.shortestPathData("A", "M").toString());
        Assertions.assertEquals(4, graph.shortestPathCost("A", "M"));
        Assertions.assertEquals("[A, B, M, E]", graph.shortestPathData("A", "E").toString());
        Assertions.assertEquals(7, graph.shortestPathCost("A", "E"));
        Assertions.assertEquals("[A, H]", graph.shortestPathData("A", "H").toString());
        Assertions.assertEquals(8, graph.shortestPathCost("A", "H"));
        Assertions.assertEquals("[A, B, M, F]", graph.shortestPathData("A", "F").toString());
        Assertions.assertEquals(8, graph.shortestPathCost("A", "F"));
        Assertions.assertEquals("[A, H, I]", graph.shortestPathData("A", "I").toString());
        Assertions.assertEquals(10, graph.shortestPathCost("A", "I"));
        Assertions.assertEquals("[A, H, I, D]", graph.shortestPathData("A", "D").toString());
        Assertions.assertEquals(11, graph.shortestPathCost("A", "D"));
        Assertions.assertEquals("[A, H, I, D, G]", graph.shortestPathData("A", "G").toString());
        Assertions.assertEquals(13, graph.shortestPathCost("A", "G"));
        Assertions.assertEquals("[A, H, I, L]", graph.shortestPathData("A", "L").toString());
        Assertions.assertEquals(15, graph.shortestPathCost("A", "L"));
    }

    /**
     * This test makes use of the same graph from the previous testers, but it specially checks for
     * the cases where we are searching for the shortest path between two existing nodes, but there
     * is actually no path at all that connects those two nodes
     */
    @Test
    public void checkInvalidShortestPaths() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertNode("I");
        graph.insertNode("L");
        graph.insertNode("M");
        // adding edges with same weights as example from lecture
        graph.insertEdge("A", "B", 1);
        graph.insertEdge("A", "H", 8);
        graph.insertEdge("A", "M", 5);
        graph.insertEdge("B", "M", 3);
        graph.insertEdge("D", "A", 7);
        graph.insertEdge("D", "G", 2);
        graph.insertEdge("F", "G", 9);
        graph.insertEdge("G", "L", 7);
        graph.insertEdge("H", "B", 6);
        graph.insertEdge("H", "I", 2);
        graph.insertEdge("I", "D", 1);
        graph.insertEdge("I", "L", 5);
        graph.insertEdge("I", "H", 2);
        graph.insertEdge("M", "E", 3);
        graph.insertEdge("M", "F", 4);
        // no path connects L and A, so expect NoSuchElementException to be thrown
        try {
            graph.computeShortestPath("L", "A");
            Assertions.fail(); // fails if no exception was thrown
        } catch (NoSuchElementException e) { // expect this exception to be thrown
            Assertions.assertEquals("No path from L to A was found.", e.getMessage());
        } catch (Exception e) {
            Assertions.fail(); // fail if different exception was thrown
        }
        // no path connects E and I, so expect NoSuchElementException to be thrown
        try {
            graph.computeShortestPath("E", "I");
            Assertions.fail(); // fails if no exception was thrown
        } catch (NoSuchElementException e) { // expect this exception to be thrown
            Assertions.assertEquals("No path from E to I was found.", e.getMessage());
        } catch (Exception e) {
            Assertions.fail(); // fail if different exception was thrown
        }
        // no path connects M and D, so expect NoSuchElementException to be thrown
        try {
            graph.computeShortestPath("M", "D");
            Assertions.fail(); // fails if no exception was thrown
        } catch (NoSuchElementException e) { // expect this exception to be thrown
            Assertions.assertEquals("No path from M to D was found.", e.getMessage());
        } catch (Exception e) {
            Assertions.fail(); // fail if different exception was thrown
        }
    }

    /**
     * This test tests the scenario where there are no edges in the graph (only Nodes)
     */
    @Test
    public void checkAdditionalInvalidCalls() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("A");
        graph.insertNode("B");

        try {
            graph.computeShortestPath("A", "B");
            Assertions.fail(); // fail if no exception was thrown
        } catch (NoSuchElementException e) { // expect this exception to be thrown
            Assertions.assertEquals("No path from A to B was found.", e.getMessage());
        } catch (Exception e) {
            Assertions.fail(); // fail if different exception was thrown
        }
    }
}
