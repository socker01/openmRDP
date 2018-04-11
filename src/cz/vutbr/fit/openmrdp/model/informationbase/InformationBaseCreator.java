package cz.vutbr.fit.openmrdp.model.informationbase;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyInformation;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyService;

import java.util.HashSet;
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

    public Set<RDFTriple> createInformationBase(){
        Set<RDFTriple> informationBase = informationBaseService.loadInformationBase();

        TransitivePredicateTree predicateTree = initializeTransitivePredicateTree();


        return informationBase;
//        informationBase = addTransitivePredicates(informationBase);
    }

    private TransitivePredicateTree initializeTransitivePredicateTree(){
        return new TransitivePredicateTree(ontologyInformation.getTransitivePredicates());
    }

    public String getLevelUpPredicate(){
        return ontologyInformation.getLevelUpPredicate() == null ? DEFAULT_LEVEL_UP_PATH_PREDICATE : ontologyInformation.getLevelUpPredicate();
    }

    public String getLevelDownPredicate(){
        return ontologyInformation.getLevelDownPredicate() == null ? DEFAULT_LEVEL_DOWN_PATH_PREDICATE : ontologyInformation.getLevelDownPredicate();
    }


}
