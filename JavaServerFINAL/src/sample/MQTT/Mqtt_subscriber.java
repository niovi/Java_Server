package sample.MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Mqtt_subscriber implements MqttCallback {

    private static Mqtt_subscriber subscriber;

    private int qos = 2;
    private String broker = "tcp://localhost:1883";
    private String clientId = "JavaServerSubscriber";
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient sampleClient;


    private Mqtt_subscriber() {
        create_client();
    }



    public static Mqtt_subscriber getInstance() {
        if (subscriber == null) {
            subscriber = new Mqtt_subscriber();
        }
        return subscriber;
    }

    private void create_client(){

        try {
            //Connect	client	to	MQTT	Broker
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            //Set	callback
            sampleClient.setCallback(this);
            System.out.println("Connecting	to	broker:	" + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

        } catch (MqttException me) {
            ConnectivityHandler.print_exception(me);
        }

    }

    void subscribe_Availability() {

        //Subscribe	to Availability
        try {
            sampleClient.subscribe("Availability",qos);
            System.out.println("Subscribing	to	topic:	\"Availability\"	qos: " + qos );

        } catch (MqttException me) {
            ConnectivityHandler.print_exception(me);
        }

    }

    public void subscribe_Terminal(String add_uuid) {

        //Subscribe	to	a	topic
        try {
            sampleClient.subscribe(add_uuid,qos);
            System.out.println("Subscribing	to	topic	\"" + add_uuid + "\"	qos " + qos );

        } catch (MqttException me) {
            ConnectivityHandler.print_exception(me);
        }

    }

    public void unsubscribe_Terminal(String drop_uuid) {

        try {
            sampleClient.unsubscribe(drop_uuid);
            System.out.println("UnSubscribing	from	topic	\"" + drop_uuid  );

        } catch (MqttException me) {
            ConnectivityHandler.print_exception(me);
        }

    }


    public void disconnect(){

        try {
            if (sampleClient.isConnected())
            sampleClient.disconnect();
            System.out.println("Disconnected");

        } catch (MqttException me2) {
            ConnectivityHandler.print_exception(me2);
        }

    }




    public void connectionLost(Throwable cause) {
    //	This	method	is	called	when	the	connection	to	the	server	is	lost.
        System.out.println("Connection	lost!" + cause);

    }


    public void deliveryComplete(IMqttDeliveryToken token) {
    //Called	when	delivery	for	a	message	has	been	completed,	and	all	acknowledgments	have	been	received
    }


    public void messageArrived(String topic, MqttMessage message) throws MqttException {
    //This	method	is	called	when	a	message	arrives	from	the	server.

        // Get current time
        String time = new java.sql.Timestamp(System.currentTimeMillis()).toString();
        /*
        System.out.println("Time:\t" + time +
                "		Topic:\t" + topic +
                "		Message:\t" + new String(message.getPayload()) +
                "		QoS:\t" + message.getQos());
        */

        String in_mess =  time+","+topic+","+ new String(message.getPayload());
        //System.out.println("all data: "+in_mess);

        // Pass incoming message to MessageSort for Processing
        MessageSort.sorting(in_mess);

    }



    }

