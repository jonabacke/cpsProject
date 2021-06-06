package TrafficNode;

import Config.AmpelStatus;

public class NeighborNodes implements Comparable<NeighborNodes>
{
    private final String sourceUUID;
    private final String destinationUUID;
    private double distance;
    private double weight;
    private boolean isDefault;
    private int amount;
    private AmpelStatus status;
    private Double workloud;

    public NeighborNodes(double distance, double weight, boolean isDefault, String sourceUUID, String destinationUUID)
    {
        this.distance = distance;
        this.weight = weight;
        this.isDefault = isDefault;
        this.sourceUUID = sourceUUID;
        this.destinationUUID = destinationUUID;
        this.amount = 0;
        this.status = AmpelStatus.RED;
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
        this.workloud = amount * this.weight;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setStatus(String status) {
        this.status = AmpelStatus.valueOf(status);
    }

    public AmpelStatus getStatus() {
        return this.status;
    }

    public double getWorkloud() {
        return workloud;
    }

    @Override
    public int compareTo(NeighborNodes o) {
        return this.workloud.compareTo(o.workloud);
    }
}
