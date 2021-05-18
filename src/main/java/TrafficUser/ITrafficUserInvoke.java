package TrafficUser;

public interface ITrafficUserInvoke {

    public void signInTrafficUser(String targetName, String trafficUserUUID, String networkString);

    public void signOutTrafficUser(String targetName, String trafficUserUUID);

    public void setTempo(String targetName, String trafficUserUUID, double tempo);

    public  void setPriority(String targetName, String trafficUserUUID, String priority);

    public  void setNextTrafficNode(String targetName, String trafficUserUUID, String nextTrafficNode);

    public  void setFinalTrafficNode(String targetName, String trafficUserUUID, String finalTrafficNode);

}
