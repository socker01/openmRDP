package cz.vutbr.fit.openmrdp.model.ontology;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.util.Pair;

import java.util.List;

/**
 * Domain object which contains information loaded via {@link cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseService}
 *
 * @author Jiri Koudelka
 * @since 06.04.2018.
 */
public final class OntologyInformation {

    @Nullable
    private final String levelUpPredicate;
    @Nullable
    private final String levelDownPredicate;
    @Nullable
    private final String delimiter;
    @Nullable
    private final List<Pair<String, String>> transitivePredicates;

    private OntologyInformation(Builder builder) {
        this.levelUpPredicate = builder.levelUpPredicate;
        this.levelDownPredicate = builder.levelDownPredicate;
        this.delimiter = builder.delimiter;
        this.transitivePredicates = Preconditions.checkNotNull(builder.transitivePredicates);
    }

    static class Builder {
        @Nullable
        private String levelUpPredicate;
        @Nullable
        private String levelDownPredicate;
        @Nullable
        private String delimiter;
        @Nullable
        private List<Pair<String, String>> transitivePredicates;

        Builder withLevelUpPredicate(String levelUpPredicate) {
            this.levelUpPredicate = levelUpPredicate;
            return this;
        }

        Builder withLevelDownPredicate(String levelDownPredicate) {
            this.levelDownPredicate = levelDownPredicate;
            return this;
        }

        Builder withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        Builder withTransitivePredicates(List<Pair<String, String>> transitivePredicates) {
            this.transitivePredicates = transitivePredicates;
            return this;
        }

        OntologyInformation build() {
            return new OntologyInformation(this);
        }
    }

    @Nullable
    public String getLevelUpPredicate() {
        return levelUpPredicate;
    }

    @Nullable
    public String getLevelDownPredicate() {
        return levelDownPredicate;
    }

    @Nullable
    public String getDelimiter() {
        return delimiter;
    }

    @Nullable
    public List<Pair<String, String>> getTransitivePredicates() {
        return transitivePredicates;
    }
}
