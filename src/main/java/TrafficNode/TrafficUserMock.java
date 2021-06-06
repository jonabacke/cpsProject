package TrafficNode;

import Config.ConfigFile;
import Config.EPriority;


public class TrafficUserMock {

    private double tempo;
    private EPriority priority;
    private String uuid;
    private String nextTrafficNode;
    private String finalTrafficNode;
    private long arriveTime;
    private String lastTrafficNode;
    private double distance;
    private double lastDistanceTime;

    public TrafficUserMock(double tempo, EPriority priority, String uuid, String trafficNode, String finalTrafficNode, String lastTrafficNode) {
        this.tempo = tempo;
        this.priority = priority;
        this.uuid = uuid;
        nextTrafficNode = trafficNode;
        this.finalTrafficNode = finalTrafficNode;
        this.arriveTime = System.currentTimeMillis();
        this.lastDistanceTime = System.currentTimeMillis() / 1000;
        this.lastTrafficNode = lastTrafficNode;
        this.distance = 0;
    }

    public TrafficUserMock(String networkString) {
        String [] networkStrings = networkString.split(ConfigFile.SEPARATOR_NETWORK_CONCAT);
        if (networkStrings.length > 4) {
            this.distance = 0;
            this.arriveTime = System.currentTimeMillis();
            this.lastDistanceTime = System.currentTimeMillis() / 1000;
            this.uuid = networkStrings[0];
            this.tempo = Double.parseDouble(networkStrings[1]);
            this.priority = EPriority.valueOf(networkStrings[2]);
            this.nextTrafficNode = networkStrings[3];
            this.lastTrafficNode = networkStrings[4];
        }
        if (networkStrings.length == 6) {
            this.finalTrafficNode = networkStrings[5];
        }
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

    public String getLastTrafficNode() {
        if (this.lastTrafficNode.equalsIgnoreCase("")) {
            return "N7";
        } else {
            return lastTrafficNode;
        }
    }

    public double getTimeOnStreetInSec() {
        return (System.currentTimeMillis() - this.getArriveTime()) / 1000;
    }

    public double getDistance() {
        return distance;
    }

    public void refreshDistance() {
        this.distance = this.distance + this.tempo * this.lastDistanceTime;
        this.lastDistanceTime = System.currentTimeMillis() / 1000;
    }
}
