package sample.MQTT;

import org.eclipse.paho.client.mqttv3.MqttException;
import sample.DataBases.Terminals.TerminalTree;


public class ConnectivityHandler {


    public ConnectivityHandler(){
        // Initialize Connectivity Components
        TerminalTree terminal_tree = TerminalTree.getInstance();
        Mqtt_publisher pub = Mqtt_publisher.getInstance();
        Mqtt_subscriber sub = Mqtt_subscriber.getInstance();
        new Thread(sub::subscribe_Availability).start();
        new Thread(terminal_tree::SubscribeToAllTerminals).start();


    }

    static void print_exception(MqttException me2)
    {
        System.out.println("reason	" + me2.getReasonCode());
        System.out.println("msg " + me2.getMessage());
        System.out.println("loc " + me2.getLocalizedMessage());
        System.out.println("cause	" + me2.getCause());
        System.out.println("exception " + me2);
        me2.printStackTrace();
    }


}
