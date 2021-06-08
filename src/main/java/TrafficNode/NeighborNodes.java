package TrafficNode;

import Config.ConfigFile;
import Config.TrafficLightState;
import ControlUnit.FSM.logic.ControlGreenRed;
import ControlUnit.FSM.logic.ControlTrafficLight;

import java.util.logging.Logger;

public class NeighborNodes implements Comparable<NeighborNodes>
{
    private final Logger logger = Logger.getGlobal();
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

    public NeighborNodes(double distance, double weight, boolean isDefault, String sourceUUID, String destinationUUID)
    {
        this.distance = distance;
        this.weight = weight;
        this.isDefault = isDefault;
        this.sourceUUID = sourceUUID;
        this.destinationUUID = destinationUUID;
        this.amount = 0;

        this.controlGreenRed = new ControlGreenRed();
        this.controlGreenRed.start();
        this.logger.info(this.controlGreenRed.toString());

        this.controlTrafficLight = new ControlTrafficLight();
        this.controlTrafficLight.start();

        this.logger.info(this.controlTrafficLight.toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
    }

    public void setGreen() {
        this.controlGreenRed.setMessage(ConfigFile.GREEN_MESSAGE);
        if (this.controlGreenRed.step()) {
            logger.info(this.controlGreenRed.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
        }
    }

    public void setRed() {
        this.controlGreenRed.setMessage(ConfigFile.RED_MESSAGE);
        if (this.controlGreenRed.step()) {
            logger.info(this.controlGreenRed.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
        }
    }

    public void setPriority() {
        this.controlTrafficLight.setMessage(ConfigFile.PRIO_MESSAGE);
        if(this.controlTrafficLight.step()){
            logger.info(this.controlTrafficLight.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
        }
    }

    public void incrementPriority() {
        this.priorityCounter ++;
        this.setPriority();
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
        }
    }

    public void setNormal() {
        this.controlTrafficLight.setMessage(ConfigFile.NORMAL_MESSAGE);
        if(this.controlTrafficLight.step()){
            logger.info(this.controlTrafficLight.getCurrentState().toString() + " from " + this.sourceUUID + " to " + this.destinationUUID);
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
        this.workload = this.amount * this.weight;
        if (workload > ConfigFile.WORKLOAD_THRESHOLD && !this.controlTrafficLight.getCurrentState().equals(ConfigFile.PRIO_MESSAGE)) {
            this.setStau();
        } else if (!this.controlTrafficLight.getCurrentState().equals(ConfigFile.PRIO_MESSAGE)) {
            this.setNormal();
        }
    }

    public ControlGreenRed getControlGreenRed() {
        return controlGreenRed;
    }

    public ControlTrafficLight getControlTrafficLight() {
        return controlTrafficLight;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public TrafficLightState getStatus() {
        return this.controlGreenRed.getStatus();
    }

    public double getWorkload() {
        return workload;
    }

    @Override
    public int compareTo(NeighborNodes o) {
        return this.workload.compareTo(o.workload);
    }

}
