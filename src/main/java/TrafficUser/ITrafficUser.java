package TrafficUser;

public interface ITrafficUser {

    void setTempo(double tempo);

    void buildEmergencyCorridor();

    void setNextTrafficNode(String trafficNodeUUID);

    void getTempo();

    void getPriority();

    void getNextTrafficNode();

    void getFinalTrafficNode();
}
