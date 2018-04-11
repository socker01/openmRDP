package cz.vutbr.fit.openmrdp.model.base;

import com.sun.istack.internal.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 07.04.2018.
 */
public class Tree {

    protected Node root;

    public Tree(String rootValue) {
        this.root = new Node(rootValue, null);
    }

    @Nullable
    protected Node findNodeInTree(Node node, String key) {
        Set<Node> children = node.getChildren();

        if (children.isEmpty()) {
            return null;
        }

        for (Node child : children) {
            if (child.getData().equals(key)) {
                return child;
            } else {
                Node foundedNode = findNodeInTree(child, key);
                if (foundedNode != null) {
                    return foundedNode;
                }
            }
        }

        return null;
    }

    protected static final class Node {
        private String data;
        private Node parent;
        private Set<Node> children;

        public Node(String data, Node parent) {
            this.data = data;
            this.parent = parent;
            this.children = new HashSet<>();
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public String getData() {
            return data;
        }

        public Node getParent() {
            return parent;
        }

        public Set<Node> getChildren() {
            return children;
        }
    }
}