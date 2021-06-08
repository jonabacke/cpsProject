package TrafficNode;

import Config.EPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficUserMockTest {

    private TrafficUserMock trafficMock;

    @BeforeEach
    void setUp() {
        this.trafficMock = new TrafficUserMock(16.0, EPriority.NORMAL, "123", "N1", "N2", "N7");
    }

    @Test
    void getTempo() {
    }

    @Test
    void setTempo() {
    }

    @Test
    void getPriority() {
    }

    @Test
    void setPriority() {
    }

    @Test
    void getUuid() {
    }

    @Test
    void setUuid() {
    }

    @Test
    void getNextTrafficNode() {
    }

    @Test
    void setNextTrafficNode() {
    }

    @Test
    void getFinalTrafficNode() {
    }

    @Test
    void setFinalTrafficNode() {
    }

    @Test
    void getArriveTime() {
    }

    @Test
    void getLastTrafficNode() {
    }

    @Test
    void getTimeOnStreetInSec() {
    }

    @Test
    void getDistance() {
    }

    @Test
    void refreshDistance() {
        this.trafficMock.refreshDistance();
        System.out.println(this.trafficMock.getDistance());
        System.out.println(this.trafficMock.getLastTrafficNode());
    }
}