package ControlUnit.FSM;

import Config.ConfigFile;
import TrafficNode.Street;

import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class ControlUnit {
    private final Logger logger = Logger.getGlobal();

    private ConcurrentMap<String, Street> trafficNodesComing;

    public ControlUnit(ConcurrentMap<String, Street> trafficNodesComing) {
        this.trafficNodesComing = trafficNodesComing;
        this.run();
    }


    public void run() {

        new Thread(() -> {

            Street[] neighborNodes = this.trafficNodesComing.values().toArray(new Street[0]);
            int counter = 0;
            int nextHasPriority = -1;
            boolean nextHasTrafficUser = true;
            Street greenNode = this.trafficNodesComing.values().iterator().next();


            while (true) {

                for (int i = 0; i < neighborNodes.length; i++) {
                    if (neighborNodes[i].getControlTrafficLight().getCurrentState().equals(ConfigFile.PRIO_MESSAGE)) {
                        nextHasPriority = i;
                    }
                }

                if (nextHasPriority != -1) {
                    for (int i = 0; i < neighborNodes.length; i++) {
                        if (nextHasPriority == i) {
                            neighborNodes[i].setGreen();
                        } else {
                            neighborNodes[i].setRed();
                        }
                    }
                    nextHasPriority = -1;
                    sleep(ConfigFile.CYCLE_TIME);
                } else {
                    for (int i = 0; i < neighborNodes.length; i++) {
                        if (counter == i) {
                            neighborNodes[i].setGreen();
                        } else {
                             neighborNodes[i].setRed();
                        }
                    }
                    counter = (counter + 1) % neighborNodes.length;
                }
                sleep(ConfigFile.CYCLE_TIME);
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
