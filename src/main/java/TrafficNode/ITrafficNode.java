package TrafficNode;

import TrafficUser.ITrafficUser;

public interface ITrafficNode {
    /**
     * Fügt ein Node hinzu
     * @param trafficNode, welches hinzugefügt werden soll
     */
    public void addTrafficNode(TrafficNode trafficNode);

    /**
     * entfernt ein Node, dass zb. defekt ist
     * @param trafficNodeUuid, welches entfernt werden soll
     */
    public void deleteTrafficNode(String trafficNodeUuid);


    public void registerTrafficUser(ITrafficUser trafficUser);

    public void deleteTrafficUser(String uuid);

}
