package sample.MQTT;

import javafx.collections.ObservableList;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Mqtt_publisher {


    private static Mqtt_publisher publisher;

    private int qos =	2;
    // to find host: nmap -sn 192.168.1.0/24
    private String	broker	=	"tcp://localhost:1883";
    private String	clientId =	"JavaServerPublisher";
    private MemoryPersistence persistence =	new	MemoryPersistence();
    private MqttClient sampleClient;


    private Mqtt_publisher() {
        create_client();
    }

    public static Mqtt_publisher getInstance() {
        if (publisher == null) {
            publisher = new Mqtt_publisher();
        }
        return publisher;
    }


    private void create_client(){

        try {
            //Connect	to	MQTT	Broker
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts2 = new MqttConnectOptions();
            connOpts2.setCleanSession(true);
            System.out.println("Connecting	to	broker:	" + broker);
            sampleClient.connect(connOpts2);
            System.out.println("Connected");

        } catch (MqttException me2) {
            ConnectivityHandler.print_exception(me2);
        }

    }



    public void publish(String topic, String content) {


        try {
            //Publish	message	to	MQTT	Broker
            System.out.println("Publishing	message: topic:	"+topic+" message :" + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message	published");

        } catch (MqttException me2) {
            ConnectivityHandler.print_exception(me2);
        }
    }



    public void disconnect(){

        try {
            if(sampleClient.isConnected())
            sampleClient.disconnect();
            System.out.println("Disconnected");

        } catch (MqttException me2) {
            ConnectivityHandler.print_exception(me2);
        }

    }


    public void terminate(ObservableList<String> terms){

        // Inform all connected terminals about end of session
        terms.forEach((term_id)->{
            publish("Availability",term_id+",OFF");
        });



    }


}
