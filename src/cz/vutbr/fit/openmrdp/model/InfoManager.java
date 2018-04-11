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

    private boolean initialized = false;
    private Set<RDFTriple> informationBase = new HashSet<>();
    private final InformationBaseService informationBaseService;
    private final LocationTreeService locationTreeService;
    private final InformationBaseCreator informationBaseCreator;
    private final String locationPredicate;
    private final String pathPredicate;

    public InfoManager(InformationBaseService informationBaseService, OntologyService ontologyService) {
        this.informationBaseService = informationBaseService;
        this.informationBaseCreator = new InformationBaseCreator(informationBaseService, ontologyService);
        this.locationPredicate = informationBaseCreator.getLevelUpPredicate();
        this.pathPredicate = informationBaseCreator.getLevelDownPredicate();
        this.locationTreeService = new LocationTreeService(pathPredicate);

        createInformationBase();
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

    void addInformationToBase(String subject, String predicate, String object) {
        RDFTriple triple = new RDFTriple(subject, predicate, object);

        if (!informationBase.contains(triple)) {
            informationBase.add(triple);
            informationBaseService.addInformationToBase(triple);
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

    public String getPathPredicate() {
        return pathPredicate;
    }

    public String getLocationPredicate() {
        return locationPredicate;
    }
}
