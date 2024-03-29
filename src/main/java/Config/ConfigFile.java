package Config;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigFile {
    public final static String SEPARATOR_MESSAGE_REGEX = "\\|";
    public final static String SEPARATOR_MESSAGE_CONCAT = "|";
    public final static String SEPARATOR_NETWORK_CONCAT = "/";
    public final static String NETWORK_INTERFACE = "wlp0s20f3";
    public final static int WAITING_TIME = 5000; // in millis
    public static final int CIRCLE_AMOUNT = 100;
    public final static String RECEIVE = "receive";
    public final static String ERROR_RECEIVE = "errorReceive";
    public final static String RECEIVE_PUBLISH_INFORMATION = "receivePublishedInformation";
    public final static int WORKLOAD_THRESHOLD_UP = 50;
    public final static int WORKLOAD_THRESHOLD_DOWN = 45;
    public final static boolean LOG = false;
    public final static String BROKER_ADDRESS = "tcp://localhost:1883";
    public final static long CYCLE_TIME = 1000;
    public final static String RED_MESSAGE = "RED";
    public final static String GREEN_MESSAGE = "GREEN";
    public final static String STAU_MESSAGE = "STAU";
    public final static String NORMAL_MESSAGE = "NORMAL";
    public final static String PRIO_MESSAGE = "PRIO";
    public final static Level LOGGER_LEVEL = Level.SEVERE;
    public static final boolean IS_OPTIMIZED = false;


}
