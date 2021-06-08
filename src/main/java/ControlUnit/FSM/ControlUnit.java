package ControlUnit.FSM;

import Config.ConfigFile;
import TrafficNode.NeighborNodes;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class ControlUnit {
    private final Logger logger = Logger.getGlobal();

    ConcurrentMap<String, NeighborNodes> trafficNodesComing;

    public ControlUnit(ConcurrentMap<String, NeighborNodes> trafficNodesComing) {
        this.trafficNodesComing = trafficNodesComing;
        this.run();
    }


    public void run() {

        new Thread(() -> {

            NeighborNodes [] neighborNodes = this.trafficNodesComing.values().toArray(new NeighborNodes[0]);
            int counter = 0;
            boolean nextHasTrafficUser = true;
            boolean nextHasPriority = false;
            NeighborNodes greenNode = this.trafficNodesComing.values().iterator().next();


            while (true) {
                for (int i = 0; i < neighborNodes.length; i++) {
                    if (counter == i) {
                        neighborNodes[i].setGreen();
                    } else {
                        neighborNodes[i].setRed();
                    }
                }
                counter = (counter + 1) % neighborNodes.length;


                sleep(ConfigFile.CYCLE_TIME);

            /*
            for (NeighborNodes nodes: this.trafficNodesComing.values()) {
                if (nodes.getControlTrafficLight().getCurrentState().toString().equalsIgnoreCase(ConfigFile.PRIO_MESSAGE)) {
                    nextHasPriority = true;
                    break;
                }
                if (nodes.getAmount() > 0) {
                    nextHasTrafficUser = true;
                    break;
                }
            }



                if (nextHasPriority) {
                    greenNode.setRed();
                    for (NeighborNodes nodes : this.trafficNodesComing.values()) {
                        if (nodes.getControlTrafficLight().getCurrentState().toString().equalsIgnoreCase(ConfigFile.PRIO_MESSAGE)) {
                            nodes.setGreen();
                            greenNode = nodes;
                            break;
                        }
                    }
                } else if (nextHasTrafficUser) {
                    greenNode.setRed();
                    for (NeighborNodes nodes : this.trafficNodesComing.values()) {
                        if (nodes.getAmount() > 0) {
                            nodes.setGreen();
                            greenNode = nodes;
                            break;
                        }
                    }
                }
                if (nextHasPriority) {
                    nextHasPriority = false;
                    sleep(ConfigFile.CYCLE_TIME);
                }
                sleep(ConfigFile.CYCLE_TIME);
             */
            }
        }).start();
    }


    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
