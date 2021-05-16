import Config.LogFormatter;
import TrafficNode.TrafficNodeFactory;
import TrafficUser.TrafficUserFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger("HAL");

    private final List<ProcessBuilder> nodeList;

    private final String javaBin;
    private final String classPath;
    private final String interrupt = "Interrupted! ";



    private static final long STARTUP_DELAY = 100;
    public static void main(String[] args) {
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(Level.FINE);
        Logger.getGlobal().getParent().getHandlers()[0].setFormatter(new LogFormatter());
        if (args.length > 0) new App(Integer.getInteger(args[0]));
        else new App(10);

    }

    public App(Integer amount) {

        String javaHome = System.getProperty("java.home");
        this.javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        this.classPath = System.getProperty("java.class.path");
        this.nodeList = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int finalI = i;
            new Thread(() -> this.buildProcess(TrafficNodeFactory.class.getName(), "" + finalI)).start();
            try {
                Thread.sleep(STARTUP_DELAY);
            } catch (InterruptedException e) {
                logger.warning(() -> this.interrupt + e);
                Thread.currentThread().interrupt();
            }
        }

        for (int i = 0; i < amount; i++) {
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
    private void buildProcess(String className, String uuid) {
        List<String> command = new ArrayList<>();
        List<String> params = new ArrayList<>();
        params.add(uuid);
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
}
