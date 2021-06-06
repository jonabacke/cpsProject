package TrafficNode;

import Config.AmpelStatus;
import Config.EPriority;
import TrafficUser.ITrafficUser;

import java.util.*;
import java.util.logging.Logger;

public class TrafficNode implements ITrafficNode {
    private final Logger logger = Logger.getGlobal();

    Map<String, NeighborNodes> trafficNodes;
    Map<String, TrafficUserMock> trafficUserMap;
    String uuid;

    String defaultRoute;




    TrafficNodeInvokeStub trafficNodeInvokeStub;
    private EPriority state;
    private List<NeighborNodes> inStreamNodes;

    public TrafficNode(String uuid, TrafficNodeInvokeStub trafficNodeInvokeStub, Map<String, NeighborNodes> trafficNodes) {
        this.trafficNodes = new HashMap<>();
        this.trafficUserMap = new HashMap<>();
        this.uuid = uuid;
        this.trafficNodeInvokeStub = trafficNodeInvokeStub;
        this.trafficNodes = trafficNodes;
        this.defaultRoute = this.getDefaultRoute();
    }

    public void init() {
        //this.registerOnNeighborTrafficNodes();
        this.test();
    }

    public void test() {
        new Thread(() -> {
            String status = "RED";
            while (true) {
//                if (this.trafficNodes.size() > 0 && this.trafficUserMap.size() > 0) {
//                    String [] user = this.trafficUserMap.keySet().toArray(new String[0]);
//                    status = "GREEN";
//                    this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", status);
//                    for (int i = 0; i < (int) (user.length * Math.random()); i++) {
//                        TrafficUserMock temp = this.trafficUserMap.get(user[(int) (Math.random() * user.length)]);
//                        if (temp != null) {
//                            this.setNextTrafficNodeForUser(temp.getUuid());
//                        }
//                        this.sleep(100);
//                    }
//                    status = "RED";
//                    this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", status);
//                }
//                this.sleep((long)(Math.random() * 5000));

                if (this.trafficNodes.size() > 0 && this.trafficUserMap.size() > 0) {
                    String [] user = this.trafficUserMap.keySet().toArray(new String[0]);
                    for (String u: user) {
                        TrafficUserMock userMock = this.trafficUserMap.get(u);
                        userMock.refreshDistance();
                        NeighborNodes lastNode = this.trafficNodes.get(userMock.getLastTrafficNode());
                        if (lastNode.getDistance() - userMock.getDistance() < 1) {
                            if (lastNode.getStatus().equals(AmpelStatus.RED) && userMock.getPriority().equals(EPriority.NORMAL)) {
                                this.trafficNodeInvokeStub.setTempo(ITrafficUser.class.getName() + "/" + u, 0);
                            } else if (lastNode.getStatus().equals(AmpelStatus.GREEN)) {
                                this.setNextTrafficNodeForUser(u);
                            } else if (userMock.getPriority().equals(EPriority.EMERGENCY)) {
                                this.setNextTrafficNodeForUser(u);
                            }
                        } else {
                            this.trafficNodeInvokeStub.setTempo(ITrafficUser.class.getName() + "/" + u, 16);
                        }
                    }
                }
                sleep(100);
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

    @Override
    public void setStatus(String nodeUUID, String status) {
        this.trafficNodes.get(nodeUUID).setStatus(status);
    }

    private String getDefaultRoute() {
        String defaultRoute = "";
        this.inStreamNodes = new ArrayList<>();
        Iterator<NeighborNodes> iterator = this.trafficNodes.values().iterator();
        while (iterator.hasNext()) {
            NeighborNodes next = iterator.next();
            if (next.getIsDefault() && next.getsourceUUID().equals(this.uuid) && defaultRoute.equalsIgnoreCase("")) {
                defaultRoute = next.getDestinationUUID();
            }
            if (next.getDestinationUUID().equals(this.uuid)) {
                this.inStreamNodes.add(next);
            }
        }
        return defaultRoute;
    }


    public String getNextTrafficNode() {

        String finalDefaultRoute = this.defaultRoute;

        Set<Map.Entry<String, TrafficUserMock>> entrySet = this.trafficUserMap.entrySet();

        if (this.trafficNodes.get(finalDefaultRoute).getWorkloud() >= 20)
        {
            // TODO ErsatzWeg -> ask next Route with lowest weight

            Iterator<NeighborNodes> iterator = this.trafficNodes.values().iterator();
            while (iterator.hasNext()) {
                NeighborNodes next = iterator.next();
                if (next.getWeight() * next.getAmount() < 20) {
                    finalDefaultRoute = next.getDestinationUUID();
                }
                if (next.getDestinationUUID().equals(this.uuid)) {
                    this.inStreamNodes.add(next);
                }
            }
            if (finalDefaultRoute.equalsIgnoreCase(this.defaultRoute)) {
                finalDefaultRoute = this.trafficNodes.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get().getKey();
            }
            return finalDefaultRoute;
        }
        else
        {
            return finalDefaultRoute;
        }
        // TODO if default full check alternative

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
