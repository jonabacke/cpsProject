package ComModule;


public class Service {
    private final String serviceName;
    private final int qos;
    private final boolean retain;

    public Service(String serviceName, int qos, boolean retain) {
        this.serviceName = serviceName;
        this.qos = qos;
        this.retain = retain;
    }

    public int getQos() {
        return qos;
    }

    public boolean isRetain() {
        return retain;
    }

    public String getServiceName() {
        return serviceName;
    }
}
