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

    LocationTree(String rootAddress) {
        super(rootAddress);
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
        Node foundedObject = findNodeInTree(root, triple.getObject());
        if (foundedObject == null) {
            root.getChildren().add(new Node(triple.getSubject(), root));
        } else {
            foundedObject.getChildren().add(new Node(triple.getSubject(), foundedObject));
        }
    }

    @Nullable
    String findLocation(String resourceName) {

        Node foundedResource = findNodeInTree(root, resourceName);

        if (foundedResource == null) {
            return null;
        }

        return constructResourcePath(foundedResource);
    }

    private String constructResourcePath(Node locationNode) {

        if (root.getData().equals(locationNode.getParent().getData())) {
            return locationNode.getData();
        }

        return constructResourcePath(locationNode.getParent()) + InfoManager.PATH_PREDICATE + locationNode.getData();
    }
}
