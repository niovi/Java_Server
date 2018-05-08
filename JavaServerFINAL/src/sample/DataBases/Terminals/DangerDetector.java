package sample.DataBases.Terminals;


import sample.MQTT.Mqtt_publisher;

public class DangerDetector {

    private static DangerDetector danger_detector;
    private TerminalTree terminal_tree;
    Mqtt_publisher pub;

    private DangerDetector() {
        pub = Mqtt_publisher.getInstance();
        terminal_tree = TerminalTree.getInstance();

    }

    public static DangerDetector getInstance() {
        if (danger_detector == null) {
            danger_detector = new DangerDetector();
        }
        return danger_detector;
    }

    public String CheckTerminal(String uuid){

        if(terminal_tree.IsTerminalInDanger(uuid).equals(true)){

            if(terminal_tree.OtherTerminalsInDanger(uuid).equals(true)){
                pub.publish(uuid+",ON","VERIFIED");
                return "VERIFIED";
            }
            else{ pub.publish(uuid+",ON","SIMPLE");return "SIMPLE";}

        } else {pub.publish(uuid+",ON","SAFE");return "SAFE";}


    }



}
