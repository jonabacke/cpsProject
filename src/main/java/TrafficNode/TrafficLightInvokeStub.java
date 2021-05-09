package TrafficNode;

import ComModule.Middleware;
import TrafficUser.ITrafficUser;

public class TrafficLightInvokeStub implements ITrafficNodeInvoke {

    private final Middleware middleware;
    private String oldTargetName;
    private TrafficNodeClientStub service;

    public TrafficLightInvokeStub(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public void addTrafficNode(String targetName, String trafficNodeUUID) {
        if (this.service == null || !this.oldTargetName.equals(targetName)) {
            this.service = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.service.addTrafficNode(trafficNodeUUID);
    }

    @Override
    public void deleteTrafficNode(String targetName, String trafficNodeUUID) {
        if (this.service == null || !this.oldTargetName.equals(targetName)) {
            this.service = new TrafficNodeClientStub(this.middleware, targetName);
            this.oldTargetName = targetName;
        }
        this.service.deleteTrafficNode(trafficNodeUUID);

    }
}
