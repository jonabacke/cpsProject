package TrafficNode;

public interface ITrafficNodeInvoke {

    /**
     * Fügt ein Node hinzu
     * @param trafficNodeUUID, welches hinzugefügt werden soll
     */
    public void addTrafficNode(String targetName, String trafficNodeUUID);

    /**
     * entfernt ein Node, dass zb. defekt ist
     * @param trafficNodeUUID, welches entfernt werden soll
     */
    public void deleteTrafficNode(String targetName, String trafficNodeUUID);
}
