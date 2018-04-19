package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
final class LocationTreeService {

    private LocationTree locationTree;
    private final String levelUpPredicate;
    private Map<String, Set<RDFTriple>> objectLocationMap;

    LocationTreeService(String delimiter, String levelUpPredicate) {
        this.locationTree = new LocationTree("locations", delimiter);
        objectLocationMap = new HashMap<>();
        this.levelUpPredicate = levelUpPredicate;
    }

    void createLocationTree(Set<RDFTriple> locationInformation) {
        addTopLevelLocations(findTopLevelLocationInformation(locationInformation));

        //TODO: 5?!?!?
        // Opakuj algoritmus od kroku 2, dokud se přidávají nové informace do stromu nebo se strom mení
        for (int i = 0; i < 5; i++) {
            Set<String> locationLeafs = locationTree.getLeafs();
            Set<RDFTriple> leafLocationsInformation = findLocationsInformationForLeafs(locationLeafs);

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

    @Nullable
    String findResourceLocation(String resourceName) {
        return locationTree.findLocation(resourceName);
    }

    void addLocationInformation(RDFTriple locationInformation) {
        if (locationInformation.getPredicate().equals(levelUpPredicate)) {
            locationTree.addLocation(locationInformation);
        }
    }

    void removeLocationInformation(RDFTriple locationInformation){
        if (locationInformation.getPredicate().equals(levelUpPredicate)){
            locationTree.removeLocation(locationInformation);
        }
    }
}
