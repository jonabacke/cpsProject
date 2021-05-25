package TrafficNode;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import Config.LogFormatter;
import TrafficUser.TrafficUserFactory;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficNodeFactory {
    private static final Logger logger = Logger.getLogger("Node");

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(Level.FINE);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/java/Config/system.json"));

            JsonObject system = new JsonObject();
            system.put("uuid", "0");
            Jsoner.serialize(system, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/Config/system.json"));


        } catch (IOException e) {
            e.printStackTrace();
        }

        //if (args.length > 0)        new TrafficNodeFactory(args[0], Double.parseDouble(args[1]), Double.parseDouble(args[2]), Boolean.getBoolean(args[3]));
        //else new TrafficNodeFactory("1",0, 0, false);
    }


    private final String uuid;
    private final Middleware middleware;
    private final TrafficNode trafficNode;
    private final TrafficNodeInvokeStub trafficNodeInvoke;

    public TrafficNodeFactory(String uuid, double distance, double weight, boolean isDefault) {
        if (uuid == null) this.uuid = UUID.randomUUID().toString();
        else this.uuid = uuid;

        Map<String, NeighborNodes> neighborNodesMap = new HashMap<>();

        this.retain = false;
        this.qos = 0;

        // Build Middleware
        this.middleware = new Middleware(this.qos, this.retain, this.uuid);

        // Build InvokeStub
        this.trafficNodeInvoke = new TrafficNodeInvokeStub(this.middleware);

        // Build Object
        this.trafficNode = new TrafficNode(uuid, this.trafficNodeInvoke, neighborNodesMap);

        // Build CallStub
            // TrafficUser
        new SkeletonStub(ITrafficNode.class.getName() + "/" + this.uuid, this.trafficNode, this.middleware, this.retain, this.qos);
            // TrafficNodes
        new SkeletonStub(ITrafficNode.class.getName(), this.trafficNode, this.middleware, this.retain, this.qos);


        this.trafficNode.init();
    }

}
