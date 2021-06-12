package TrafficUser;

import Config.ConfigFile;
import Config.EPriority;
import TrafficNode.ITrafficNode;

import java.util.logging.Logger;

public class TrafficUser implements ITrafficUser {
    private final Logger logger = Logger.getGlobal();

    private double tempo;
    private final EPriority priority;
    private final String uuid;
    private String lastTrafficNode;
    private String nextTrafficNode;
    private String finalTrafficNode;
    private TrafficUserInvokeStub trafficUserInvokeStub;
    private long timer;

    private int redCounter;


    public TrafficUser(EPriority priority, String uuid, TrafficUserInvokeStub trafficUserInvokeStub) {
        this.uuid = uuid;
        this.priority = priority;
        this.finalTrafficNode = finalTrafficNode;
        this.trafficUserInvokeStub = trafficUserInvokeStub;
        this.lastTrafficNode = "N7";
        this.redCounter = 0;
        this.setTempo(0);
        this.calcNextDestination();
        this.trafficUserInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/type", "" + priority.toString() );
        this.test();
    }

    private void test() {
        this.nextTrafficNode = "N1";
        this.signIn();

        new Thread(() -> {
            while (true) {
                this.setTempo(this.tempo);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public synchronized void signIn() {
        if (this.nextTrafficNode.equalsIgnoreCase("n1")) {
            this.timer = System.currentTimeMillis();
        }
        this.trafficUserInvokeStub.signInTrafficUser(ITrafficNode.class.getName() + "/" + this.nextTrafficNode, this.uuid, this.getNetworkString());
    }

    public synchronized void signOut() {
        if (this.nextTrafficNode.equalsIgnoreCase("n7")) {
            this.timer = System.currentTimeMillis() - this.timer;
            logger.severe("Zeit: " + this.timer + " Priorität: " + this.priority.toString() + " RedCounter: " + this.redCounter);
            this.trafficUserInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/time", "" + (this.timer / 1000) );
            this.trafficUserInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/stops", "" + this.redCounter);
        }
        this.trafficUserInvokeStub.signOutTrafficUser(ITrafficNode.class.getName() + "/" + this.nextTrafficNode, this.uuid);
    }

    @Override
    public synchronized void setTempo(double tempo) {
        this.tempo = tempo;
        if ( Math.abs(tempo - 0) < 0.01) {
            this.redCounter ++;
        }
        this.trafficUserInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/speed", "" + this.tempo);
        this.trafficUserInvokeStub.setTempo(ITrafficNode.class.getName() + "/" + this.nextTrafficNode, this.uuid, this.tempo);
    }

    @Override
    public void buildEmergencyCorridor() {
        System.out.println("Build Emergency Corridor");
    }

    public synchronized String getNetworkString() {
        String result = "";
        result += this.uuid;
        result += ConfigFile.SEPARATOR_NETWORK_CONCAT;
        result += this.tempo;
        result += ConfigFile.SEPARATOR_NETWORK_CONCAT;
        result += this.priority;
        result += ConfigFile.SEPARATOR_NETWORK_CONCAT;
        result += this.nextTrafficNode;
        result += ConfigFile.SEPARATOR_NETWORK_CONCAT;
        result += this.lastTrafficNode;
        result += ConfigFile.SEPARATOR_NETWORK_CONCAT;
        result += this.finalTrafficNode;
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    public void calcNextDestination() {
        this.nextTrafficNode = finalTrafficNode;
    }

    @Override
    public synchronized void setNextTrafficNode(String trafficNodeUUID) {
        logger.info(this.uuid + " set new Goal " + trafficNodeUUID);
        if (!this.nextTrafficNode.equalsIgnoreCase(trafficNodeUUID)) {
            this.signOut();
            this.lastTrafficNode = this.nextTrafficNode;
            this.nextTrafficNode = trafficNodeUUID;
            this.signIn();
        }
    }
}
