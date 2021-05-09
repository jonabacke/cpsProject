package TrafficNode;

import Config.ConfigFile;
import TrafficUser.ITrafficUser;
import TrafficUser.TrafficUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrafficNode {

    Map<String, TrafficNode> trafficNeighborNodeMap;
    Map<String, TrafficUser> trafficUserMap;
    String uuid;

    public TrafficNode() {
        this.trafficNeighborNodeMap = new HashMap<>();
        this.uuid = UUID.randomUUID().toString();
    }

    public TrafficNode(String uuid) {
        this.trafficNeighborNodeMap = new HashMap<>();
        this.uuid = uuid;
    }


    public void addTrafficNode(TrafficNode trafficNode) {
        this.trafficNeighborNodeMap.put(trafficNode.uuid, trafficNode);
    }

    public void deleteTrafficNode(String trafficNodeUuid) {
        this.trafficNeighborNodeMap.remove(trafficNodeUuid);
    }


    public TrafficNode[] getNeighborTrafficNode() {
        return this.getTrafficNeighborNodeMap().values().toArray(new TrafficNode[0]);
    }

    public Map<String, TrafficNode> getTrafficNeighborNodeMap() {
        return trafficNeighborNodeMap;
    }


    public void registerTrafficUser(TrafficUser trafficUser) {
        this.trafficUserMap.put(trafficUser.getUuid(), trafficUser);
    }


    public void deleteTrafficUser(String uuid) {
        this.trafficUserMap.remove(uuid);
    }

}
