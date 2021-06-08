package ControlUnit.FSM;

import TrafficNode.TrafficNode;
import TrafficNode.TrafficNodeFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlUnitTest {

    TrafficNode trafficNode;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @Test
    void run() {
        int a = 2;
        int b = 2<<1;
        System.out.println(b);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
}