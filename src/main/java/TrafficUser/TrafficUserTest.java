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
        assertTrue(this.trafficUser.getUuid().equals(trafficUserTest.getUuid()));
        assertTrue(this.trafficUser.getTempo() == (trafficUserTest.getTempo()));
        assertTrue(this.trafficUser.getFinalDestination().equals(trafficUserTest.getFinalDestination()));
        assertTrue(this.trafficUser.getNextDestination().equals(trafficUserTest.getNextDestination()));
        assertTrue(this.trafficUser.getPriority().equals(trafficUserTest.getPriority()));
    }

    @org.junit.jupiter.api.Test
    void calcNextDestination() {
    }
}