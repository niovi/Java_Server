package sample.DataBases.Terminals;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.MQTT.Mqtt_subscriber;

import java.util.Iterator;
import java.util.List;


public class TerminalTree {


    private static TerminalTree terminal_tree;
    private Node treeRootNode;


    private TerminalTree() {

        treeRootNode = new Node(null);
        treeRootNode.setId("root");

    }



    public static TerminalTree getInstance() {
        if (terminal_tree == null) {
            terminal_tree = new TerminalTree();
        }
        return terminal_tree;
    }

    public void clear_tree() {
        treeRootNode=null;
    }

    public void AddTerminal(String uuid){

        if (GetTerminal(uuid)==null){
        Mqtt_subscriber sub = Mqtt_subscriber.getInstance();

        Node.addChild(treeRootNode,uuid);
        sub.subscribe_Terminal(uuid);}

    }

    public void RemoveTerminal(String uuid){
        Mqtt_subscriber sub = Mqtt_subscriber.getInstance();

        Node TerminalNode = GetTerminal(uuid);
        TerminalNode.removeChild();
        sub.unsubscribe_Terminal(uuid);

    }


    public void SubscribeToAllTerminals(){

        Mqtt_subscriber sub = Mqtt_subscriber.getInstance();

        List<Node> children = treeRootNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node TerminalNode = (Node) itr.next();
            sub.subscribe_Terminal(TerminalNode.getId());
        }

    }

    public ObservableList<String> TerminalsAvailable(){

        ObservableList<String> terms= FXCollections.observableArrayList();
        List<Node> children = treeRootNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node TerminalNode = (Node) itr.next();
            terms.add(TerminalNode.getId());
        }
        return terms;

    }

    public Node GetTerminal(String uuid){
        List<Node> children = treeRootNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node TerminalNode = (Node) itr.next();
            if (TerminalNode.getId().equals(uuid)){
                return TerminalNode;
            }
        }
        System.out.println("Terminal not found");
        return null;

    }

    public void AddSensor(String uuid, String sensor_id){

        Node TerminalNode = GetTerminal(uuid);
        Node.addChild(TerminalNode,sensor_id);
    }

    public Node GetSensor(String uuid,String sensor_id){

        Node TerminalNode = GetTerminal(uuid);
        List<Node> children = TerminalNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node SensorNode = (Node) itr.next();
            if (SensorNode.getId().equals(sensor_id)){
                return SensorNode;
            }
        }
        AddSensor(uuid, sensor_id);
        AddDanger(uuid,sensor_id,"FALSE");
        return GetSensor(uuid, sensor_id);

    }

    public void AddDanger(String uuid, String sensor_id, String danger){

        Node SensorNode = GetSensor(uuid, sensor_id);
        Node.addChild(SensorNode,danger);
    }



    public void SetDanger(String uuid,String sensor_id, String danger){

        Node SensorNode = GetSensor(uuid,sensor_id);
        List<Node> children = SensorNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node DangerNode = (Node) itr.next();
            DangerNode.setId(danger);
        }
        System.out.println("Danger not found");


    }

    public Boolean IsTerminalInDanger(String uuid){


        Node TerminalNode = GetTerminal(uuid);
        List<Node> children = TerminalNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node SensorNode = (Node) itr.next();
            List<Node> children2 = SensorNode.getChildren();
            Iterator itr2 = children2.iterator();
            while(itr2.hasNext()) {
                Node DangerNode = (Node) itr2.next();
                String danger = DangerNode.getId();
                if(danger.equals("TRUE"))return true;
            }
        }
        System.out.println("No danger detected");
        return false;



    }

    public Boolean OtherTerminalsInDanger(String uuid){

        List<Node> children = treeRootNode.getChildren();
        Iterator itr = children.iterator();

        while(itr.hasNext()) {
            Node TerminalNode = (Node) itr.next();
            if (!TerminalNode.getId().equals(uuid)){
                if(IsTerminalInDanger(TerminalNode.getId())) return true;
            }
        }

        return false;



    }

}

