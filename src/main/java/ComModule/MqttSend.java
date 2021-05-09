package ComModule;

import Config.ConfigFile;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MqttSend {

    private IMqttClient publisher = null;

    public MqttSend(String uuid) {
        try {
            this.publisher = new MqttClient(ConfigFile.BROKERADDRESS, uuid);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

    }


    public void sendMsg(String topic, String str, int qos, boolean retain) {
        MqttMessage msg = new MqttMessage(str.getBytes(StandardCharsets.UTF_8));
        msg.setQos(qos);
        msg.setRetained(retain);
        try {
            this.publisher.publish(topic, msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
