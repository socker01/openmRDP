package cz.vutbr.fit.openmrdp.model;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseCreator;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyService;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class InfoManager {

    private static InfoManager instance = null;

    private boolean initialized = false;
    private Set<RDFTriple> informationBase = new HashSet<>();
    private final InformationBaseService informationBaseService;
    private final LocationTreeService locationTreeService;
    private final InformationBaseCreator informationBaseCreator;
    private final String locationPredicate;

    private InfoManager(InformationBaseService informationBaseService, OntologyService ontologyService) {
        this.informationBaseService = informationBaseService;
        this.informationBaseCreator = new InformationBaseCreator(informationBaseService, ontologyService);
        this.locationPredicate = informationBaseCreator.getLevelUpPredicate();
        this.locationTreeService = new LocationTreeService(informationBaseCreator.getDelimiter(), informationBaseCreator.getLevelUpPredicate());

        createInformationBase();
    }

    public static InfoManager getInfoManager(InformationBaseService informationBaseService, OntologyService ontologyService) {
        if (instance == null) {
            instance = new InfoManager(informationBaseService, ontologyService);
        }

        return instance;
    }

    private void createInformationBase() {
        if (!initialized) {
            this.informationBase = informationBaseCreator.createInformationBase();
            initialized = true;

            locationTreeService.createLocationTree(getLocationInformation());
        }
    }

    private Set<RDFTriple> getLocationInformation() {
        Set<RDFTriple> locationInformation = new HashSet<>();

        for (RDFTriple triple : informationBase) {
            if (triple.getPredicate().equals(locationPredicate)) {
                locationInformation.add(triple);
            }
        }

        return locationInformation;
    }

    private boolean isPredicateLocation(String predicate) {
        return predicate.equals(locationPredicate) || predicate.equals(informationBaseCreator.getLevelDownPredicate());
    }

    public void removeInformationFromBase(RDFTriple information){
        if(isPredicateLocation(information.getPredicate())) {
            RDFTriple symmetricalTriple = informationBaseCreator.createSymmetricalLocationInformation(information);
            removeInformation(symmetricalTriple);
        }

        removeInformation(information);

    }

    private void removeInformation(RDFTriple information) {
        if(informationBase.contains(information)){
            informationBase.remove(information);
            informationBaseService.removeInformationFromBase(information);
            locationTreeService.removeLocationInformation(information);
        }
    }

    public void addInformationToBase(RDFTriple information) {
        if (isPredicateLocation(information.getPredicate())) {
            RDFTriple symmetricalTriple = informationBaseCreator.createSymmetricalLocationInformation(information);
            createAndAddInformation(symmetricalTriple);
        }

        createAndAddInformation(information);
    }

    private void createAndAddInformation(RDFTriple information) {
        if (!informationBase.contains(information)) {
            informationBase.add(information);
            informationBaseService.addInformationToBase(information);
            locationTreeService.addLocationInformation(information);
        }
    }

    public Set<RDFTriple> findMatchingPatterns(RDFTriple findingTriple) {

        Set<RDFTriple> matchingPatterns = new HashSet<>();

        for (RDFTriple triple : informationBase) {
            if (triple.equals(findingTriple)
                    || matchWithVariableInObject(triple, findingTriple)
                    || matchWithVariableInSubject(triple, findingTriple)
                    || matchWithTwoVariables(triple, findingTriple)) {
                matchingPatterns.add(triple);
            }
        }

        return matchingPatterns;
    }

    @Nullable
    public String findResourceLocation(@NotNull String resourceName) {
        Preconditions.checkNotNull(resourceName);
        return locationTreeService.findResourceLocation(resourceName);
    }

    private boolean matchWithVariableInObject(RDFTriple referenceTriple, RDFTriple findingTriple) {
        return referenceTriple.getObject().equals(findingTriple.getObject())
                && referenceTriple.getPredicate().equals(findingTriple.getPredicate())
                && findingTriple.isSubjectVariable();
    }

    private boolean matchWithVariableInSubject(RDFTriple referenceTriple, RDFTriple findingTriple) {
        return findingTriple.isObjectVariable()
                && referenceTriple.getPredicate().equals(findingTriple.getPredicate())
                && referenceTriple.getSubject().equals(findingTriple.getSubject());
    }

    private boolean matchWithTwoVariables(RDFTriple referenceTriple, RDFTriple findingTriple) {
        return findingTriple.isObjectVariable()
                && findingTriple.getPredicate().equals(referenceTriple.getPredicate())
                && findingTriple.isSubjectVariable();
    }

    private boolean verifyFact(RDFTriple factToVerify) {
        return informationBase.contains(factToVerify);
    }

    public boolean verifyFacts(Set<RDFTriple> factsToVerify) {
        for (RDFTriple fact : factsToVerify) {
            if (!verifyFact(fact)) {
                return false;
            }
        }

        return true;
    }
}
