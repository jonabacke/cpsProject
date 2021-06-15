package ComModule;

import Config.ConfigFile;
import org.eclipse.paho.client.mqttv3.*;

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
            this.publisher = new MqttClient(ConfigFile.BROKER_ADDRESS, uuid);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setConnectionTimeout(10);
        options.setMaxInflight(1);
        logger.info("listen on: " + this.topic);
        publisher.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.severe("Connection Lost: " + throwable.getMessage() + " " + throwable.getCause());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) {
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
        try {
            publisher.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void callBack(String msg) {
        Wrapper wrapper = Marshaller.unpack(msg);
        Logger.getGlobal().info("Receive msg: " + msg + " on " + wrapper.getClassName());
        stub.call(wrapper.getMethodName(), wrapper.getObjectParams(), wrapper.getClassParams());
    }
}
