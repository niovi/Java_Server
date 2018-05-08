package sample.DataBases.Terminals;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private final List<Node> children = new ArrayList<>();
    private final Node parent;

    public Node(Node parent) {
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }


    public static Node addChild(Node parent, String id) {
         Node node = new Node(parent);
         node.setId(id);
         parent.getChildren().add(node);
         return node;
    }

    public void removeChild(){
        parent.getChildren().remove(this);

    }

}
