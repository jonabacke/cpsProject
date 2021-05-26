import Config.LogFormatter;
import TrafficNode.TrafficNodeFactory;
import TrafficUser.TrafficUserFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger("HAL");

    private final List<ProcessBuilder> nodeList;

    private final String javaBin;
    private final String classPath;
    private final String interrupt = "Interrupted! ";



    private static final long STARTUP_DELAY = 1000;
    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(Level.WARNING);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());
        if (args.length > 0) new App(Integer.getInteger(args[0]));
        else new App(0);

    }

    public App(Integer amount) {

        String javaHome = System.getProperty("java.home");
        this.javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        this.classPath = System.getProperty("java.class.path");
        this.nodeList = new ArrayList<>();

        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N1", "StreetN1_N2.json", "StreetN1_N3.json")).start();
        sleep(STARTUP_DELAY);
        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N2", "StreetN2_N3.json", "StreetN2_N4.json")).start();
        sleep(STARTUP_DELAY);
        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N3", "StreetN3_N4.json", "StreetN3_N5.json")).start();
        sleep(STARTUP_DELAY);
        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N4", "StreetN4_N5.json", "StreetN4_N6.json")).start();
        sleep(STARTUP_DELAY);
        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N5", "StreetN5_N6.json", "StreetN5_N7.json")).start();
        sleep(STARTUP_DELAY);
        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N6", "StreetN6_N7.json")).start();
        sleep(STARTUP_DELAY);
        new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "N7", "StreetN7_N1.json")).start();

        for (int i = 0; i < 10 * 2; i++) {
            int finalI = i;
            new Thread(() -> this.buildProcess(TrafficUserFactory.class.getName(), "" + finalI)).start();
            try {
                Thread.sleep(STARTUP_DELAY);
            } catch (InterruptedException e) {
                logger.warning(() -> this.interrupt + e);
                Thread.currentThread().interrupt();
            }
        }
    }
    private void buildProcess(String className, String uuid, String ...street) {
        List<String> command = new ArrayList<>();
        List<String> params = new ArrayList<>();
        params.add(uuid);
        params.addAll(Arrays.asList(street));
        command.add(this.javaBin);
        command.add("-cp");
        command.add(this.classPath);
        command.add(className);
        command.addAll(params);

        ProcessBuilder pb = new ProcessBuilder(command);
        this.nodeList.add(pb);
        Process process;
        try {
            process = pb.inheritIO().start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                process.destroy();
                logger.severe("ChildProcessDestroyed");
            }));
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            logger.warning(() -> this.interrupt + e);
            Thread.currentThread().interrupt();
        }

    }

    private void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            logger.warning(() -> this.interrupt + e);
            Thread.currentThread().interrupt();
        }
    }
}
