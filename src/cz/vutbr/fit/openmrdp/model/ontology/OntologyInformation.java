package cz.vutbr.fit.openmrdp.model.ontology;

import com.sun.istack.internal.Nullable;
import javafx.util.Pair;

import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 06.04.2018.
 */
public final class OntologyInformation {

    @Nullable
    private final String levelUpPredicate;
    @Nullable
    private final String levelDownPredicate;
    private final List<Pair<String, String>> transitivePredicates;

    OntologyInformation(@Nullable String levelUpPredicate, @Nullable String levelDownPredicate, List<Pair<String, String>> transitivePredicates) {
        this.levelUpPredicate = levelUpPredicate;
        this.levelDownPredicate = levelDownPredicate;
        this.transitivePredicates = transitivePredicates;
    }

    @Nullable
    public String getLevelUpPredicate() {
        return levelUpPredicate;
    }

    @Nullable
    public String getLevelDownPredicate() {
        return levelDownPredicate;
    }

    public List<Pair<String, String>> getTransitivePredicates() {
        return transitivePredicates;
    }
}
