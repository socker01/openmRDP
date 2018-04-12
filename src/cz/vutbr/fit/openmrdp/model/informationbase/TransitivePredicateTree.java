package cz.vutbr.fit.openmrdp.model.informationbase;

import cz.vutbr.fit.openmrdp.model.base.Tree;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 07.04.2018.
 */
final class TransitivePredicateTree extends Tree {

    private static final String PREDICATE_TREE_ROOT_NAME = "Predicates";

    private List<List<String>> transitivePredicatesList;

    TransitivePredicateTree(List<Pair<String, String>> transitivePredicates) {
        super(PREDICATE_TREE_ROOT_NAME);
        transitivePredicatesList = new ArrayList<>();
        initializePredicateTree(transitivePredicates);
    }

    private void initializePredicateTree(List<Pair<String, String>> transitivePredicates) {

        for (Pair<String, String> transitivePredicate : transitivePredicates) {
            Node foundedPredicate = findNodeInTree(root, transitivePredicate.getKey());

            if (foundedPredicate == null) {
                createNewTransitivePredicate(transitivePredicate);
            } else {
                addTransitiveRelation(transitivePredicate, foundedPredicate);
            }
        }

        createTransitivePredicateList();
    }

    private void addTransitiveRelation(Pair<String, String> transitivePredicate, Node foundedPredicate) {
        Node transitiveRelation = new Node(transitivePredicate.getValue(), foundedPredicate);
        foundedPredicate.getChildren().add(transitiveRelation);
    }

    private void createNewTransitivePredicate(Pair<String, String> transitivePredicate) {
        Node newTransitivePredicate = new Node(transitivePredicate.getKey(), root);
        root.getChildren().add(newTransitivePredicate);
        addTransitiveRelation(transitivePredicate, newTransitivePredicate);
    }

    private void createTransitivePredicateList(){
        for (Node child : root.getChildren()){
            List<String> predicates = new ArrayList<>();
            predicates.add(child.getData());

            if (child.getChildren().isEmpty()){
                transitivePredicatesList.add(predicates);
            }else{
                createTransitivePredicate(child, predicates);
            }
        }
    }

    private void createTransitivePredicate(Node node, List<String> currentTransitivePredicates){
        for (Node child : node.getChildren()){
            List<String> predicates = new ArrayList<>(currentTransitivePredicates);
            predicates.add(child.getData());

            if(child.getChildren().isEmpty()){
                transitivePredicatesList.add(predicates);
            }else{
                createTransitivePredicate(child, predicates);
            }
        }
    }

    List<List<String>> getTransitivePredicatesList() {
        List<List<String>> toReturn = new ArrayList<>();
        for (List<String> transitivePredicate : transitivePredicatesList)
        {
            toReturn.add(new LinkedList<>(transitivePredicate));
        }

        return toReturn;
    }
}
