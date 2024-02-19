import java.util.List;
/**
 * Interface for the results of the shortest path search.
 */
public interface ShortestPathResult {
  /**
   * Constructor: This file initialize the ShortestPathResult every implementation of ShortestPathResult is expected to
   * have a constructor with the following parameters:
   * @param route       - The route of the flight.
   * @param segmentMiles - the miles to travel for each segments of the route      
   * @param totalMiles   - the total miles for the route
   */
  /*
      public ShortestPathResult(List<String> route, List<Double> segmentMiles, Double totalMiles);
      this.route = route;
      this.segmentMiles = segmentMiles;
      this.totalMiles = totalMiles;
  }
   */

  
    /**
     * Gets the list of airports along the shortest route.
     *
     */
    List<String> getRoute();

    /**
     * Gets the list of miles for each segment of the route.
     *
     */
    List<Double> getSegmentMiles();

    /**
     * Gets the total miles for the entire route.
     *
     */
    Double getTotalMiles();
}

