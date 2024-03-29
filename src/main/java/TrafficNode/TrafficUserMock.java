package TrafficNode;

import Config.ConfigFile;
import Config.EPriority;

import java.util.logging.Logger;


public class TrafficUserMock {

    private final Logger logger = Logger.getGlobal();
    private double tempo;
    private EPriority priority;
    private String uuid;
    private String nextTrafficNode;
    private String finalTrafficNode;
    private long arriveTime;
    private String lastTrafficNode;
    private double distance;
    private long lastTimeStamp;

    public TrafficUserMock(double tempo, EPriority priority, String uuid, String trafficNode, String finalTrafficNode, String lastTrafficNode) {
        this.tempo = tempo;
        this.priority = priority;
        this.uuid = uuid;
        nextTrafficNode = trafficNode;
        this.finalTrafficNode = finalTrafficNode;
        this.arriveTime = System.currentTimeMillis();
        this.lastTrafficNode = lastTrafficNode;
        this.distance = 0;
        this.lastTimeStamp = System.currentTimeMillis();
    }

    public TrafficUserMock(String networkString) {
        String [] networkStrings = networkString.split(ConfigFile.SEPARATOR_NETWORK_CONCAT);
        if (networkStrings.length > 4) {
            this.distance = 0;
            this.arriveTime = System.currentTimeMillis();
            this.uuid = networkStrings[0];
            this.tempo = Double.parseDouble(networkStrings[1]);
            this.priority = EPriority.valueOf(networkStrings[2]);
            this.nextTrafficNode = networkStrings[3];
            this.lastTrafficNode = networkStrings[4];
        }
        if (networkStrings.length == 6) {
            this.finalTrafficNode = networkStrings[5];
        }
        this.lastTimeStamp = System.currentTimeMillis();
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public EPriority getPriority() {
        return priority;
    }

    public void setPriority(EPriority priority) {
        this.priority = priority;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNextTrafficNode() {
        return nextTrafficNode;
    }

    public void setNextTrafficNode(String nextTrafficNode) {
        this.nextTrafficNode = nextTrafficNode;
    }

    public String getFinalTrafficNode() {
        return finalTrafficNode;
    }

    public void setFinalTrafficNode(String finalTrafficNode) {
        this.finalTrafficNode = finalTrafficNode;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setLastTrafficNode(String lastTrafficNode) {
        this.lastTrafficNode = lastTrafficNode;
    }

    public String getLastTrafficNode() {
        if (this.lastTrafficNode.equalsIgnoreCase("")) {
            return "N7";
        } else {
            return lastTrafficNode;
        }
    }

    public double getDistance() {
        logger.finest("distance: " + this.distance);
        return this.distance;
    }

    public void refreshDistance() {

        this.distance = this.distance + this.tempo * (System.currentTimeMillis() - this.lastTimeStamp) / 1000;
        this.lastTimeStamp = System.currentTimeMillis();
        logger.finest("distance: " + this.distance);
        logger.finest("tempo: " + this.tempo);
    }
}
