package TrafficNode;

import Config.ConfigFile;
import Config.TrafficLightState;
import ControlUnit.FSM.logic.ControlGreenRed;
import ControlUnit.FSM.logic.ControlTrafficLight;

import java.util.logging.Logger;

public class Street implements Comparable<Street>
{
    private final Logger logger = Logger.getGlobal();

    private TrafficNodeInvokeStub trafficNodeInvokeStub;

    private String uuid;
    // Direction of Street
    private final String sourceUUID;
    private final String destinationUUID;
    // Attributes of Street
    private double distance;
    private double weight;
    private boolean isDefault;
    // Traffic on Street
    private int amount;
    private Double workload;
    // FSMs
    private ControlGreenRed controlGreenRed;
    private ControlTrafficLight controlTrafficLight;
    private int priorityCounter = 0;
    private boolean isComing;

    public Street(String uuid, TrafficNodeInvokeStub trafficNodeInvokeStub, double distance, double weight, boolean isDefault, String sourceUUID, String destinationUUID, boolean isComing)
    {
        this.distance = 300;
        this.weight = weight;
        this.isDefault = isDefault;
        this.sourceUUID = sourceUUID;
        this.destinationUUID = destinationUUID;
        this.amount = 0;
        this.isComing = isComing;

        this.controlGreenRed = new ControlGreenRed();
        this.controlGreenRed.start();
        this.logger.info(this.controlGreenRed.toString());

        this.controlTrafficLight = new ControlTrafficLight();
        this.controlTrafficLight.start();

        this.trafficNodeInvokeStub = trafficNodeInvokeStub;
        this.uuid = uuid;

        this.calcWorkload();
        this.logger.info(this.controlTrafficLight.toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
    }

    public void setGreen() {
        this.controlGreenRed.setMessage(ConfigFile.GREEN_MESSAGE);
        if (this.controlGreenRed.step()) {
            logger.finest(this.controlGreenRed.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/" + this.getSourceUUID() + this.getDestinationUUID() + "/status", ConfigFile.GREEN_MESSAGE);
        }
    }

    public void setRed() {
        this.controlGreenRed.setMessage(ConfigFile.RED_MESSAGE);
        if (this.controlGreenRed.step()) {
            logger.finest(this.controlGreenRed.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/" + this.getSourceUUID() + this.getDestinationUUID() + "/status", ConfigFile.RED_MESSAGE);
        }
    }

    public void setPriority(double tempo) {
        long time = 0;
        if (tempo > 1) {
            time = Math.max(((int) (this.distance / tempo) - 1) * 1000L, ConfigFile.CYCLE_TIME); // Berechne Zeit bis Emergency an Ampel - 1 sek für umschalten
        }
        long finalTime = time;
        new Thread(() -> {
            try {
                Thread.sleep(finalTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.controlTrafficLight.setMessage(ConfigFile.PRIO_MESSAGE);
            if(this.controlTrafficLight.step()){
                logger.info(this.controlTrafficLight.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
                this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/" + this.getSourceUUID() + this.getDestinationUUID() + "/mode", ConfigFile.PRIO_MESSAGE);
            }
        }).start();


    }



    public void decrementPriority() {
        this.priorityCounter --;
        if (priorityCounter == 0) {
            this.setStau();
        }
    }

    public void setStau() {
        this.controlTrafficLight.setMessage(ConfigFile.STAU_MESSAGE);
        if(this.controlTrafficLight.step()){
            logger.info(this.controlTrafficLight.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/" + this.getSourceUUID() + this.getDestinationUUID() + "/mode", ConfigFile.STAU_MESSAGE);
        }
    }

    public void setNormal() {
        this.controlTrafficLight.setMessage(ConfigFile.NORMAL_MESSAGE);
        if(this.controlTrafficLight.step()){
            logger.info(this.controlTrafficLight.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
            this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/" + this.getSourceUUID() + this.getDestinationUUID() + "/mode", ConfigFile.NORMAL_MESSAGE);
        }
    }

    public double getDistance() {
        return distance;
    }

    public double getWeight() {
        return weight;
    }

    public String getSourceUUID() {
        return sourceUUID;
    }

    public String getDestinationUUID() {
        return destinationUUID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.calcWorkload();
    }

    public void incrementAmount() {
        this.amount ++;
        this.calcWorkload();
    }

    public void decrementAmount() {
        this.amount --;
        this.calcWorkload();
    }

    public void calcWorkload() {
        assert this.amount > 0;
        this.workload = this.amount * this.weight;
        this.trafficNodeInvokeStub.publishVisualizationData("frontend/" + this.uuid + "/" + this.getSourceUUID() + this.getDestinationUUID() + "/amount", "" + this.amount);

        if (workload > ConfigFile.WORKLOAD_THRESHOLD_UP && !this.controlTrafficLight.getCurrentState().toString().equals(ConfigFile.PRIO_MESSAGE)) {
            this.setStau();
        } else if (!this.controlTrafficLight.getCurrentState().toString().equals(ConfigFile.PRIO_MESSAGE) && workload < ConfigFile.WORKLOAD_THRESHOLD_DOWN) {
            this.setNormal();
        }
    }

    public ControlGreenRed getControlGreenRed() {
        return controlGreenRed;
    }

    public ControlTrafficLight getControlTrafficLight() {
        return controlTrafficLight;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public TrafficLightState getStatus() {
        return this.controlGreenRed.getStatus();
    }

    public double getWorkload() {
        return workload;
    }

    @Override
    public int compareTo(Street o) {
        return this.workload.compareTo(o.getWorkload());
    }

}
