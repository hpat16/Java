import java.util.List;

public class ShortestPath implements ShortestPathResult {
    private List<String> route;
    private List<Double> segmentMiles;
    private Double totalMiles;

    public ShortestPath(List<String> route, List<Double> segmentMiles, Double totalMiles) {
        this.route = route;
        this.segmentMiles = segmentMiles;
        this.totalMiles = totalMiles;
    }

    @Override
    public List<String> getRoute() {
        return route;
    }

    @Override
    public List<Double> getSegmentMiles() {
        return segmentMiles;
    }

    @Override
    public Double getTotalMiles() {
        return totalMiles;
    }
}