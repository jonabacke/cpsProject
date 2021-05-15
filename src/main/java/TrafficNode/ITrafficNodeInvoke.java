package TrafficNode;

public interface ITrafficNodeInvoke {

    /**
     * Fügt ein Node hinzu
     * @param trafficNodeUUID, welches hinzugefügt werden soll
     */
    public void signInTrafficNode(String targetName, String trafficNodeUUID);

    /**
     * entfernt ein Node, dass zb. defekt ist
     * @param trafficNodeUUID, welches entfernt werden soll
     */
    public void signOutTrafficNode(String targetName, String trafficNodeUUID);
}
