package cz.vutbr.fit.openmrdp.model;

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

    private final LocationTreeService service = new LocationTreeService();
    private static final String EXPECTED_PATH_FOR_FUEL1_RESOURCE = "urn:uuid:room1<loc:contains>urn:uuid:box1<loc:contains>urn:uuid:fuel1";
    private static final String FUEL_RESOURCE_NAME = "urn:uuid:fuel1";
    private static final String NON_EXISTING_RESOURCE_NAME = "urn:uuid:nonExistingResourceName";

    @Before
    public void initializeLocationTree(){
        service.createLocationTree(createLocationInformationTestSet());
    }

    @Test
    public void findLocationForExistingResource() {
        assertThat(service.findResourceLocation(FUEL_RESOURCE_NAME), is(EXPECTED_PATH_FOR_FUEL1_RESOURCE));
    }

    @Test
    public void findLocationForNonExistingResource(){
        assertThat(service.findResourceLocation(NON_EXISTING_RESOURCE_NAME), is(nullValue()));
    }

    private Set<RDFTriple> createLocationInformationTestSet(){
        Set<RDFTriple> locationInformation = new HashSet<>();
        InformationBaseService service = new InformationBaseTestService();

        for (RDFTriple triple : service.loadInformationBase()) {
            if (triple.getPredicate().equals(InfoManager.LOCATION_PREDICATE)) {
                locationInformation.add(triple);
            }
        }

        return locationInformation;
    }
}