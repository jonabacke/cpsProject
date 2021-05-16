package TrafficUser;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import Config.LogFormatter;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficUserFactory {

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(Level.INFO);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());
        System.out.println("Hello World");
        new TrafficUserFactory();
    }

    private String uuid;
    private Middleware middleware;
    private TrafficUser trafficUser;
    private TrafficUserInvokeStub trafficUserInvokeStub;

    public TrafficUserFactory() {
        this.uuid = UUID.randomUUID().toString();

        this.retain = false;
        this.qos = 0;

        // Build Middleware
        this.middleware = new Middleware(this.qos, this.retain, this.uuid);

        // Build InvokeStub
        this.trafficUserInvokeStub = new TrafficUserInvokeStub(this.middleware);

        // Build Object
        this.trafficUser = new TrafficUser(EPriority.CONSTRUCTION_VEHICLE, uuid, this.trafficUserInvokeStub);

        // Build CallStub
        new SkeletonStub(ITrafficUser.class.getName() + "/" + this.uuid, this.trafficUser, this.middleware, this.retain, this.qos);

    }
}
