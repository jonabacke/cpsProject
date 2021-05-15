package ComModule;

import Config.ConfigFile;

import java.util.UUID;
import java.util.logging.Logger;

public class Middleware implements IMiddlewareInvoke, IMiddlewareRegisterService {
    private static final Logger logger = Logger.getGlobal();

    private final MqttSend mqtt;
    private final int qos;
    private final boolean retain;

    public Middleware(int qos, boolean retain, String uuid) {
        this.qos = qos;
        this.retain = retain;
        this.mqtt = new MqttSend(uuid);
    }

    @Override
    public void register(String uuid, String serviceName, IMiddlewareCallableStub stub, boolean retain, int qos) {
        new MqttReceive().init(uuid, new Service(serviceName, qos, retain), stub);
    }

    @Override
    public void invoke(String serviceName, String functionName, Object ...paramValues) {
        String msg = Marshaller.pack(getClassByName(serviceName), functionName, paramValues);
        logger.info(msg);
        this.mqtt.sendMsg(serviceName, msg, this.qos, this.retain);
    }

    public Class<?> getClassByName(String serviceName) {
        Class<?> aClass = null;
        // logger.info(serviceName);
        try {
            aClass = Class.forName(serviceName.split(ConfigFile.SEPARATOR_NETWORK_CONCAT)[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return aClass;
    }


}

