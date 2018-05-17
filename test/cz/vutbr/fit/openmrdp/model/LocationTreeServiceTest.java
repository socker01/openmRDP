package cz.vutbr.fit.openmrdp.model;

import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseService;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class LocationTreeServiceTest {

    private static final String DEFAULT_LEVEL_UP_PATH_PREDICATE = "<loc:locatedIn>";
    private static final String DEFAULT_DELIMITER = "\\";
    private static final String EXPECTED_PATH_FOR_FUEL1_RESOURCE = "urn:uuid:room1\\urn:uuid:box1\\urn:uuid:fuel1";
    private static final String FUEL_RESOURCE_NAME = "urn:uuid:fuel1";
    private static final String TEST_RESOURCE_NAME = "urn:uuid:test";
    private static final String NON_EXISTING_RESOURCE_NAME = "urn:uuid:nonExistingResourceName";
    private static final String NEW_TEST_RESOURCE = "urn:uuid:newTestResource";

    private final LocationTreeService service = new LocationTreeService(DEFAULT_DELIMITER, DEFAULT_LEVEL_UP_PATH_PREDICATE);

    @Before
    public void initializeLocationTree() {
        service.createLocationTree(createLocationInformationTestSet());
    }

    @Test
    public void findLocationForExistingResource() {
        assertThat(service.findResourceLocation(FUEL_RESOURCE_NAME), is(EXPECTED_PATH_FOR_FUEL1_RESOURCE));
    }

    @Test
    public void findLocationForNonExistingResource() {
        assertThat(service.findResourceLocation(NON_EXISTING_RESOURCE_NAME), is(nullValue()));
    }

    private Set<RDFTriple> createLocationInformationTestSet() {
        Set<RDFTriple> locationInformation = new HashSet<>();
        InformationBaseService service = new InformationBaseTestService();

        for (RDFTriple triple : service.loadInformationBase()) {
            if (triple.getPredicate().equals(DEFAULT_LEVEL_UP_PATH_PREDICATE)) {
                locationInformation.add(triple);
            }
        }

        return locationInformation;
    }

    @Test
    public void addLocationInformation(){
        RDFTriple newLocationInformation = new RDFTriple(TEST_RESOURCE_NAME, "<loc:locatedIn>", FUEL_RESOURCE_NAME);

        service.addLocationInformation(newLocationInformation);

        assertThat(service.findResourceLocation(TEST_RESOURCE_NAME), is("urn:uuid:room1\\urn:uuid:box1\\urn:uuid:fuel1\\urn:uuid:test"));
    }

    @Test
    public void addLocationInformationWithContainsInfo(){
        RDFTriple newLocationInformation = new RDFTriple(FUEL_RESOURCE_NAME, "<loc:contains>", TEST_RESOURCE_NAME);
        service.addLocationInformation(newLocationInformation);

        assertThat(service.findResourceLocation(TEST_RESOURCE_NAME), is(nullValue()));
    }

    @Test
    public void removeLocationFromTree(){
        RDFTriple newLocationInformation = new RDFTriple(NEW_TEST_RESOURCE, "<loc:locatedIn>", FUEL_RESOURCE_NAME);
        service.addLocationInformation(newLocationInformation);

        assertThat(service.findResourceLocation(NEW_TEST_RESOURCE), is("urn:uuid:room1\\urn:uuid:box1\\urn:uuid:fuel1\\urn:uuid:newTestResource"));

        service.removeLocationInformation(newLocationInformation);

        assertThat(service.findResourceLocation(NEW_TEST_RESOURCE), is(nullValue()));
    }
}