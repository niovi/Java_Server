package sample;


import javafx.collections.ObservableList;
import sample.DataBases.Terminals.TerminalTree;
import sample.MQTT.Mqtt_publisher;
import sample.MQTT.Mqtt_subscriber;

 class ExitHandler {


     ExitHandler() {

        // Get pub/sub instances
        Mqtt_subscriber sub = Mqtt_subscriber.getInstance();
        Mqtt_publisher pub = Mqtt_publisher.getInstance();
        //System.out.println("got pub/sub instances");

        // Observable list of terminals
        ObservableList<String> terms;
        TerminalTree term_tree =TerminalTree.getInstance();
        terms = term_tree.TerminalsAvailable();
        //System.out.println("got all terminals available");

        // Announce termination of session
        pub.terminate(terms);
        //System.out.println("terminals terminated");

        // Remove all terminals and disconnect pub/sub
        term_tree.clear_tree();
        sub.disconnect();
        pub.disconnect();

}

}
