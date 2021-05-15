package TrafficNode;

public interface ITrafficNode {
    /**
     * Fügt ein Node hinzu
     * @param trafficNodeUUID, welches hinzugefügt werden soll
     */
    public void signInTrafficNode(String trafficNodeUUID);

    /**
     * entfernt ein Node, dass zb. defekt ist
     * @param trafficNodeUUID, welches entfernt werden soll
     */
    public void signOutTrafficNode(String trafficNodeUUID);


    public void signInTrafficUser(String trafficUserNetworkString, String trafficUserUUID);

    public void signOutTrafficUser(String trafficUserUUID);

    void setTempo(double tempo, String trafficUserUUID);

    void setPriority(String priority, String trafficUserUUID);

    void setNextTrafficNode(String nextTrafficNode, String trafficUserUUID);

    void setFinalTrafficNode(String finalTrafficNode, String trafficUserUUID);
}
