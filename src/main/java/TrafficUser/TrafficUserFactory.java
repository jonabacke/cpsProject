package TrafficUser;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import Config.ConfigFile;
import Config.EPriority;
import Config.LogFormatter;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficUserFactory {

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(ConfigFile.LOGGER_LEVEL);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());
        EPriority prio;
        if (Math.random() < 0.05) {
            prio = EPriority.EMERGENCY;
        } else {
            prio = EPriority.NORMAL;
        }
        if (args.length > 0) {
            new TrafficUserFactory(prio, "user" + args[0]);
            //new TrafficUserFactory(prio, UUID.randomUUID().toString());
        } else {
            new TrafficUserFactory(prio, UUID.randomUUID().toString());
        }
    }

    private String uuid;
    private Middleware middleware;
    private TrafficUser trafficUser;
    private TrafficUserInvokeStub trafficUserInvokeStub;

    public TrafficUserFactory(EPriority prio, String uuid) {
        this.uuid = uuid;

        this.retain = false;
        this.qos = 0;

        // Build Middleware
        this.middleware = new Middleware(this.qos, this.retain, this.uuid);

        // Build InvokeStub
        this.trafficUserInvokeStub = new TrafficUserInvokeStub(this.middleware);

        // Build Object
        this.trafficUser = new TrafficUser(prio, uuid, this.trafficUserInvokeStub);

        // Build CallStub
        new SkeletonStub(ITrafficUser.class.getName() + "/" + this.uuid, this.trafficUser, this.middleware, this.retain, this.qos);

    }
}
