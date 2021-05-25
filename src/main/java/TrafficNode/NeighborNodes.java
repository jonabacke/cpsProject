package TrafficNode;

public class NeighborNodes
{
    private final String uuid;
    double distance;
    double weight;
    boolean isDefault;

    public NeighborNodes(double distance, double weight, boolean isDefault, String uuid)
    {
        this.distance = distance;
        this.weight = weight;
        this.isDefault = isDefault;
        this.uuid = uuid;
    }

    public double getDistance() {
        return distance;
    }

    public double getWeight() {
        return weight;
    }

    public String getUuid() {
        return uuid;
    }
}
