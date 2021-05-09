package TrafficNode;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import TrafficUser.ITrafficUserInvoke;
import TrafficUser.TrafficUserInvokeStub;

import java.util.UUID;

public class TrafficNodeFactory {

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {

    }

    private String uuid;
    private Middleware middleware;
    private TrafficNode trafficNode;
    private ITrafficUserInvoke trafficUserInvoke;
    private ITrafficNodeInvoke trafficNodeInvoke;

    public TrafficNodeFactory() {

        this.uuid = UUID.randomUUID().toString();
        this.retain = false;
        this.qos = 2;
        this.trafficNode = new TrafficNode(uuid);

        this.middleware = new Middleware("trafficNode/" + this.uuid, this.qos, this.retain, this.uuid);

        this.trafficNodeInvoke = new TrafficNodeInvokeStub(this.middleware);
        this.trafficUserInvoke = new TrafficUserInvokeStub(this.middleware);

        new SkeletonStub(ITrafficNode.class.getName(), this.trafficNode, this.middleware, this.retain, this.qos);

    }

}
