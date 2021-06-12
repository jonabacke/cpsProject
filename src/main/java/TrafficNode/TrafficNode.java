package TrafficNode;

import Config.ConfigFile;
import Config.TrafficLightState;
import Config.EPriority;
import ControlUnit.FSM.ControlUnit;
import TrafficUser.ITrafficUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class TrafficNode implements ITrafficNode {
    private final Logger logger = Logger.getGlobal();

    ConcurrentMap<String, NeighborNodes> trafficNodesComing;
    ConcurrentMap<String, NeighborNodes> trafficNodesGoing;
    ConcurrentMap<String, TrafficUserMock> trafficUserMap;
    String uuid;

    String defaultRoute;

    TrafficNodeInvokeStub trafficNodeInvokeStub;

    public TrafficNode(String uuid, TrafficNodeInvokeStub trafficNodeInvokeStub, ConcurrentMap<String, NeighborNodes> trafficNodesComing, ConcurrentMap<String, NeighborNodes> trafficNodesGoing) {
        this.trafficNodesComing = new ConcurrentHashMap<>();
        this.trafficUserMap = new ConcurrentHashMap<>();
        this.uuid = uuid;
        this.trafficNodeInvokeStub = trafficNodeInvokeStub;
        this.trafficNodesComing = trafficNodesComing;
        this.trafficNodesGoing = trafficNodesGoing;
        this.defaultRoute = this.getDefaultRoute();
        new ControlUnit(trafficNodesComing);

    }

    public void init() {
        //this.registerOnNeighborTrafficNodes();
        this.test();
    }

    public synchronized void test() {
        new Thread(() -> {
            double priorityDistance = 0;
            while (true) {
                if (this.trafficNodesComing.size() > 0 && this.trafficUserMap.size() > 0) {
                    String [] user = this.trafficUserMap.keySet().toArray(new String[0]);
                    priorityDistance = Double.MAX_VALUE;
                    for (TrafficUserMock userMock: this.trafficUserMap.values()) {
                        if (userMock.getPriority().equals(EPriority.EMERGENCY) && userMock.getDistance() < priorityDistance) {
                            priorityDistance = userMock.getDistance();
                        }
                    }
                    for (String u: user) {
                        TrafficUserMock userMock = this.trafficUserMap.get(u);
                        userMock.refreshDistance();
                        NeighborNodes lastNode = this.trafficNodesComing.get(userMock.getLastTrafficNode());
                        // Stay if self !Priority but priority is behind
                        if (userMock.getPriority().equals(EPriority.NORMAL) && lastNode.getControlTrafficLight().getCurrentState().toString().equals(ConfigFile.PRIO_MESSAGE) && userMock.getDistance() > priorityDistance) {
                            this.stop(u); // Stehen bleiben und Prio vor lassen
                        } else if (lastNode.getDistance() - userMock.getDistance() < 2) {
                            // Stay at RED
                            if (lastNode.getStatus().equals(TrafficLightState.RED) && userMock.getPriority().equals(EPriority.NORMAL)) {
                                this.stop(u);
                            }
                             else if (lastNode.getStatus().equals(TrafficLightState.GREEN)) {
                                this.setNextTrafficNodeForUser(u);
                            } else if (userMock.getPriority().equals(EPriority.EMERGENCY)) {
                                this.setNextTrafficNodeForUser(u);
                            }
                        } else {
                            if (userMock.getPriority().equals(EPriority.EMERGENCY)) {
                                this.drive(u, 14);
                            } else {
                                this.drive(u, this.calcTempo(lastNode));
                            }
                        }
                    }
                }
                sleep(100);
            }
        }).start();
    }

    private synchronized double calcTempo(NeighborNodes node) {
        double result = 0;
        if (node.getWorkload() < 2) {
            result = 14;
        } else if (node.getWorkload() < 4) {
            result = 10;
        } else if (node.getWorkload() < 6) {
            result = 8;
        } else if (node.getWorkload() < 8) {
            result = 6;
        } else if (node.getWorkload() < 10) {
            result = 4;
        } else {
            result = 2;
        }
        return result;
    }

    // TODO Check if its not needed
    @Override
    public synchronized void signInTrafficNode(String trafficNodeUUID, double distance, double weight, boolean isDefault, String uuid) {
        logger.info("signIn: " + trafficNodeUUID);
        if (!this.trafficNodesComing.containsKey(trafficNodeUUID)) {
            this.registerOnNeighborTrafficNodes();
        }
        this.trafficNodesComing.put(trafficNodeUUID, new NeighborNodes(this.uuid, this.trafficNodeInvokeStub, distance, weight, isDefault, uuid, null));
    }

    // TODO Check if its waste
    @Override
    public synchronized void signOutTrafficNode(String trafficNodeUUID) {
        logger.info("signOut: " + trafficNodeUUID);
        this.trafficNodesComing.remove(trafficNodeUUID);
    }

    @Override
    public synchronized void signInTrafficUser(String trafficUserUUID, String trafficUserNetworkString) {
        logger.info("signIn: " + trafficUserUUID);

        TrafficUserMock trafficUser = new TrafficUserMock(trafficUserNetworkString);

        this.trafficUserMap.put(trafficUserUUID, trafficUser);

        if (this.trafficUserMap.get(trafficUserUUID).getPriority().equals(EPriority.EMERGENCY)) {
            this.trafficNodesComing.get(trafficUser.getLastTrafficNode()).setPriority(trafficUser.getTempo());
        }

        this.trafficNodesComing.get(trafficUser.getLastTrafficNode()).incrementAmount();


        for (String key: this.trafficNodesComing.keySet()) {
            this.trafficNodeInvokeStub.setWorkload(ITrafficNode.class.getName() + "/" + key, this.uuid, this.trafficUserMap.size());
        }

        // Frontend
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/mode", this.trafficUserMap.get(trafficUserUUID).getPriority().toString());
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
    }

    @Override
    public synchronized void signOutTrafficUser(String trafficUserUUID) {
        logger.info("signOut: " + trafficUserUUID);
        if (this.trafficUserMap.get(trafficUserUUID).getPriority().equals(EPriority.EMERGENCY)) {
            this.trafficNodesComing.get(this.trafficUserMap.get(trafficUserUUID).getLastTrafficNode()).decrementPriority();
        }
        this.trafficNodesComing.get(this.trafficUserMap.get(trafficUserUUID).getLastTrafficNode()).decrementAmount();
        this.trafficUserMap.remove(trafficUserUUID);
        for (String key: this.trafficNodesComing.keySet()) {
            this.trafficNodeInvokeStub.setWorkload(ITrafficNode.class.getName() + "/" + key, this.uuid, this.trafficUserMap.size());
        }
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
    }

    @Override
    public synchronized void setTempo(String trafficUserUUID, double tempo) {
        logger.info("Tempo: " + tempo);
        if (this.trafficUserMap.get(trafficUserUUID) == null) {
            logger.warning("Receiver ID: " + this.uuid);
            logger.warning("Sender ID: " + trafficUserUUID);
            logger.warning("Receiver Neighbors: " + this.trafficUserMap.keySet().toString());
            return;
        }
        this.trafficUserMap.get(trafficUserUUID).setTempo(tempo);
        String Tempo = "" + tempo;
        Tempo = Tempo.substring(0, Math.min(5, Tempo.length()));
        trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/speed", trafficUserUUID + "/" + Tempo);
    }

    @Override
    public synchronized void setPriority(String trafficUserUUID, String priority) {
        logger.info("Priority: " + priority);
        if (priority.equals(EPriority.EMERGENCY.toString())) {
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", priority);
        }
        this.trafficUserMap.get(trafficUserUUID).setPriority(EPriority.valueOf(priority));
    }

    @Override
    public synchronized void setNextTrafficNode(String trafficUserUUID, String nextTrafficNode) {
        logger.info("NextTrafficNode: " + nextTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setNextTrafficNode(nextTrafficNode);
    }

    @Override
    public synchronized void setFinalTrafficNode(String trafficUserUUID, String finalTrafficNode) {
        logger.info("FinalTrafficNode: " + finalTrafficNode);
        this.trafficUserMap.get(trafficUserUUID).setFinalTrafficNode(finalTrafficNode);
    }

    @Override
    public synchronized void setWorkload(String trafficNodeUUID, int amount) {
        if (this.trafficNodesGoing.get(trafficNodeUUID) == null) {
            logger.warning("Receiver ID: " + this.uuid);
            logger.warning("Sender ID: " + trafficNodeUUID);
            logger.warning("Receiver Neighbors: " + this.trafficNodesGoing.keySet().toString());
            return;
        }
        this.trafficNodesGoing.get(trafficNodeUUID).setAmount(amount);
    }

    @Override
    public synchronized void setStatus(String nodeUUID, String status) {
        //this.trafficNodes.get(nodeUUID).setStatus(status);
    }

    private synchronized String getDefaultRoute() {
        String defaultRoute = "";
        Iterator<NeighborNodes> iterator = this.trafficNodesGoing.values().iterator();
        while (iterator.hasNext()) {
            NeighborNodes next = iterator.next();
            if (next.getIsDefault() && next.getSourceUUID().equals(this.uuid) && defaultRoute.equalsIgnoreCase("")) {
                defaultRoute = next.getDestinationUUID();
            }
        }
        return defaultRoute;
    }


    public synchronized String getNextTrafficNode() {

        String finalDefaultRoute = this.defaultRoute;

        Set<Map.Entry<String, TrafficUserMock>> entrySet = this.trafficUserMap.entrySet();

        if (finalDefaultRoute.equalsIgnoreCase(this.defaultRoute)) {
            finalDefaultRoute = this.trafficNodesGoing.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
        }
        /*
        if (this.trafficNodesGoing.get(finalDefaultRoute).getControlTrafficLight().getCurrentState().toString().equals(ConfigFile.STAU_MESSAGE))
        {
            Iterator<NeighborNodes> iterator = this.trafficNodesGoing.values().iterator();
            while (iterator.hasNext()) {
                NeighborNodes next = iterator.next();
                if (!next.getControlTrafficLight().getCurrentState().toString().equals(ConfigFile.STAU_MESSAGE)) {
                    finalDefaultRoute = next.getDestinationUUID();
                }
            }
            if (finalDefaultRoute.equalsIgnoreCase(this.defaultRoute)) {
                finalDefaultRoute = this.trafficNodesGoing.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
            }
        }
        */
        return finalDefaultRoute;
    }

    public synchronized void setNextTrafficNodeForUser(String trafficUserUUID) {
        this.trafficNodeInvokeStub.setNextTrafficNode(ITrafficUser.class.getName() + "/" + trafficUserUUID, this.getNextTrafficNode());
    }

    public synchronized void registerOnNeighborTrafficNodes() {
        //this.trafficNodeInvokeStub.signInTrafficNode(ITrafficNode.class.getName(), this.uuid, , );
    }

    private void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void drive(String trafficUserUUID, double tempo) {
        if (!(Math.abs(this.trafficUserMap.get(trafficUserUUID).getTempo() - 16) < 1)) {
            this.trafficNodeInvokeStub.setTempo(ITrafficUser.class.getName() + "/" + trafficUserUUID, tempo);
        }
    }

    private synchronized void stop(String trafficUserUUID) {
        if (Math.abs(this.trafficUserMap.get(trafficUserUUID).getTempo() - 0) < 1) {
            this.trafficNodeInvokeStub.setTempo(ITrafficUser.class.getName() + "/" + trafficUserUUID, 0);
        }

    }

}
