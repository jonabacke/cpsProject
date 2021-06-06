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


    public TrafficUser(EPriority priority, String uuid, TrafficUserInvokeStub trafficUserInvokeStub) {
        this.uuid = uuid;
        this.priority = priority;
        this.finalTrafficNode = finalTrafficNode;
        this.trafficUserInvokeStub = trafficUserInvokeStub;
        this.lastTrafficNode = "";
        this.setTempo(0);
        this.calcNextDestination();
        this.test();
    }

    private void test() {
        this.nextTrafficNode = "N1";
        this.signIn();
        new Thread(() -> {
            while (true) {
                this.trafficUserInvokeStub.setTempo(ITrafficNode.class.getName() + "/" + this.nextTrafficNode, this.uuid, this.tempo);
                try {
                    Thread.sleep((int) (Math.random() * 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void signIn() {
        this.trafficUserInvokeStub.signInTrafficUser(ITrafficNode.class.getName() + "/" + this.nextTrafficNode, this.uuid, this.getNetworkString());
    }

    public void signOut() {
        this.trafficUserInvokeStub.signOutTrafficUser(ITrafficNode.class.getName() + "/" + this.nextTrafficNode, this.uuid);
    }

    @Override
    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    @Override
    public void buildEmergencyCorridor() {
        System.out.println("Build Emergency Corridor");
    }

    public String getNetworkString() {
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
    public void setNextTrafficNode(String trafficNodeUUID) {
        logger.info("Set new Goal " + trafficNodeUUID);
        if (!this.nextTrafficNode.equalsIgnoreCase(trafficNodeUUID)) {
            this.signOut();
            this.lastTrafficNode = this.nextTrafficNode;
            this.nextTrafficNode = trafficNodeUUID;
            this.signIn();
        }
    }
}
