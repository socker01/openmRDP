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
import java.util.stream.Collectors;

/**
 * Singleton Information Manager that contains loaded information about information base and provides these information to other classes.
 * <p>
 * Information manager can verify facts and add or remove information to or from information base.
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class InfoManager {

    private static InfoManager instance = null;

    private boolean initialized = false;
    @NotNull
    private Set<RDFTriple> informationBase = new HashSet<>();
    @NotNull
    private final InformationBaseService informationBaseService;
    @NotNull
    private final LocationTreeService locationTreeService;
    @NotNull
    private final InformationBaseCreator informationBaseCreator;
    @NotNull
    private final String locationPredicate;

    private InfoManager(@NotNull InformationBaseService informationBaseService, @NotNull OntologyService ontologyService) {
        this.informationBaseService = Preconditions.checkNotNull(informationBaseService);
        this.informationBaseCreator = new InformationBaseCreator(informationBaseService, Preconditions.checkNotNull(ontologyService));
        this.locationPredicate = informationBaseCreator.getLevelUpPredicate();
        this.locationTreeService = new LocationTreeService(informationBaseCreator.getDelimiter(), informationBaseCreator.getLevelUpPredicate());

        createInformationBase();
    }

    @NotNull
    public static InfoManager getInfoManager(@NotNull InformationBaseService informationBaseService, @NotNull OntologyService ontologyService) {
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

    @NotNull
    private Set<RDFTriple> getLocationInformation() {
        return informationBase.stream().filter(t -> t.getPredicate().equals(locationPredicate)).collect(Collectors.toSet());
    }

    private boolean isPredicateLocation(@NotNull String predicate) {
        return predicate.equals(locationPredicate) || predicate.equals(informationBaseCreator.getLevelDownPredicate());
    }

    public void removeInformationFromBase(@NotNull RDFTriple information) {
        if (isPredicateLocation(information.getPredicate())) {
            RDFTriple symmetricalTriple = informationBaseCreator.createSymmetricalLocationInformation(information);
            removeInformation(symmetricalTriple);
        }

        removeInformation(information);

    }

    private void removeInformation(@NotNull RDFTriple information) {
        if (informationBase.contains(information)) {
            informationBase.remove(information);
            informationBaseService.removeInformationFromBase(information);
            locationTreeService.removeLocationInformation(information);
        }
    }

    public void addInformationToBase(@NotNull RDFTriple information) {
        if (isPredicateLocation(information.getPredicate())) {
            RDFTriple symmetricalTriple = informationBaseCreator.createSymmetricalLocationInformation(information);
            createAndAddInformation(symmetricalTriple);
        }

        createAndAddInformation(information);
    }

    private void createAndAddInformation(@NotNull RDFTriple information) {
        if (!informationBase.contains(information)) {
            informationBase.add(information);
            informationBaseService.addInformationToBase(information);
            locationTreeService.addLocationInformation(information);
        }
    }

    @NotNull
    public Set<RDFTriple> findMatchingPatterns(@NotNull RDFTriple findingTriple) {
        return informationBase.stream().filter(i -> i.equals(findingTriple)
                || matchWithVariableInObject(i, findingTriple)
                || matchWithVariableInSubject(i, findingTriple)
                || matchWithTwoVariables(i, findingTriple)).collect(Collectors.toSet());
    }

    @Nullable
    public String findResourceLocation(@NotNull String resourceName) {
        Preconditions.checkNotNull(resourceName);
        return locationTreeService.findResourceLocation(resourceName);
    }

    private boolean matchWithVariableInObject(@NotNull RDFTriple referenceTriple, @NotNull RDFTriple findingTriple) {
        return referenceTriple.getObject().equals(findingTriple.getObject())
                && referenceTriple.getPredicate().equals(findingTriple.getPredicate())
                && findingTriple.isSubjectVariable();
    }

    private boolean matchWithVariableInSubject(@NotNull RDFTriple referenceTriple, @NotNull RDFTriple findingTriple) {
        return findingTriple.isObjectVariable()
                && referenceTriple.getPredicate().equals(findingTriple.getPredicate())
                && referenceTriple.getSubject().equals(findingTriple.getSubject());
    }

    private boolean matchWithTwoVariables(@NotNull RDFTriple referenceTriple, @NotNull RDFTriple findingTriple) {
        return findingTriple.isObjectVariable()
                && findingTriple.getPredicate().equals(referenceTriple.getPredicate())
                && findingTriple.isSubjectVariable();
    }

    private boolean verifyFact(@NotNull RDFTriple factToVerify) {
        return informationBase.contains(factToVerify);
    }

    public boolean verifyFacts(@NotNull Set<RDFTriple> factsToVerify) {
        for (RDFTriple fact : factsToVerify) {
            if (!verifyFact(fact)) {
                return false;
            }
        }

        return true;
    }
}
