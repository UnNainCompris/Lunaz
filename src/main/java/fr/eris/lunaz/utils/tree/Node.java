package fr.eris.lunaz.utils.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    @Getter @Setter private Node<T> parentNode;
    @Getter @Setter private T nodeValue;
    @Getter @Setter private List<Node<T>> children;

    public Node(Node<T> parentNode, T nodeValue) {
        this.parentNode = parentNode;
        this.nodeValue = nodeValue;
        this.children = new ArrayList<>();
    }

    public Node<T> addChildren(Node<T> child) {
        children.add(child);
        return this;
    }

    public Node<T> findChildByValue(T value) {
        for(Node<T> child : children)
            if(child.getNodeValue().equals(value))
                return child;
        return null;
    }
}
