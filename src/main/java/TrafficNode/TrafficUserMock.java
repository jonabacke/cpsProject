package TrafficNode;

import Config.ConfigFile;
import TrafficUser.EPriority;

public class TrafficUserMock {

    private double tempo;
    private EPriority priority;
    private String uuid;
    private String nextTrafficNode;
    private String finalTrafficNode;

    public TrafficUserMock(double tempo, EPriority priority, String uuid, String trafficNode, String finalTrafficNode) {
        this.tempo = tempo;
        this.priority = priority;
        this.uuid = uuid;
        nextTrafficNode = trafficNode;
        this.finalTrafficNode = finalTrafficNode;
    }

    public TrafficUserMock(String networkString) {
        String [] networkStrings = networkString.split(ConfigFile.SEPARATOR_NETWORK_CONCAT);
        if (networkStrings.length > 3) {
            this.uuid = networkStrings[0];
            this.tempo = Double.parseDouble(networkStrings[1]);
            this.priority = EPriority.valueOf(networkStrings[2]);
            this.nextTrafficNode = networkStrings[3];
        }
        if (networkStrings.length == 5) {
            this.finalTrafficNode = networkStrings[4];
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
}
