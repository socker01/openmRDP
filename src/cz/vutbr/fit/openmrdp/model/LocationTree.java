package cz.vutbr.fit.openmrdp.model;

import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.Tree;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
final class LocationTree extends Tree {

    private final String delimiter;

    LocationTree(String rootAddress, String delimiter) {
        super(rootAddress);
        this.delimiter = delimiter;
    }

    void addTopLevelLocations(Set<String> locations) {
        for (String location : locations) {
            root.getChildren().add(new Node(location, root));
        }
    }

    Set<String> getLeafs() {
        Set<String> leafs = new HashSet<>();

        for (Node child : root.getChildren()) {
            if (child.getChildren().isEmpty()) {
                leafs.add(child.getData());
            } else {
                leafs.addAll(getLeafs(child));
            }
        }

        return leafs;
    }

    private Set<String> getLeafs(Node child) {
        Set<String> leafs = new HashSet<>();

        for (Node childOfChild : child.getChildren()) {
            if (childOfChild.getChildren().isEmpty()) {
                leafs.add(childOfChild.getData());
            } else {
                leafs.addAll(getLeafs(childOfChild));
            }
        }

        return leafs;
    }

    void addLocation(RDFTriple locationInformation) {
        Node location = findNodeInTree(root, locationInformation.getSubject());

        if (location == null) {
            addLocationInformation(locationInformation);
        } else {
            replaceLocation(locationInformation, location);
        }
    }

    void removeLocation(RDFTriple locationInformation){
        Node location = findNodeInTree(root, locationInformation.getSubject());

        if(location != null){
            Set<Node> children = location.getChildren();
            location.getParent().getChildren().remove(location);

            for(Node child : children){
                if(!child.getChildren().isEmpty()){
                    child.setParent(root);
                    root.getChildren().add(child);
                }
            }
        }
    }

    private void replaceLocation(RDFTriple locationInformation, Node location) {
        Node newParent = findNodeInTree(root, locationInformation.getObject());
        if (newParent != null) {
            for (Node child : location.getChildren()) {
                child.setParent(location.getParent());
            }
            location.getParent().getChildren().remove(location);
            location.setParent(newParent);
            newParent.getChildren().add(location);
        } else {
            throw new IllegalStateException("Locations in the tree are not correct.");
        }
    }

    private void addLocationInformation(RDFTriple triple) {
        Node foundObject = findNodeInTree(root, triple.getObject());
        if (foundObject == null) {
            root.getChildren().add(new Node(triple.getSubject(), root));
        } else {
            foundObject.getChildren().add(new Node(triple.getSubject(), foundObject));
        }
    }

    @Nullable
    String findLocation(String resourceName) {

        Node foundResource = findNodeInTree(root, resourceName);

        if (foundResource == null) {
            return null;
        }

        return constructResourcePath(foundResource);
    }

    private String constructResourcePath(Node locationNode) {

        if (root.getData().equals(locationNode.getParent().getData())) {
            return locationNode.getData();
        }

        return constructResourcePath(locationNode.getParent()) + delimiter + locationNode.getData();
    }
}
