package TrafficUser;

import Config.ConfigFile;

import java.util.UUID;

public class TrafficUser implements ITrafficUser {

    private double tempo;
    private EPriority priority;
    private final String uuid;
    private String nextDestination;
    private String finalDestination;

    public static void main(String[] args) {
        TrafficUser trafficUser = new TrafficUser(EPriority.NORMAl, "");
    }

    public TrafficUser(EPriority priority, String finalDestination) {
        this.uuid = UUID.randomUUID().toString();
        this.priority = priority;
        this.finalDestination = finalDestination;
        this.setTempo(0);
        this.calcNextDestination();
    }

    public TrafficUser(double tempo, EPriority priority, String uuid, String nextDestination, String finalDestination) {
        this.tempo = tempo;
        this.priority = priority;
        this.uuid = uuid;
        this.nextDestination = nextDestination;
        this.finalDestination = finalDestination;
    }

    public TrafficUser(String networkString) {
        String [] networkStrings = networkString.split(ConfigFile.SEPARATOR_MESSAGE_REGEX);
        this.uuid = networkStrings[0];
        this.tempo = Double.parseDouble(networkStrings[1]);
        this.priority = EPriority.valueOf(networkStrings[2]);
        this.nextDestination = networkStrings[3];
        this.finalDestination = networkStrings[4];

    }

    @Override
    public void setTempo(double tempo) {
        if (tempo > Double.MAX_VALUE || tempo < Double.MIN_VALUE) {
            throw new IllegalArgumentException();
        }

        this.tempo = tempo;
    }

    @Override
    public void buildEmergencyCorridor() {

    }

    public String getNetworkString() {
        String result = "";
        result += this.uuid;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        result += this.tempo;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        result += this.priority;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        result += this.nextDestination;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        result += this.finalDestination;
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public void setNextDestination(String nextDestination) {
        this.nextDestination = nextDestination;
    }

    public void calcNextDestination() {
        this.nextDestination = finalDestination;
    }

    public double getTempo() {
        return this.tempo;
    }

    public EPriority getPriority() {
        return priority;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public String getNextDestination() {
        return nextDestination;
    }
}
