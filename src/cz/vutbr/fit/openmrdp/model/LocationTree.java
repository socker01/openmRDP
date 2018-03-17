package cz.vutbr.fit.openmrdp.model;

import com.sun.istack.internal.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
final class LocationTree {

    private Node root;

    LocationTree(String rootAddress) {
        this.root = new Node(rootAddress, null);
    }

    void addTopLevelLocations(Set<String> locations){
        for (String location : locations){
            this.root.getChildren().add(new Node(location, root));
        }
    }

    Set<String> getLeafs(){
        Set<String> leafs = new HashSet<>();

        for (Node child : root.getChildren()){
            if(child.getChildren().isEmpty()){
                leafs.add(child.getData());
            }else{
                leafs.addAll(getLeafs(child));
            }
        }

        return leafs;
    }

    private Set<String> getLeafs(Node child){
        Set<String> leafs = new HashSet<>();

        for (Node childOfChild : child.getChildren()){
            if(childOfChild.getChildren().isEmpty()){
                leafs.add(childOfChild.getData());
            }else{
                leafs.addAll(getLeafs(childOfChild));
            }
        }

        return leafs;
    }

    void addLocation(RDFTriple locationInformation){
        Node location = findObjectInLocationTree(root, locationInformation.getSubject());

        if(location == null){
            addLocationInformation(locationInformation);
        }else{
            replaceLocation(locationInformation, location);
        }
    }

    private void replaceLocation(RDFTriple locationInformation, Node location) {
        Node newParent = findObjectInLocationTree(root, locationInformation.getObject());
        if (newParent != null){
            for (Node child : location.getChildren()){
                child.setParent(location.getParent());
            }
            location.getParent().getChildren().remove(location);
            location.setParent(newParent);
            newParent.getChildren().add(location);
        }else{
            throw new IllegalStateException("Locations in the tree are not correct.");
        }
    }

    private void addLocationInformation(RDFTriple triple){
        Node foundedObject = findObjectInLocationTree(root, triple.getObject());
        if(foundedObject == null){
            this.root.getChildren().add(new Node(triple.getSubject(), root));
        }else{
            foundedObject.getChildren().add(new Node(triple.getSubject(), foundedObject));
        }
    }

    @Nullable
    private Node findObjectInLocationTree(Node node, String object){
        Set<Node> children = node.getChildren();

        if(children.isEmpty()){
            return null;
        }

        for(Node child : children){
            if(child.getData().equals(object)){
                return child;
            }else{
                Node foundedNode = findObjectInLocationTree(child, object);
                if(foundedNode != null){
                    return foundedNode;
                }
            }
        }

        return null;
    }

    @Nullable
    String findLocation(String resourceName){

        Node foundedResource = findObjectInLocationTree(root, resourceName);

        if(foundedResource == null){
            return null;
        }

        return constructResourcePath(foundedResource);
    }

    private String constructResourcePath(Node locationNode){

        if(root.getData().equals(locationNode.getParent().getData())){
            return locationNode.getData();
        }

        return constructResourcePath(locationNode.getParent()) + InfoManager.PATH_PREDICATE + locationNode.getData();
    }

    private static class Node {
        private String data;
        private Node parent;
        private Set<Node> children;

        Node(String data, Node parent) {
            this.data = data;
            this.parent = parent;
            this.children = new HashSet<>();
        }

        void setParent(Node parent) {
            this.parent = parent;
        }

        String getData() {
            return data;
        }

        Node getParent() {
            return parent;
        }

        Set<Node> getChildren() {
            return children;
        }
    }
}
