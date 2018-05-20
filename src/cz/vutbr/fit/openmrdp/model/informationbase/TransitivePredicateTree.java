package cz.vutbr.fit.openmrdp.model.informationbase;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.model.base.Tree;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The subclass of the {@link Tree} which is used for storing of the transitive predicates.
 *
 * @author Jiri Koudelka
 * @since 07.04.2018.
 */
final class TransitivePredicateTree extends Tree {

    private static final String PREDICATE_TREE_ROOT_NAME = "Predicates";

    private List<List<String>> transitivePredicatesList;

    TransitivePredicateTree(@NotNull List<Pair<String, String>> transitivePredicates) {
        super(PREDICATE_TREE_ROOT_NAME);
        transitivePredicatesList = new ArrayList<>();
        initializePredicateTree(transitivePredicates);
    }

    private void initializePredicateTree(@NotNull List<Pair<String, String>> transitivePredicates) {

        for (Pair<String, String> transitivePredicate : transitivePredicates) {
            Node foundPredicate = findNodeInTree(root, transitivePredicate.getKey());

            if (foundPredicate == null) {
                createNewTransitivePredicate(transitivePredicate);
            } else {
                addTransitiveRelation(transitivePredicate, foundPredicate);
            }
        }

        createTransitivePredicateList();
    }

    private void addTransitiveRelation(@NotNull Pair<String, String> transitivePredicate, @NotNull Node foundPredicate) {
        Node transitiveRelation = new Node(transitivePredicate.getValue(), foundPredicate);
        foundPredicate.getChildren().add(transitiveRelation);
    }

    private void createNewTransitivePredicate(@NotNull Pair<String, String> transitivePredicate) {
        Node newTransitivePredicate = new Node(transitivePredicate.getKey(), root);
        root.getChildren().add(newTransitivePredicate);
        addTransitiveRelation(transitivePredicate, newTransitivePredicate);
    }

    private void createTransitivePredicateList() {
        for (Node child : root.getChildren()) {
            List<String> predicates = new ArrayList<>();
            predicates.add(child.getData());

            if (child.getChildren().isEmpty()) {
                transitivePredicatesList.add(predicates);
            } else {
                createTransitivePredicate(child, predicates);
            }
        }
    }

    private void createTransitivePredicate(@NotNull Node node, @NotNull List<String> currentTransitivePredicates) {
        for (Node child : node.getChildren()) {
            List<String> predicates = new ArrayList<>(currentTransitivePredicates);
            predicates.add(child.getData());

            if (child.getChildren().isEmpty()) {
                transitivePredicatesList.add(predicates);
            } else {
                createTransitivePredicate(child, predicates);
            }
        }
    }

    @NotNull
    List<List<String>> getTransitivePredicatesList() {
        List<List<String>> toReturn = new ArrayList<>();
        for (List<String> transitivePredicate : transitivePredicatesList) {
            toReturn.add(new LinkedList<>(transitivePredicate));
        }

        return toReturn;
    }
}
