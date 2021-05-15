package TrafficUser;

public interface ITrafficUser {

    public void setTempo(double tempo);

    public void buildEmergencyCorridor();

    public void setNextTrafficNode(String trafficNodeUUID);

    public void getTempo();

    public void getPriority();

    public void getNextTrafficNode();

    public void getFinalTrafficNode();
}
