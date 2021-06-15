package TrafficNode;

import Config.ConfigFile;
import Config.EPriority;
import Config.TrafficLightState;
import ControlUnit.FSM.ControlUnit;
import TrafficUser.ITrafficUser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class TrafficNode implements ITrafficNode {
    private final Logger logger = Logger.getGlobal();

    ConcurrentMap<String, Street> trafficNodesComing;
    ConcurrentMap<String, Street> trafficNodesGoing;
    ConcurrentMap<String, TrafficUserMock> trafficUserMap;
    String uuid;
    ReentrantLock userLock;

    ConcurrentLinkedQueue<String> trafficUserMocksPriority;
    ConcurrentLinkedQueue<String> trafficUserMocksNormal;

    //String defaultRoute;

    TrafficNodeInvokeStub trafficNodeInvokeStub;

    public TrafficNode(String uuid, TrafficNodeInvokeStub trafficNodeInvokeStub, ConcurrentMap<String, Street> trafficNodesComing, ConcurrentMap<String, Street> trafficNodesGoing) {
        this.trafficNodesComing = new ConcurrentHashMap<>();
        this.trafficUserMap = new ConcurrentHashMap<>();
        this.trafficUserMocksPriority = new ConcurrentLinkedQueue<>();
        this.trafficUserMocksNormal = new ConcurrentLinkedQueue<>();
        this.uuid = uuid;
        this.trafficNodeInvokeStub = trafficNodeInvokeStub;
        this.trafficNodesComing = trafficNodesComing;
        this.trafficNodesGoing = trafficNodesGoing;
        //this.defaultRoute = this.getDefaultRoute();
        this.userLock = new ReentrantLock();
        logger.info("Going:  " + this.trafficNodesGoing.toString());
        logger.info("Coming: " + this.trafficNodesComing.toString());
        new ControlUnit(trafficNodesComing);

    }

    public void init() {
        //this.registerOnNeighborTrafficNodes();
        this.run();
        this.setNextTrafficNodeForUser();
    }

    public void run() {
        new Thread(() -> {
            double priorityDistance = 0;
            while (true) {
                boolean priorityComes = false;
                this.userLock.lock();
                if (this.trafficNodesComing.size() > 0 && this.trafficUserMap.size() > 0) {
                    String[] user = this.trafficUserMap.keySet().toArray(new String[0]);
                    priorityDistance = Double.MAX_VALUE;
                    for (TrafficUserMock userMock : this.trafficUserMap.values()) {
                        if (userMock.getPriority().equals(EPriority.EMERGENCY) && userMock.getDistance() < priorityDistance) {
                            priorityDistance = userMock.getDistance();
                        }
                    }
                    for (String u: user) {
                        TrafficUserMock userMock = this.trafficUserMap.get(u);
                        userMock.refreshDistance();
                        Street currentStreet = this.trafficNodesComing.get(userMock.getLastTrafficNode());
                        // Stay if self !Priority but priority is behind
                        logger.finest("User: " + userMock.getUuid() + " with distance: " + (currentStreet.getDistance() - userMock.getDistance()));
                        priorityComes = userMock.getPriority().equals(EPriority.NORMAL) && currentStreet.getControlTrafficLight().getCurrentState().toString().equals(ConfigFile.PRIO_MESSAGE) && userMock.getDistance() > priorityDistance;
                        if (currentStreet.getDistance() - userMock.getDistance() < 5) {
                            // Stay at RED
                            if (currentStreet.getStatus().equals(TrafficLightState.RED) && userMock.getPriority().equals(EPriority.NORMAL)) {
                                logger.warning("Stop user: " + userMock.getUuid() + " with distance: " + (currentStreet.getDistance() - userMock.getDistance()));
                                this.stop(u);

                            } else if (currentStreet.getStatus().equals(TrafficLightState.GREEN) && !this.trafficUserMocksNormal.contains(userMock.getUuid())) {
                                logger.info("New Goal for: " + userMock.getUuid() + " on " + this.uuid);
                                this.trafficUserMocksNormal.add(userMock.getUuid());
                            } else if (userMock.getPriority().equals(EPriority.EMERGENCY) && !this.trafficUserMocksPriority.contains(userMock.getUuid())) {
                                logger.info("New Goal for: " + userMock.getUuid() + " on " + this.uuid);
                                this.trafficUserMocksPriority.add(userMock.getUuid());
                            }
                        } else {
                            if (userMock.getPriority().equals(EPriority.EMERGENCY)) {
                                this.drive(u, 14);
                            } else if (priorityComes) {
                                this.drive(u, this.calcTempo(currentStreet) - 5);// langsamer wegen Prio vor lassen
                            } else {
                                this.drive(u, this.calcTempo(currentStreet));
                            }
                        }
                    }

                }
                this.userLock.unlock();
                //long time = System.currentTimeMillis();
                sleep(100, Thread.currentThread());
                //logger.info("ThreadLoop: " + this.userLock.isLocked() + " time " + (System.currentTimeMillis() - time));

            }
        }).start();
    }

    private double calcTempo(Street node) {
        assert node != null;
        double result = 0;
        if (node.getWorkload() < 2) {
            result = 14;
        } else if (node.getWorkload() < 4) {
            result = 13;
        } else if (node.getWorkload() < 6) {
            result = 12;
        } else if (node.getWorkload() < 8) {
            result = 11;
        } else if (node.getWorkload() < 10) {
            result = 10;
        } else {
            result = 9;
        }
        return result;
    }

    // TODO Check if its not needed
    @Override
    public void signInTrafficNode(String trafficNodeUUID, double distance, double weight, boolean isDefault, String uuid) {
        logger.info("signIn: " + trafficNodeUUID);
        synchronized (this) {

        }
        if (!this.trafficNodesComing.containsKey(trafficNodeUUID)) {
            this.registerOnNeighborTrafficNodes();
        }
        //this.trafficNodesComing.put(trafficNodeUUID, new Street(this.uuid, this.trafficNodeInvokeStub, distance, weight, isDefault, uuid, null), );
    }

    // TODO Check if its waste
    @Override
    public void signOutTrafficNode(String trafficNodeUUID) {
        logger.info("signOut: " + trafficNodeUUID);
        this.trafficNodesComing.remove(trafficNodeUUID);
    }

    @Override
    public void signInTrafficUser(String trafficUserUUID, String trafficUserNetworkString) {
        logger.info("signIn: " + trafficUserUUID);

        TrafficUserMock trafficUser = new TrafficUserMock(trafficUserNetworkString);

        this.userLock.lock();
        this.trafficUserMap.put(trafficUserUUID, trafficUser);

        logger.info(trafficUser.getLastTrafficNode());
        if (this.trafficNodesComing.get(trafficUser.getLastTrafficNode()) == null) {
            logger.severe("TrafficUser: " + trafficUserNetworkString + " node: " + this.uuid + " next: " + trafficUser.getNextTrafficNode());
            this.trafficNodeInvokeStub.signInTrafficUser(ITrafficNode.class.getName() + "/" + trafficUser.getLastTrafficNode(), trafficUserUUID, trafficUserNetworkString);
            throw new NullPointerException();
        }
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
        this.userLock.unlock();
    }

    @Override
    public void signOutTrafficUser(String trafficUserUUID) {
        logger.info("signOut: " + trafficUserUUID);
        this.userLock.lock();
        if (this.trafficUserMap.get(trafficUserUUID).getPriority().equals(EPriority.EMERGENCY))
            this.trafficNodesComing.get(this.trafficUserMap.get(trafficUserUUID).getLastTrafficNode()).decrementPriority();


        if (this.trafficUserMap.get(trafficUserUUID) == null) throw new NullPointerException();
        logger.info(this.trafficUserMap.get(trafficUserUUID).getLastTrafficNode());
        if (this.trafficNodesComing.get(this.trafficUserMap.get(trafficUserUUID).getLastTrafficNode()) == null)
            throw new NullPointerException();
        this.trafficNodesComing.get(this.trafficUserMap.get(trafficUserUUID).getLastTrafficNode()).decrementAmount();

        this.trafficUserMap.remove(trafficUserUUID);
        for (String key : this.trafficNodesComing.keySet()) {
            this.trafficNodeInvokeStub.setWorkload(ITrafficNode.class.getName() + "/" + key, this.uuid, this.trafficUserMap.size());
        }
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/amount", "" + this.trafficUserMap.size());
        this.userLock.unlock();
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
        this.userLock.lock();
        this.trafficUserMap.get(trafficUserUUID).setTempo(tempo);
        this.userLock.unlock();
        String Tempo = "" + tempo;
        Tempo = Tempo.substring(0, Math.min(5, Tempo.length()));
        trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/speed", trafficUserUUID + "/" + Tempo);
    }

    @Override
    public void setPriority(String trafficUserUUID, String priority) {
        logger.info("Priority: " + priority);
        if (priority.equals(EPriority.EMERGENCY.toString())) {
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/status", priority);
        }
        this.userLock.lock();
        this.trafficUserMap.get(trafficUserUUID).setPriority(EPriority.valueOf(priority));
        this.userLock.unlock();
    }

    @Override
    public void setNextTrafficNode(String trafficUserUUID, String nextTrafficNode) {
        logger.info("NextTrafficNode: " + nextTrafficNode);
        this.userLock.lock();
        this.trafficUserMap.get(trafficUserUUID).setNextTrafficNode(nextTrafficNode);
        this.userLock.unlock();
    }

    @Override
    public void setFinalTrafficNode(String trafficUserUUID, String finalTrafficNode) {
        logger.info("FinalTrafficNode: " + finalTrafficNode);
        this.userLock.lock();
        this.trafficUserMap.get(trafficUserUUID).setFinalTrafficNode(finalTrafficNode);
        this.userLock.unlock();
    }

    @Override
    public void setWorkload(String trafficNodeUUID, int amount) {
        if (this.trafficNodesGoing.get(trafficNodeUUID) == null) {
            logger.warning("Receiver ID: " + this.uuid);
            logger.warning("Sender ID: " + trafficNodeUUID);
            logger.warning("Receiver Neighbors: " + this.trafficNodesGoing.keySet().toString());
            return;
        }
        this.trafficNodesGoing.get(trafficNodeUUID).setAmount(amount);
    }

    @Override
    public void setStatus(String nodeUUID, String status) {
        //this.trafficNodes.get(nodeUUID).setStatus(status);
    }

    private synchronized String getDefaultRoute() {
        String defaultRoute = "";
        for (Street next : this.trafficNodesGoing.values()) {
            if (next.isDefault() && defaultRoute.equalsIgnoreCase("")) {
                return next.getDestinationUUID();
            }
        }
        throw new NullPointerException();
    }


    public synchronized String getNextTrafficNode() {

        String finalDefaultRoute = null;
        double workload = Double.MAX_VALUE;
        if (ConfigFile.IS_OPTIMIZED) {
            for (Street street : this.trafficNodesGoing.values()) {
                logger.info("Node: " + this.uuid + " with workload: " + street.getWorkload() + " Street from: " + street.getSourceUUID() + " to " + street.getDestinationUUID() + " isDefault: " + street.isDefault());
                if (Math.abs(street.getWorkload() - 0) < 0.1 && street.isDefault()) {
                    return street.getDestinationUUID();
                } else if (street.getWorkload() < workload) {
                    workload = street.getWorkload();
                    finalDefaultRoute = street.getDestinationUUID();
                }
            }
        } else {

            for (Street street : this.trafficNodesGoing.values()) {
                if (street.isDefault()) {
                    return street.getDestinationUUID();
                }
            }
        }
        if (finalDefaultRoute == null) throw new NullPointerException();
        return finalDefaultRoute;
    }

    public synchronized void setNextTrafficNodeForUser() {
        new Thread(() -> {
            while (true) {
                if (!this.trafficUserMocksPriority.isEmpty()) {
                    String nextStreet = this.getNextTrafficNode();
                    logger.info("send E to: " + nextStreet);
                    this.trafficNodeInvokeStub.setNextTrafficNode(ITrafficUser.class.getName() + "/" + this.trafficUserMocksPriority.poll(), nextStreet);
                } else if (!this.trafficUserMocksNormal.isEmpty()) {
                    String nextStreet = this.getNextTrafficNode();
                    logger.info("send N to: " + nextStreet);
                    this.trafficNodeInvokeStub.setNextTrafficNode(ITrafficUser.class.getName() + "/" + this.trafficUserMocksNormal.poll(), nextStreet);
                }
                this.sleep(100, Thread.currentThread());
            }
        }).start();
    }

    public synchronized void registerOnNeighborTrafficNodes() {
        //this.trafficNodeInvokeStub.signInTrafficNode(ITrafficNode.class.getName(), this.uuid, , );
    }

    private void sleep(long s, Thread thread) {
        try {
            thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void drive(String trafficUserUUID, double tempo) {
        if (!(Math.abs(this.trafficUserMap.get(trafficUserUUID).getTempo() - tempo) < 1)) {
            logger.fine(String.valueOf((Math.abs(this.trafficUserMap.get(trafficUserUUID).getTempo() - tempo) < 1)));
            this.trafficNodeInvokeStub.setTempo(ITrafficUser.class.getName() + "/" + trafficUserUUID, tempo);
        }
    }

    private synchronized void stop(String trafficUserUUID) {
        //if (Math.abs(this.trafficUserMap.get(trafficUserUUID).getTempo() - 0) < 1) {
            this.trafficNodeInvokeStub.setTempo(ITrafficUser.class.getName() + "/" + trafficUserUUID, 0);
        //}
    }

}
