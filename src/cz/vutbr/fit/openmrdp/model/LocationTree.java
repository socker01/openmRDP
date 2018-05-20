package cz.vutbr.fit.openmrdp.model;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.Tree;

import java.util.HashSet;
import java.util.Set;

/**
 * The subclass of the {@link Tree} which contains loaded information about resource locations.
 *
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
final class LocationTree extends Tree {

    @NotNull
    private final String delimiter;

    LocationTree(@NotNull String rootAddress, @NotNull String delimiter) {
        super(rootAddress);
        this.delimiter = delimiter;
    }

    void addTopLevelLocations(@NotNull Set<String> locations) {
        for (String location : locations) {
            root.getChildren().add(new Node(location, root));
        }
    }

    @NotNull
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

    @NotNull
    private Set<String> getLeafs(@NotNull Node child) {
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

    void addLocation(@NotNull RDFTriple locationInformation) {
        Node location = findNodeInTree(root, locationInformation.getSubject());

        if (location == null) {
            addLocationInformation(locationInformation);
        } else {
            replaceLocation(locationInformation, location);
        }
    }

    void removeLocation(@NotNull RDFTriple locationInformation) {
        Node location = findNodeInTree(root, locationInformation.getSubject());

        if (location != null) {
            Set<Node> children = location.getChildren();
            location.getParent().getChildren().remove(location);

            for (Node child : children) {
                if (!child.getChildren().isEmpty()) {
                    child.setParent(root);
                    root.getChildren().add(child);
                }
            }
        }
    }

    private void replaceLocation(@NotNull RDFTriple locationInformation, @NotNull Node location) {
        Node newParent = findNodeInTree(root, locationInformation.getObject());
        if (newParent != null) {
            location.getChildren().forEach(child -> child.setParent(location.getParent()));
            location.getParent().getChildren().remove(location);
            location.setParent(newParent);
            newParent.getChildren().add(location);
        } else {
            throw new IllegalStateException("Locations in the tree are not correct.");
        }
    }

    private void addLocationInformation(@NotNull RDFTriple triple) {
        Node foundObject = findNodeInTree(root, triple.getObject());
        if (foundObject == null) {
            root.getChildren().add(new Node(triple.getSubject(), root));
        } else {
            foundObject.getChildren().add(new Node(triple.getSubject(), foundObject));
        }
    }

    @Nullable
    String findLocation(@NotNull String resourceName) {

        Node foundResource = findNodeInTree(root, resourceName);

        if (foundResource == null) {
            return null;
        }

        return constructResourcePath(foundResource);
    }

    @NotNull
    private String constructResourcePath(@NotNull Node locationNode) {

        if (root.getData().equals(locationNode.getParent().getData())) {
            return locationNode.getData();
        }

        return constructResourcePath(locationNode.getParent()) + delimiter + locationNode.getData();
    }
}
