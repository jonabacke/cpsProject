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
        this.trafficNodeService.signInTrafficUser(trafficUserUUID, networkString);
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
    public void setTempo(String targetName, String trafficUserUUID, double tempo) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setTempo(trafficUserUUID, tempo);

    }

    @Override
    public void setPriority(String targetName, String trafficUserUUID, String priority) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setPriority(trafficUserUUID, priority);

    }

    @Override
    public void setNextTrafficNode(String targetName, String trafficUserUUID, String nextTrafficNode) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setNextTrafficNode(trafficUserUUID, nextTrafficNode);

    }

    @Override
    public void setFinalTrafficNode(String targetName, String trafficUserUUID, String finalTrafficNode) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.setFinalTrafficNode(trafficUserUUID, finalTrafficNode);

    }
}
