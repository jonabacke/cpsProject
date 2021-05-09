package ComModule;

import Config.ConfigFile;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MqttReceive {
    private static final Logger logger = Logger.getGlobal();

    IMqttClient publisher = null;
    int qos;
    boolean retain;
    String topic;
    IMiddlewareCallableStub stub;

    public MqttReceive() {
    }

    public void init(String uuid, Service service, IMiddlewareCallableStub stub) {
        this.stub = stub;
        this.qos = service.getQos();
        this.retain = service.isRetain();
        this.topic = service.getServiceName();
        try {
            this.publisher = new MqttClient("tcp://localhost:1883", uuid);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        publisher.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.severe("Connection Lost: " + throwable.getMessage());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                logger.info(new String(mqttMessage.getPayload()));
                callBack(new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                logger.info("Delivery complete");
            }
        });
        try {
            publisher.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void callBack(String msg) {
        Wrapper wrapper = Marshaller.unpack(msg);
        Logger.getGlobal().info("Receive msg: " + msg);
        stub.call(wrapper.getMethodName(), wrapper.getObjectParams(), wrapper.getClassParams());
    }
}
