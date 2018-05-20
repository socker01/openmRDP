package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service used for manipulating with information in the {@link LocationTree}.
 *
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
final class LocationTreeService {

    private static final int MAXIMUM_ITERATION = 100;

    private LocationTree locationTree;
    private final String levelUpPredicate;
    private Map<String, Set<RDFTriple>> objectLocationMap;

    LocationTreeService(String delimiter, String levelUpPredicate) {
        this.locationTree = new LocationTree("locations", delimiter);
        objectLocationMap = new HashMap<>();
        this.levelUpPredicate = levelUpPredicate;
    }

    /**
     * Create tree of the locations {@link LocationTree} from the list of location information.
     *
     * @param locationInformation - {@link java.util.List} of locations information
     */
    void createLocationTree(Set<RDFTriple> locationInformation) {
        addTopLevelLocations(findTopLevelLocationInformation(locationInformation));

        Set<RDFTriple> previousLeafLocationsInformation = new HashSet<>();
        for (int i = 0; i < MAXIMUM_ITERATION; i++) {
            Set<String> locationLeafs = locationTree.getLeafs();
            Set<RDFTriple> leafLocationsInformation = findLocationsInformationForLeafs(locationLeafs);

            if (previousLeafLocationsInformation.containsAll(leafLocationsInformation)) {
                break;
            } else {
                previousLeafLocationsInformation = leafLocationsInformation;
            }

            for (RDFTriple leafLocationInformation : leafLocationsInformation) {
                locationTree.addLocation(leafLocationInformation);
            }
        }
    }

    private Set<RDFTriple> findLocationsInformationForLeafs(Set<String> locationLeafs) {
        Set<RDFTriple> locationsInformation = new HashSet<>();

        for (String locationLeaf : locationLeafs) {
            if (objectLocationMap.containsKey(locationLeaf)) {
                locationsInformation.addAll(objectLocationMap.get(locationLeaf));
            }
        }

        return locationsInformation;
    }

    private void addTopLevelLocations(Set<String> topLevelLocations) {
        locationTree.addTopLevelLocations(topLevelLocations);
    }

    private Set<String> findTopLevelLocationInformation(Set<RDFTriple> locationInformation) {

        Set<String> objects = new HashSet<>();
        Set<String> subjects = new HashSet<>();

        for (RDFTriple triple : locationInformation) {
            if (objectLocationMap.containsKey(triple.getObject())) {
                objectLocationMap.get(triple.getObject()).add(triple);
            } else {
                objectLocationMap.put(triple.getObject(), Sets.newHashSet(triple));
            }
            objects.add(triple.getObject());
            subjects.add(triple.getSubject());
        }

        objects.removeAll(subjects);

        return objects;
    }

    /**
     * Find resource in the {@link LocationTree}
     *
     * @param resourceName - name of the finding resource
     * @return - Found resource location. Returns null if the resource is not int the {@link LocationTree}
     */
    @Nullable
    String findResourceLocation(String resourceName) {
        return locationTree.findLocation(resourceName);
    }

    /**
     * Add location information into the location tree
     *
     * @param locationInformation - information to add
     */
    void addLocationInformation(RDFTriple locationInformation) {
        if (locationInformation.getPredicate().equals(levelUpPredicate)) {
            locationTree.addLocation(locationInformation);
        }
    }

    /**
     * Remove location information from the location tree
     *
     * @param locationInformation - information to remove
     */
    void removeLocationInformation(RDFTriple locationInformation){
        if (locationInformation.getPredicate().equals(levelUpPredicate)){
            locationTree.removeLocation(locationInformation);
        }
    }
}
