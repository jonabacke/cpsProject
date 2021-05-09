package TrafficUser;

import ComModule.Middleware;
import TrafficNode.TrafficNodeClientStub;

public class TrafficUserInvokeStub implements ITrafficUserInvoke{

    private final Middleware middleware;
    private String oldTargetName;
    private TrafficUserClientStub service;

    public TrafficUserInvokeStub(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public void registerTrafficUser(String targetName, String trafficUserUUID) {
        if (this.service == null || !this.oldTargetName.equals(targetName)) {
            this.service = new TrafficUserClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.service.registerTrafficUser(trafficUserUUID);

    }

    @Override
    public void deleteTrafficUser(String targetName, String trafficUserUUID) {
        if (this.service == null || !this.oldTargetName.equals(targetName)) {
            this.service = new TrafficUserClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.service.deleteTrafficUser(trafficUserUUID);

    }
}
