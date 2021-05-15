package TrafficNode;

import ComModule.Middleware;
import TrafficUser.TrafficUserClientStub;

public class TrafficNodeInvokeStub implements ITrafficNodeInvoke {

    private final Middleware middleware;
    private String oldTargetName;
    private TrafficNodeClientStub trafficNodeService;
    private TrafficUserClientStub trafficUserService;

    public TrafficNodeInvokeStub(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public void signInTrafficNode(String targetName, String trafficNodeUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.signInTrafficNode(trafficNodeUUID);
    }

    @Override
    public void signOutTrafficNode(String targetName, String trafficNodeUUID) {
        if (this.trafficNodeService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficNodeService = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficNodeService.signOutTrafficNode(trafficNodeUUID);
    }

    public void setNextTrafficNode(String targetName, String trafficUserUUID) {
        if (this.trafficUserService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficUserService = new TrafficUserClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficUserService.setNextTrafficNode(trafficUserUUID);
    }

    public void buildEmergencyCorridor(String targetName) {
        if (this.trafficUserService == null || !this.oldTargetName.equals(targetName)) {
            this.trafficUserService = new TrafficUserClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.trafficUserService.buildEmergencyCorridor();
    }

}
