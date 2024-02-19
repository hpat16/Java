import java.util.ArrayList;
import java.util.List;

public class ShortestPathResultPlaceholder implements ShortestPathResult {
  /**
   * Gets the list of airports along the shortest route.
   */
  @Override
  public List<String> getRoute() {
    return new ArrayList<>();
  }

  /**
   * Gets the list of miles for each segment of the route.
   */
  @Override
  public List<Double> getSegmentMiles() {
    return new ArrayList<>();
  }

  /**
   * Gets the total miles for the entire route.
   */
  @Override
  public Double getTotalMiles() {
    return 0.0;
  }
}
