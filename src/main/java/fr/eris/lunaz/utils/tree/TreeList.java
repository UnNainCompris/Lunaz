package fr.eris.lunaz.utils.tree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TreeList<T> {

    @Getter private List<Node<T>> nodes;

    public TreeList() {
        nodes = new ArrayList<>();
    }

    public Node<T> findNodeByValue(T value) {
        for(Node<T> node : nodes)
            if(node.getNodeValue().equals(value))
                return node;
        return null;
    }

    public void addNode(Node<T> node) {
        nodes.add(node);
    }
}
