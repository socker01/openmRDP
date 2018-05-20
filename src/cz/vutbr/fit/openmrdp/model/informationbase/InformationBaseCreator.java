package cz.vutbr.fit.openmrdp.model.informationbase;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyInformation;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class creates information base for {@link cz.vutbr.fit.openmrdp.model.InfoManager}.
 * <p>
 * It also resolves transitive and symmetrical relations.
 *
 * @author Jiri Koudelka
 * @since 07.04.2018.
 */
public final class InformationBaseCreator {

    private static final String DEFAULT_LEVEL_UP_PATH_PREDICATE = "<loc:locatedIn>";
    private static final String DEFAULT_LEVEL_DOWN_PATH_PREDICATE = "<loc:contains>";
    private static final String DEFAULT_DELIMITER = "/";

    @NotNull
    private final InformationBaseService informationBaseService;
    @NotNull
    private OntologyInformation ontologyInformation;

    public InformationBaseCreator(@NotNull InformationBaseService informationBaseService, @NotNull OntologyService ontologyService) {
        this.informationBaseService = Preconditions.checkNotNull(informationBaseService);
        this.ontologyInformation = Preconditions.checkNotNull(ontologyService.loadOntology());
    }

    @NotNull
    public Set<RDFTriple> createInformationBase() {
        Set<RDFTriple> informationBase = informationBaseService.loadInformationBase();

        TransitivePredicateTree predicateTree = initializeTransitivePredicateTree();

        createInformationBaseWithTransitivePredicates(informationBase, predicateTree.getTransitivePredicatesList());

        addSymmetricalLocationInformation(informationBase);

        return informationBase;
    }

    private void addSymmetricalLocationInformation(@NotNull Set<RDFTriple> informationBase) {
        Set<RDFTriple> locationInformation = informationBase
                .stream()
                .filter(this::isLocationInformation)
                .collect(Collectors.toSet());
        Set<RDFTriple> newInformationBase = locationInformation.
                stream()
                .map(this::createSymmetricalLocationInformation)
                .collect(Collectors.toSet());

        newInformationBase.addAll(locationInformation);
    }

    @NotNull
    public RDFTriple createSymmetricalLocationInformation(@NotNull RDFTriple triple) {
        String newPredicate;
        if (triple.getPredicate().equals(getLevelUpPredicate())) {
            newPredicate = getLevelDownPredicate();
        } else {
            newPredicate = getLevelUpPredicate();
        }

        return new RDFTriple(triple.getObject(), newPredicate, triple.getSubject());
    }

    private boolean isLocationInformation(@NotNull RDFTriple information) {
        return information.getPredicate().equals(getLevelUpPredicate())
                || information.getPredicate().equals(getLevelDownPredicate());
    }

    private TransitivePredicateTree initializeTransitivePredicateTree() {
        return new TransitivePredicateTree(ontologyInformation.getTransitivePredicates());
    }

    @NotNull
    public String getLevelUpPredicate() {
        return ontologyInformation.getLevelUpPredicate() == null ? DEFAULT_LEVEL_UP_PATH_PREDICATE : ontologyInformation.getLevelUpPredicate();
    }

    @NotNull
    public String getLevelDownPredicate() {
        return ontologyInformation.getLevelDownPredicate() == null ? DEFAULT_LEVEL_DOWN_PATH_PREDICATE : ontologyInformation.getLevelDownPredicate();
    }

    @NotNull
    public String getDelimiter() {
        return ontologyInformation.getDelimiter() == null ? DEFAULT_DELIMITER : ontologyInformation.getDelimiter();
    }

    private void createInformationBaseWithTransitivePredicates(@NotNull Set<RDFTriple> informationBase, @NotNull List<List<String>> transitivePredicates) {
        for (List<String> transitivePredicate : transitivePredicates) {
            String predicate = transitivePredicate.remove(0);
            Set<RDFTriple> triplesWithPredicate = findAllTriplesWithPredicate(informationBase, predicate);

            for (RDFTriple tripleWithPredicate : triplesWithPredicate) {
                for (String transitiveRelation : transitivePredicate) {
                    Set<RDFTriple> triplesForNewInformation = getTriplesForNewInformation(tripleWithPredicate, transitiveRelation, informationBase);

                    for (RDFTriple triple : triplesForNewInformation) {
                        RDFTriple newInformation = new RDFTriple(tripleWithPredicate.getSubject(), predicate, triple.getObject());

                        informationBase.add(newInformation);
                    }
                }
            }
        }
    }

    @NotNull
    private Set<RDFTriple> findAllTriplesWithPredicate(@NotNull Set<RDFTriple> informationBase, @Nullable String predicate) {
        return informationBase.stream().filter(information -> information.getPredicate().equals(predicate)).collect(Collectors.toSet());
    }

    @NotNull
    private Set<RDFTriple> getTriplesForNewInformation(@NotNull RDFTriple tripleWithPredicate, @NotNull String transitiveRelation,
                                                       @NotNull Set<RDFTriple> informationBase) {
        return informationBase
                .stream()
                .filter(information -> information.getPredicate().equals(transitiveRelation) &&
                        information.getSubject().equals(tripleWithPredicate.getObject()))
                .collect(Collectors.toSet());
    }
}
