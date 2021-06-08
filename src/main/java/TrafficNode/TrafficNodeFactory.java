package TrafficNode;

import ComModule.Middleware;
import ComModule.SkeletonStub;
import Config.LogFormatter;
import TrafficUser.TrafficUserFactory;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficNodeFactory {
    private static final Logger logger = Logger.getLogger("Node");

    private final Boolean retain;
    private final Integer qos;

    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(Level.WARNING);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());
        String uuid = UUID.randomUUID().toString();
        if (args.length > 0) {
            uuid = args[0];
        }
        String [] streets = null;
        if (args.length > 1) {
            streets = new String[args.length - 1];
            System.arraycopy(args, 1, streets, 0, args.length - 1);
        }

        assert streets != null;
        new TrafficNodeFactory(uuid, streets);
        //if (args.length > 0)        new TrafficNodeFactory(args[0], Double.parseDouble(args[1]), Double.parseDouble(args[2]), Boolean.getBoolean(args[3]));
        //else new TrafficNodeFactory("1",0, 0, false);
    }


    private final Middleware middleware;
    private final TrafficNode trafficNode;
    private final TrafficNodeInvokeStub trafficNodeInvoke;

    public TrafficNodeFactory(String uuid, String[] streets) {
        String sourceUUID = "";
        String destinationUUID = "";
        double distance = 0;
        double weight = 0;
        boolean isDefault = false;
        ConcurrentMap<String, NeighborNodes> neighborNodesGoing = new ConcurrentHashMap<>();
        ConcurrentMap<String, NeighborNodes> neighborNodesComing = new ConcurrentHashMap<>();

        for (int i = 0; i < streets.length; i++) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get("src/main/java/Config/" + streets[i]));

                JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
                sourceUUID = (String) parser.get("sourceUUID");
                destinationUUID = (String) parser.get("destinationUUID");
                distance = Double.parseDouble((String) parser.get("distance"));
                weight = Double.parseDouble((String) parser.get("weight"));
                isDefault = Boolean.parseBoolean((String) parser.get("isDefault"));
            } catch (IOException | JsonException e) {
                e.printStackTrace();
            }

            if (sourceUUID.equals(uuid)) {
                neighborNodesGoing.put(destinationUUID, new NeighborNodes(distance, weight, isDefault, sourceUUID, destinationUUID));
            } else {
                neighborNodesComing.put(sourceUUID, new NeighborNodes(distance, weight, isDefault, sourceUUID, destinationUUID));
            }

        }

        this.retain = false;
        this.qos = 0;

        // Build Middleware
        this.middleware = new Middleware(this.qos, this.retain, uuid);

        // Build InvokeStub
        this.trafficNodeInvoke = new TrafficNodeInvokeStub(this.middleware);

        // Build Object
        this.trafficNode = new TrafficNode(uuid, this.trafficNodeInvoke, neighborNodesComing, neighborNodesGoing);

        // Build CallStub
            // TrafficUser
        new SkeletonStub(ITrafficNode.class.getName() + "/" + uuid, this.trafficNode, this.middleware, this.retain, this.qos);
            // TrafficNodes
        new SkeletonStub(ITrafficNode.class.getName(), this.trafficNode, this.middleware, this.retain, this.qos);


        this.trafficNode.init();
    }

    public TrafficNode getTrafficNode() {
        return trafficNode;
    }
}
