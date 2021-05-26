package TrafficNode;

import TrafficUser.EPriority;
import TrafficUser.ITrafficUser;
import TrafficUser.TrafficUser;

import java.util.*;

public class TrafficNode implements ITrafficNode {

    Map<String, NeighborNodes> trafficNodes;
    Map<String, TrafficUserMock> trafficUserMap;
    String uuid;




    TrafficNodeInvokeStub trafficNodeInvokeStub;
    private EPriority state;

    public TrafficNode(String uuid, TrafficNodeInvokeStub trafficNodeInvokeStub, Map<String, NeighborNodes> trafficNodes) {
        this.trafficNodes = new HashMap<>();
        this.trafficUserMap = new HashMap<>();
        this.uuid = uuid;
        this.trafficNodeInvokeStub = trafficNodeInvokeStub;
        this.trafficNodes = trafficNodes;
    }

    public void init() {
        //this.registerOnNeighborTrafficNodes();
        this.test();
    }

    public void test() {
        new Thread(() -> {
            String status = "RED";
            while (true) {
                if (this.trafficNodes.size() > 0 && this.trafficUserMap.size() > 0) {
                    String [] user = this.trafficUserMap.keySet().toArray(new String[0]);
                    for (int i = 0; i < (int) (user.length * Math.random()); i++) {
                        TrafficUserMock temp = this.trafficUserMap.get(user[(int) (Math.random() * user.length)]);
                        if (temp != null) {
                            this.setNextTrafficNodeForUser(temp.getUuid());
                        }
                    }
                    this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", status);
                    if (status.equals("GREEN"))
                    {
                        status = "RED";
                    }
                    else
                    {
                        status = "GREEN";
                    }
                }
                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void signInTrafficNode(String trafficNodeUUID, double distance, double weight, boolean isDefault, String uuid) {
        System.out.println("signIn: " + trafficNodeUUID);
        if (!this.trafficNodes.containsKey(trafficNodeUUID)) {
            this.registerOnNeighborTrafficNodes();
        }
        this.trafficNodes.put(trafficNodeUUID, new NeighborNodes(distance, weight, isDefault, uuid, null));
    }

    @Override
    public void signOutTrafficNode(String trafficNodeUUID) {
        System.out.println("signOut: " + trafficNodeUUID);
        this.trafficNodes.remove(trafficNodeUUID);
    }

    @Override
    public void signInTrafficUser(String trafficUserUUID, String trafficUserNetworkString) {
        System.out.println("signIn: " + trafficUserUUID);
        this.trafficUserMap.put(trafficUserUUID, new TrafficUserMock(trafficUserNetworkString));
        if (this.trafficUserMap.get(trafficUserUUID).getPriority().equals(EPriority.EMERGENCY)) {
            this.state = EPriority.EMERGENCY;
        }
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/mode", this.trafficUserMap.get(trafficUserUUID).getPriority().toString());
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
    }

    @Override
    public void signOutTrafficUser(String trafficUserUUID) {
        System.out.println("signOut: " + trafficUserUUID);
        this.trafficUserMap.remove(trafficUserUUID);
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
    }

    @Override
    public void setTempo(String trafficUserUUID, double tempo) {
        //System.out.println("Tempo: " + tempo);
        this.trafficUserMap.get(trafficUserUUID).setTempo(tempo);
        String Tempo = "" + tempo;
        Tempo = Tempo.substring(0, 5);
        trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/speed", trafficUserUUID + "/" + Tempo);
    }

    @Override
    public void setPriority(String trafficUserUUID, String priority) {
        System.out.println("Priority: " + priority);
        if (priority.equals(EPriority.EMERGENCY.toString())) {
            this.state = EPriority.SUPER;
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", priority);
        }
        this.trafficUserMap.get(trafficUserUUID).setPriority(EPriority.valueOf(priority));
    }

    @Override
    public void setNextTrafficNode(String trafficUserUUID, String nextTrafficNode) {
        System.out.println("NextTrafficNode: " + nextTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setNextTrafficNode(nextTrafficNode);
    }

    @Override
    public void setFinalTrafficNode(String trafficUserUUID, String finalTrafficNode) {
        System.out.println("FinalTrafficNode: " + finalTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setFinalTrafficNode(finalTrafficNode);
    }

    public String getNextTrafficNode() {
        // TODO check default route
        String defaultRoute = "";
        Iterator<NeighborNodes> iterator = this.trafficNodes.values().iterator();
        while (iterator.hasNext()) {
            NeighborNodes next = iterator.next();
            if (next.isDefault) {
                defaultRoute = next.getDestinationUUID();
                break;
            }
        }
        String finalDefaultRoute = defaultRoute;
        /**
         *
        if (this.trafficNodes.get(defaultRoute).weight * this.trafficUserMap.entrySet().stream().filter(x -> x.getValue().getNextTrafficNode().equals(finalDefaultRoute)).count() > 100)
        {
            // TODO ErsatzWeg -> ask next Route with lowest weight

            return defaultRoute;
        }
        else
        {
            return defaultRoute;
        }
        // TODO if default full check alternative
         */
        return defaultRoute;
    }

    public void setNextTrafficNodeForUser(String trafficUserUUID) {
        this.trafficNodeInvokeStub.setNextTrafficNode(ITrafficUser.class.getName() + "/" + trafficUserUUID, this.getNextTrafficNode());
    }

    public void registerOnNeighborTrafficNodes() {
        //this.trafficNodeInvokeStub.signInTrafficNode(ITrafficNode.class.getName(), this.uuid, , );
    }

}
