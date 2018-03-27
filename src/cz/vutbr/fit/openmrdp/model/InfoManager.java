package cz.vutbr.fit.openmrdp.model;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

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
    //TODO: this constant should be specified in ontology
    static final String LOCATION_PREDICATE = "<loc:locatedIn>";
    static final String PATH_PREDICATE = "<loc:contains>";

    public InfoManager(InformationBaseService informationBaseService) {
        this.informationBaseService = informationBaseService;
        //TODO: apply IoC
        this.locationTreeService = new LocationTreeService();
    }

    public void createInformationBase() {
        if (!initialized) {
            this.informationBase = informationBaseService.loadInformationBase();
            initialized = true;

            locationTreeService.createLocationTree(getLocationInformation());
        }
    }

    private Set<RDFTriple> getLocationInformation() {
        Set<RDFTriple> locationInformation = new HashSet<>();

        for (RDFTriple triple : informationBase) {
            if (triple.getPredicate().equals(LOCATION_PREDICATE)) {
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

    Set<RDFTriple> findAllMatchingPatterns(Set<String> variables) {
        //TODO: this is wrong. Reimplement
        Set<RDFTriple> matchingPatterns = new HashSet<>();

        for (RDFTriple triple : informationBase) {
            if (variables.contains(triple.getObject()) || variables.contains(triple.getSubject())) {
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
}
