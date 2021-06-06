package TrafficNode;

import TrafficUser.EPriority;
import TrafficUser.ITrafficUser;
import TrafficUser.TrafficUser;

import java.util.*;
import java.util.logging.Logger;

public class TrafficNode implements ITrafficNode {
    private final Logger logger = Logger.getGlobal();

    Map<String, NeighborNodes> trafficNodes;
    Map<String, TrafficUserMock> trafficUserMap;
    String uuid;

    String defaultUUID;




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
                    status = "GREEN";
                    this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", status);
                    for (int i = 0; i < (int) (user.length * Math.random()); i++) {
                        TrafficUserMock temp = this.trafficUserMap.get(user[(int) (Math.random() * user.length)]);
                        if (temp != null) {
                            this.setNextTrafficNodeForUser(temp.getUuid());
                        }
                        this.sleep(100);
                    }
                    status = "RED";
                    this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", status);
                }
                this.sleep((long)(Math.random() * 5000));
            }
        }).start();
    }

    @Override
    public void signInTrafficNode(String trafficNodeUUID, double distance, double weight, boolean isDefault, String uuid) {
        logger.info("signIn: " + trafficNodeUUID);
        if (!this.trafficNodes.containsKey(trafficNodeUUID)) {
            this.registerOnNeighborTrafficNodes();
        }
        this.trafficNodes.put(trafficNodeUUID, new NeighborNodes(distance, weight, isDefault, uuid, null));
    }

    @Override
    public void signOutTrafficNode(String trafficNodeUUID) {
        logger.info("signOut: " + trafficNodeUUID);
        this.trafficNodes.remove(trafficNodeUUID);
    }

    @Override
    public void signInTrafficUser(String trafficUserUUID, String trafficUserNetworkString) {
        logger.info("signIn: " + trafficUserUUID);
        this.trafficUserMap.put(trafficUserUUID, new TrafficUserMock(trafficUserNetworkString));
        if (this.trafficUserMap.get(trafficUserUUID).getPriority().equals(EPriority.EMERGENCY)) {
            this.state = EPriority.EMERGENCY;
        }
        for (String key: this.trafficNodes.keySet()) {
            this.trafficNodeInvokeStub.setWorkload(ITrafficNode.class.getName() + "/" + key, this.uuid, this.trafficUserMap.size());
        }
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/mode", this.trafficUserMap.get(trafficUserUUID).getPriority().toString());
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
    }

    @Override
    public void signOutTrafficUser(String trafficUserUUID) {
        logger.info("signOut: " + trafficUserUUID);
        this.trafficUserMap.remove(trafficUserUUID);
        for (String key: this.trafficNodes.keySet()) {
            this.trafficNodeInvokeStub.setWorkload(ITrafficNode.class.getName() + "/" + key, this.uuid, this.trafficUserMap.size());
        }
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
    }

    @Override
    public void setTempo(String trafficUserUUID, double tempo) {
        logger.info("Tempo: " + tempo);
        if (this.trafficUserMap.get(trafficUserUUID) == null) {
            logger.warning("Receiver ID: " + this.uuid);
            logger.warning("Sender ID: " + trafficUserUUID);
            logger.warning("Receiver Neighbors: " + this.trafficUserMap.keySet().toString());
            return;
        }
        this.trafficUserMap.get(trafficUserUUID).setTempo(tempo);
        String Tempo = "" + tempo;
        Tempo = Tempo.substring(0, 5);
        trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/speed", trafficUserUUID + "/" + Tempo);
    }

    @Override
    public void setPriority(String trafficUserUUID, String priority) {
        logger.info("Priority: " + priority);
        if (priority.equals(EPriority.EMERGENCY.toString())) {
            this.state = EPriority.SUPER;
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", priority);
        }
        this.trafficUserMap.get(trafficUserUUID).setPriority(EPriority.valueOf(priority));
    }

    @Override
    public void setNextTrafficNode(String trafficUserUUID, String nextTrafficNode) {
        logger.info("NextTrafficNode: " + nextTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setNextTrafficNode(nextTrafficNode);
    }

    @Override
    public void setFinalTrafficNode(String trafficUserUUID, String finalTrafficNode) {
        logger.info("FinalTrafficNode: " + finalTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setFinalTrafficNode(finalTrafficNode);
    }

    @Override
    public void setWorkload(String trafficNodeUUID, int amount) {
        if (this.trafficNodes.get(trafficNodeUUID) == null) {
            logger.warning("Receiver ID: " + this.uuid);
            logger.warning("Sender ID: " + trafficNodeUUID);
            logger.warning("Receiver Neighbors: " + this.trafficNodes.keySet().toString());
            return;
        }
        this.trafficNodes.get(trafficNodeUUID).setAmount(amount);
    }

    public String getNextTrafficNode() {
        // TODO check default route
        String defaultRoute = "";
        Iterator<NeighborNodes> iterator = this.trafficNodes.values().iterator();
        while (iterator.hasNext()) {
            NeighborNodes next = iterator.next();
            if (next.isDefault && next.getsourceUUID().equals(this.uuid)) {
                defaultRoute = next.getDestinationUUID();
                break;
            }
        }
        String finalDefaultRoute = defaultRoute;

        Set<Map.Entry<String, TrafficUserMock>> entrySet = this.trafficUserMap.entrySet();
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

    private void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
