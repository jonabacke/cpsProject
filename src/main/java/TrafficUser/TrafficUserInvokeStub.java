package TrafficUser;

import ComModule.Middleware;
import TrafficNode.TrafficNodeClientStub;

public class TrafficUserInvokeStub implements ITrafficUserInvoke {

    private final Middleware middleware;
    private String oldTargetName;
    private TrafficNodeClientStub trafficNodeService;

    public TrafficUserInvokeStub(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public void signInTrafficUser(String targetName, String networkString, String trafficUserUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.signInTrafficUser(networkString, trafficUserUUID);
    }

    @Override
    public void signOutTrafficUser(String targetName, String trafficUserUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.signOutTrafficUser(trafficUserUUID);
    }

    @Override
    public void setTempo(String targetName, double tempo, String trafficUserUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setTempo(tempo, trafficUserUUID);

    }

    @Override
    public void setPriority(String targetName, String priority, String trafficUserUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setPriority(priority, trafficUserUUID);

    }

    @Override
    public void setNextTrafficNode(String targetName, String nextTrafficNode, String trafficUserUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setNextTrafficNode(nextTrafficNode, trafficUserUUID);

    }

    @Override
    public void setFinalTrafficNode(String targetName, String finalTrafficNode, String trafficUserUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setFinalTrafficNode(finalTrafficNode, trafficUserUUID);

    }
}
