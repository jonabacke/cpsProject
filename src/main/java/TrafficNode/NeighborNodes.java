package TrafficNode;

public class NeighborNodes
{
    private final String sourceUUID;
    private final String destinationUUID;
    double distance;
    double weight;
    boolean isDefault;
    int amount;

    public NeighborNodes(double distance, double weight, boolean isDefault, String sourceUUID, String destinationUUID)
    {
        this.distance = distance;
        this.weight = weight;
        this.isDefault = isDefault;
        this.sourceUUID = sourceUUID;
        this.destinationUUID = destinationUUID;
        this.amount = 0;
    }

    public double getDistance() {
        return distance;
    }

    public double getWeight() {
        return weight;
    }

    public String getsourceUUID() {
        return sourceUUID;
    }

    public String getDestinationUUID() {
        return destinationUUID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
