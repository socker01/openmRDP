package cz.vutbr.fit.openmrdp.model.ontology;

import javafx.util.Pair;

import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 06.04.2018.
 */
public final class OntologyInformation {

    private final String levelUpPredicate;
    private final String levelDownPredicate;
    private final List<Pair> transitivePredicates;

    OntologyInformation(String levelUpPredicate, String levelDownPredicate, List<Pair> transitivePredicates) {
        this.levelUpPredicate = levelUpPredicate;
        this.levelDownPredicate = levelDownPredicate;
        this.transitivePredicates = transitivePredicates;
    }
}
