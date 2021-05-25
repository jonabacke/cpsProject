package TrafficNode;

public interface ITrafficNodeInvoke {

    /**
     * Fügt ein Node hinzu
     * @param trafficNodeUUID, welches hinzugefügt werden soll
     * @param distance
     * @param weight
     * @param isDefault
     * @param uuid
     */
    public void signInTrafficNode(String targetName, String trafficNodeUUID, double distance, double weight, boolean isDefault, String uuid);

    /**
     * entfernt ein Node, dass zb. defekt ist
     * @param trafficNodeUUID, welches entfernt werden soll
     */
    public void signOutTrafficNode(String targetName, String trafficNodeUUID);
}
