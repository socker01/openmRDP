package cz.vutbr.fit.openmrdp.model.informationbase;

import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyInformation;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 07.04.2018.
 */
public final class InformationBaseCreator {

    private static final String DEFAULT_LEVEL_UP_PATH_PREDICATE = "<loc:locatedIn>";
    private static final String DEFAULT_LEVEL_DOWN_PATH_PREDICATE = "<loc:contains>";

    private final InformationBaseService informationBaseService;
    private OntologyInformation ontologyInformation;

    public InformationBaseCreator(InformationBaseService informationBaseService, OntologyService ontologyService) {
        this.informationBaseService = informationBaseService;
        this.ontologyInformation = ontologyService.loadOntology();
    }

    public Set<RDFTriple> createInformationBase() {
        Set<RDFTriple> informationBase = informationBaseService.loadInformationBase();

        TransitivePredicateTree predicateTree = initializeTransitivePredicateTree();

        createInformationBaseWithTransitivePredicates(informationBase, predicateTree.getTransitivePredicatesList());

        return informationBase;
    }

    private TransitivePredicateTree initializeTransitivePredicateTree() {
        return new TransitivePredicateTree(ontologyInformation.getTransitivePredicates());
    }

    public String getLevelUpPredicate() {
        return ontologyInformation.getLevelUpPredicate() == null ? DEFAULT_LEVEL_UP_PATH_PREDICATE : ontologyInformation.getLevelUpPredicate();
    }

    public String getLevelDownPredicate() {
        return ontologyInformation.getLevelDownPredicate() == null ? DEFAULT_LEVEL_DOWN_PATH_PREDICATE : ontologyInformation.getLevelDownPredicate();
    }

    private void createInformationBaseWithTransitivePredicates(Set<RDFTriple> informationBase, List<List<String>> transitivePredicates) {
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

    private Set<RDFTriple> findAllTriplesWithPredicate(Set<RDFTriple> informationBase, @Nullable String predicate) {
        Set<RDFTriple> triplesWithSpecificPredicate = new HashSet<>();

        for (RDFTriple triple : informationBase) {
            if (triple.getPredicate().equals(predicate)) {
                triplesWithSpecificPredicate.add(triple);
            }
        }

        return triplesWithSpecificPredicate;
    }

    private Set<RDFTriple> getTriplesForNewInformation(RDFTriple tripleWithPredicate, String transitiveRelation, Set<RDFTriple> informationBase) {
        Set<RDFTriple> triplesForNewInformation = new HashSet<>();

        for (RDFTriple information : informationBase) {
            if (information.getPredicate().equals(transitiveRelation) && information.getSubject().equals(tripleWithPredicate.getObject())) {
                triplesForNewInformation.add(information);
            }
        }

        return triplesForNewInformation;
    }
}
