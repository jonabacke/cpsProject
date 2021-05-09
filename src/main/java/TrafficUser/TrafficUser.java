package TrafficUser;

import Config.ConfigFile;

import java.util.UUID;

public class TrafficUser implements ITrafficUser {

    private double tempo;
    private int priority;
    private final String uuid;

    public TrafficUser(int priority) {
        this.uuid = UUID.randomUUID().toString();
        this.priority = priority;
    }


    @Override
    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    @Override
    public void buildEmergencyCorridor() {

    }

    private String getNetworkString() {
        String result = "";
        result += this.uuid;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        result += this.tempo;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        result += this.priority;
        result += ConfigFile.SEPARATOR_MESSAGE_CONCAT;
        return result;
    }

}
