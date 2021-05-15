package TrafficNode;

import TrafficUser.EPriority;
import TrafficUser.ITrafficUser;

import java.util.*;

public class TrafficNode implements ITrafficNode {

    Set<String> trafficNodes;
    Map<String, TrafficUserMock> trafficUserMap;
    String uuid;

    TrafficNodeInvokeStub trafficNodeInvokeStub;

    public TrafficNode(String uuid, TrafficNodeInvokeStub trafficNodeInvokeStub) {
        this.trafficNodes = new HashSet<>();
        this.trafficUserMap = new HashMap<>();
        this.uuid = uuid;
        this.trafficNodeInvokeStub = trafficNodeInvokeStub;
    }

    public void init() {
        this.registerOnNeighborTrafficNodes();
        this.test();
    }

    public void test() {
        new Thread(() -> {
            while (true) {
                if (this.trafficNodes.size() > 0 && this.trafficUserMap.size() > 0) {
                    String [] user = this.trafficUserMap.keySet().toArray(new String[0]);
                    this.setNextTrafficNodeForUser(this.trafficUserMap.get(user[(int) (Math.random() * user.length)]).getUuid());
                }
                try {
                    Thread.sleep((int) (Math.random() * 10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void signInTrafficNode(String trafficNodeUUID) {
        System.out.println("signIn: " + trafficNodeUUID);
        if (!this.trafficNodes.contains(trafficNodeUUID)) {
            this.registerOnNeighborTrafficNodes();
        }
        this.trafficNodes.add(trafficNodeUUID);
    }

    @Override
    public void signOutTrafficNode(String trafficNodeUUID) {
        System.out.println("signOut: " + trafficNodeUUID);
        this.trafficNodes.remove(trafficNodeUUID);
    }

    @Override
    public void signInTrafficUser(String trafficUserNetworkString, String trafficUserUUID) {
        System.out.println("signIn: " + trafficUserUUID);
        this.trafficUserMap.put(trafficUserUUID, new TrafficUserMock(trafficUserNetworkString));
    }

    @Override
    public void signOutTrafficUser(String trafficUserUUID) {
        System.out.println("signOut: " + trafficUserUUID);
        this.trafficUserMap.remove(trafficUserUUID);
    }

    @Override
    public void setTempo(double tempo, String trafficUserUUID) {
        System.out.println("Tempo: " + tempo);
        this.trafficUserMap.get(trafficUserUUID).setTempo(tempo);
    }

    @Override
    public void setPriority(String priority, String trafficUserUUID) {
        System.out.println("Priority: " + priority);
        this.trafficUserMap.get(trafficUserUUID).setPriority(EPriority.valueOf(priority));
    }

    @Override
    public void setNextTrafficNode(String nextTrafficNode, String trafficUserUUID) {
        System.out.println("NextTrafficNode: " + nextTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setNextTrafficNode(nextTrafficNode);
    }

    @Override
    public void setFinalTrafficNode(String finalTrafficNode, String trafficUserUUID) {
        System.out.println("FinalTrafficNode: " + finalTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setFinalTrafficNode(finalTrafficNode);
    }

    public String getNextTrafficNode() {
        String [] nodes = this.trafficNodes.toArray(new String[0]);
        return nodes[(int) (Math.random() * nodes.length)];
    }

    public void setNextTrafficNodeForUser(String trafficUserUUID) {
        this.trafficNodeInvokeStub.setNextTrafficNode(ITrafficUser.class.getName() + "/" + trafficUserUUID, this.getNextTrafficNode());
    }

    public void registerOnNeighborTrafficNodes() {
        this.trafficNodeInvokeStub.signInTrafficNode(ITrafficNode.class.getName(), this.uuid);
    }

}
