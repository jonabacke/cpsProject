package TrafficUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficUserTest {

    TrafficUser trafficUser;

    @BeforeEach
    void setUp() {
        this.trafficUser = new TrafficUser(EPriority.CONSTRUCTION_VEHICLE, "test");
    }

    @org.junit.jupiter.api.Test
    void setTempo() {
        this.trafficUser.setTempo(45);
        assertEquals(45, trafficUser.getTempo());
    }

    @org.junit.jupiter.api.Test
    void buildEmergencyCorridor() {
    }

    @Test
    void getNetworkString() {
        String networkString = this.trafficUser.getNetworkString();
        TrafficUser trafficUserTest = new TrafficUser(networkString);
        assertEquals(this.trafficUser.getUuid(), trafficUserTest.getUuid());
        assertEquals(this.trafficUser.getTempo(), (trafficUserTest.getTempo()));
        assertEquals(this.trafficUser.getFinalDestination(), trafficUserTest.getFinalDestination());
        assertEquals(this.trafficUser.getNextDestination(), trafficUserTest.getNextDestination());
        assertEquals(this.trafficUser.getPriority(), trafficUserTest.getPriority());
    }

    @org.junit.jupiter.api.Test
    void calcNextDestination() {
    }
}