package TrafficNode;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import TrafficUser.ITrafficUser;
import TrafficUser.TrafficUserClientStub;

import java.util.UUID;

public class TrafficNodeFactory {

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {
        System.out.println("Hello World");
        new TrafficNodeFactory("3");
    }

    private String uuid;
    private Middleware middleware;
    private TrafficNode trafficNode;
    private TrafficNodeInvokeStub trafficNodeInvoke;

    public TrafficNodeFactory(String uuid) {
        if (uuid == null) this.uuid = UUID.randomUUID().toString();
        else this.uuid = uuid;

        this.retain = false;
        this.qos = 0;

        // Build Middleware
        this.middleware = new Middleware(this.qos, this.retain, this.uuid);

        // Build InvokeStub
        this.trafficNodeInvoke = new TrafficNodeInvokeStub(this.middleware);

        // Build Object
        this.trafficNode = new TrafficNode(uuid, this.trafficNodeInvoke);

        // Build CallStub
            // TrafficUser
        new SkeletonStub(ITrafficNode.class.getName() + "/" + this.uuid, this.trafficNode, this.middleware, this.retain, this.qos);
            // TrafficNodes
        new SkeletonStub(ITrafficNode.class.getName(), this.trafficNode, this.middleware, this.retain, this.qos);


        this.trafficNode.init();
    }

}
