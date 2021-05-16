package TrafficNode;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import Config.LogFormatter;
import TrafficUser.TrafficUserFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficNodeFactory {
    private static final Logger logger = Logger.getLogger("Node");

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(Level.FINE);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());
        if (args.length > 0)        new TrafficNodeFactory(args[0]);
        else new TrafficNodeFactory("1");
    }


    private final String uuid;
    private final Middleware middleware;
    private final TrafficNode trafficNode;
    private final TrafficNodeInvokeStub trafficNodeInvoke;

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
