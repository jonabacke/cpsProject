package TrafficUser;

public interface ITrafficUserInvoke {

    public void signInTrafficUser(String targetName, String networkString, String trafficUserUUID);

    public void signOutTrafficUser(String targetName, String trafficUserUUID);

    public  void setTempo(String targetName, double tempo, String trafficUserUUID);

    public  void setPriority(String targetName, String priority, String trafficUserUUID);

    public  void setNextTrafficNode(String targetName, String nextTrafficNode, String trafficUserUUID);

    public  void setFinalTrafficNode(String targetName, String finalTrafficNode, String trafficUserUUID);

}
